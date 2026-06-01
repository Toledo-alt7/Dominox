package br.maua.dominox;
import java.util.*;

public class GameEngine {

    // ---- All 28 pieces ----
    private static final String[][] PIECES = {
        {"Ácido",    "Base"},        // 1
        {"NaOH",     "Monoácido"},   // 2
        {"HCN",      "Forte"},       // 3
        {"H2SO4",    "Dibase"},      // 4
        {"H3PO4",    "Al(OH)3"},     // 5
        {"Mg(OH)2",  "Ácido"},       // 6
        {"NaOH",     "Hidrácido"},   // 7
        {"Al(OH)3",  "Oxiácido"},    // 8
        {"Mg(OH)2",  "Forte"},       // 9
        {"H2SO4",    "Monobase"},    // 10
        {"H3PO4",    "Tribase"},     // 11
        {"HCN",      "Moderado"},    // 12
        {"Fraco",    "Monobase"},    // 13
        {"Ácido",    "Dibase"},      // 14
        {"Ácido",    "Tribase"},     // 15
        {"Ácido",    "Fraco"},       // 16
        {"Base",     "Moderado"},    // 17
        {"Base",     "Hidrácido"},   // 18
        {"Base",     "Oxiácido"},    // 19
        {"Monoácido","Monobase"},    // 20
        {"Monoácido","Forte"},       // 21
        {"Diácido",  "Dibase"},      // 22
        {"Triácido", "Tribase"},     // 23
        {"Diácido",  "Hidrácido"},   // 24
        {"Diácido",  "Moderado"},    // 25
        {"Triácido", "Forte"},       // 26
        {"Triácido", "Fraco"},       // 27
        {"Oxiácido", "Fraco"},       // 28
    };

    private List<Player> players;
    private List<Domino> board;
    private String leftEnd;
    private String rightEnd;
    private int currentPlayerIndex;
    private String statusMessage;
    private boolean JogoOver;
    private String winner;

    public GameEngine() {
        startNewJogo();
    }

    public void startNewJogo() {
        board = new ArrayList<>();
        leftEnd = null;
        rightEnd = null;
        JogoOver = false;
        winner = null;

        // Build and shuffle dominoes
        List<Domino> all = new ArrayList<>();
        for (int i = 0; i < PIECES.length; i++) {
            all.add(new Domino(i + 1, PIECES[i][0], PIECES[i][1]));
        }
        Collections.shuffle(all);

        // Create players
        players = new ArrayList<>();
        players.add(new Player("Você", true));
        players.add(new Player("Bot 1", false));
        players.add(new Player("Bot 2", false));
        players.add(new Player("Bot 3", false));

        // Deal 7 each
        for (int i = 0; i < 28; i++) {
            players.get(i % 4).addDomino(all.get(i));
        }

        // First player: whoever has piece #1 (Ácido//Base) starts
        currentPlayerIndex = findStartingPlayer();
        statusMessage = players.get(currentPlayerIndex).getName() + " começa!";
    }

    private int findStartingPlayer() {
        // Player with most "connections" starts, simplified: just player 0
        return 0;
    }

    public boolean isBoardEmpty() { return board.isEmpty(); }

    // ---- Human play ----

    /** Play a domino from human hand to left or right end. Returns true on success. */
    public boolean humanPlay(Domino d, boolean toLeft) {
        if (!isHumanTurn()) return false;
        Player human = players.get(0);

        if (board.isEmpty()) {
            placeDomino(d, true);
            human.removeDomino(d);
            advanceTurn();
            return true;
        }

        String targetEnd = toLeft ? leftEnd : rightEnd;
        if (!ChemistryRules.dominoFits(d, targetEnd)) {
            statusMessage = "Essa peça não encaixa nessa ponta!";
            return false;
        }

        orientAndPlace(d, targetEnd, toLeft);
        human.removeDomino(d);
        advanceTurn();
        return true;
    }

    public boolean humanCanPlay() {
        Player human = players.get(0);
        if (board.isEmpty()) return !human.getPecas().isEmpty();
        return human.hasPlayable(leftEnd, rightEnd, false);
    }

    public boolean humanPass() {
        if (!isHumanTurn()) return false;
        if (humanCanPlay()) {
            statusMessage = "Você tem peças jogáveis, não pode passar!";
            return false;
        }
        statusMessage = "Você passou.";
        advanceTurn();
        return true;
    }

    // ---- Bot play ----

    /** Execute one bot turn. Returns true if bot played a domino, false if passed. */
    public boolean botPlay() {
        if (isHumanTurn() || JogoOver) return false;
        Player bot = players.get(currentPlayerIndex);

        Domino chosen = bot.pickBotMove(leftEnd, rightEnd, board.isEmpty());
        if (chosen == null) {
            statusMessage = bot.getName() + " passou.";
            advanceTurn();
            return false;
        }

        if (board.isEmpty()) {
            placeDomino(chosen, true);
        } else {
            // Prefer left end
            boolean fitsLeft  = ChemistryRules.dominoFits(chosen, leftEnd);
            boolean fitsRight = ChemistryRules.dominoFits(chosen, rightEnd);
            if (fitsLeft) {
                orientAndPlace(chosen, leftEnd, true);
            } else {
                orientAndPlace(chosen, rightEnd, false);
            }
        }
        bot.removeDomino(chosen);
        statusMessage = bot.getName() + " jogou " + chosen;
        advanceTurn();
        return true;
    }

    // ---- Board placement ----

    private void placeDomino(Domino d, boolean leftFacesLeft) {
        board.add(d);
        if (leftFacesLeft) {
            leftEnd  = d.getLeft();
            rightEnd = d.getRight();
        } else {
            leftEnd  = d.getRight();
            rightEnd = d.getLeft();
        }
    }

    /**
     * Orient and insert domino so it connects to targetEnd at the given side.
     * After placement, the outward-facing side becomes the new end.
     */
    private void orientAndPlace(Domino d, String targetEnd, boolean toLeft) {
        boolean leftConnects  = ChemistryRules.canConnect(d.getLeft(), targetEnd);
        boolean rightConnects = ChemistryRules.canConnect(d.getRight(), targetEnd);

        // Decide which side touches the board (inward) and which is the new end (outward)
        // When playing to the LEFT end: inward side goes right (adjacent to board), outward goes left
        // When playing to the RIGHT end: inward side goes left (adjacent to board), outward goes right
        if (toLeft) {
            // We want the RIGHT side of the domino to connect to leftEnd
            if (!rightConnects && leftConnects) {
                d.flip(); // flip so the connecting side is on the right
            }
            // After possible flip: d.getRight() connects to targetEnd, d.getLeft() is new leftEnd
            board.add(0, d);
            leftEnd = d.getLeft();
        } else {
            // We want the LEFT side of the domino to connect to rightEnd
            if (!leftConnects && rightConnects) {
                d.flip(); // flip so the connecting side is on the left
            }
            // After possible flip: d.getLeft() connects to targetEnd, d.getRight() is new rightEnd
            board.add(d);
            rightEnd = d.getRight();
        }
    }

    // ---- Turn management ----

    private void advanceTurn() {
        if (checkJogoOver()) return;
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
        Player next = players.get(currentPlayerIndex);
        if (!next.isHuman()) {
            // Status will be set by botPlay()
        } else {
            statusMessage = "Sua vez! " + (humanCanPlay() ? "Selecione uma peça." : "Sem jogadas — passe.");
        }
    }

    private boolean checkJogoOver() {
        // Someone emptied hand
        for (Player p : players) {
            if (p.hasEmptyPecas()) {
                JogoOver = true;
                winner = p.getName() + " venceu! 🎉";
                statusMessage = winner;
                return true;
            }
        }
        // All blocked (no one can play)
        boolean allBlocked = true;
        for (Player p : players) {
            if (board.isEmpty() || p.hasPlayable(leftEnd, rightEnd, false)) {
                allBlocked = false;
                break;
            }
        }
        if (allBlocked) {
            JogoOver = true;
            // Fewest cards wins
            int min = Integer.MAX_VALUE;
            Player minPlayer = null;
            for (Player p : players) {
                if (p.countPips() < min) { min = p.countPips(); minPlayer = p; }
            }
            winner = minPlayer.getName() + " venceu com menos peças! 🎉";
            statusMessage = winner;
            return true;
        }
        return false;
    }

    // ---- Getters ----

    public boolean isHumanTurn()       { return currentPlayerIndex == 0; }
    public boolean isJogoOver()        { return JogoOver; }
    public String getWinner()          { return winner; }
    public String getStatusMessage()   { return statusMessage; }
    public List<Domino> getBoard()     { return board; }
    public String getLeftEnd()         { return leftEnd; }
    public String getRightEnd()        { return rightEnd; }
    public List<Player> getPlayers()   { return players; }
    public Player getHumanPlayer()     { return players.get(0); }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public String getCurrentPlayerName() { return players.get(currentPlayerIndex).getName(); }

    public void setStatusMessage(String msg) { statusMessage = msg; }
}