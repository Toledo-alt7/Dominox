package br.maua.dominox;
import java.util.*;

public class GameEngine {
    private String statusMessage;
    private Tabuleiro tabuleiro;
    private TurnoManager turnoManager;
    private Fase faseAtual;

    public GameEngine(Fase fase) {
        this.faseAtual = fase;
        tabuleiro = new Tabuleiro();
        turnoManager = new TurnoManager();
        startNewJogo();
    }

    public void startNewJogo() {
        tabuleiro.resetar();
        turnoManager.setGameOver(false);
        turnoManager.setWinner(null);

        List<Domino> all = new ArrayList<>();
        for (int i = 0; i < faseAtual.getPecas().length; i++) {
            all.add(new Domino(i + 1, faseAtual.getPecas()[i][0], faseAtual.getPecas()[i][1]));
        }
        Collections.shuffle(all);

        List<Player> players;
        players = new ArrayList<>();
        players.add(new Player("Você", true));
        players.add(new Player("Bot 1", false));
        players.add(new Player("Bot 2", false));
        players.add(new Player("Bot 3", false));

        turnoManager.iniciar(players);

        for (int i = 0; i < 28; i++) {
            players.get(i % 4).addDomino(all.get(i));
        }

        turnoManager.setCurrentPlayerIndex(turnoManager.findStartingPlayer());
        statusMessage = turnoManager.getCurrentPlayerName() + " começa!";
    }

    private boolean checkGameOver() {
        // Someone emptied hand
        for (Player p : turnoManager.getPlayers()) {
            if (p.hasEmptyPecas()) {
                turnoManager.setGameOver(true);
                turnoManager.setWinner(p.getName() + " venceu!");
                statusMessage = getWinner();
                return true;
            }
        }
        boolean allBlocked = true;
        for (Player p : turnoManager.getPlayers()) {
            if (tabuleiro.isBoardEmpty() || p.hasPlayable(tabuleiro.getLeftEnd(), tabuleiro.getRightEnd(), false)) {
                allBlocked = false;
                break;
            }
        }
        if (allBlocked) {
            turnoManager.setGameOver(true);
            // Fewest cards wins
            int min = Integer.MAX_VALUE;
            Player minPlayer = null;
            for (Player p : turnoManager.getPlayers()) {
                if (p.countPips() < min) { min = p.countPips(); minPlayer = p; }
            }
            turnoManager.setWinner(minPlayer.getName() + " venceu com menos peças!");
            statusMessage = getWinner();
            return true;
        }
        return false;
    }

    public boolean humanPlay(Domino d, boolean toLeft) {
        if (!isHumanTurn()) return false;
        Player human = turnoManager.getPlayers().get(0);

        if (tabuleiro.isBoardEmpty()) {
            tabuleiro.placeDomino(d, true);
            human.removeDomino(d);
            turnoManager.advanceTurn();
            return true;
        }

        String targetEnd = toLeft ? tabuleiro.getLeftEnd() : tabuleiro.getRightEnd();
        if (!faseAtual.dominoEncaixa(d, targetEnd)) {
            statusMessage = "Essa peça não encaixa nessa ponta!";
            return false;
        }

        tabuleiro.orientAndPlace(d, targetEnd, toLeft);
        human.removeDomino(d);
        if (checkGameOver() == false){
            turnoManager.advanceTurn();
        };
        return true;
    }

    public boolean humanCanPlay() {
        Player human = turnoManager.getPlayers().get(0);
        if (tabuleiro.isBoardEmpty()) return !human.getPecas().isEmpty();
        return human.hasPlayable(tabuleiro.getLeftEnd(), tabuleiro.getRightEnd(), false);
    }

    public boolean humanPass() {
        if (!isHumanTurn()) return false;
        if (humanCanPlay()) {
            statusMessage = "Você tem peças jogáveis, não pode passar!";
            return false;
        }
        statusMessage = "Você passou.";
        turnoManager.advanceTurn();
        return true;
    }

    public boolean botPlay() {
        if (isHumanTurn() || isGameOver()) return false;
        Player bot = turnoManager.getPlayers().get(turnoManager.getCurrentPlayerIndex());

        Domino chosen = bot.pickBotMove(tabuleiro.getLeftEnd(), tabuleiro.getRightEnd(), tabuleiro.isBoardEmpty());
        if (chosen == null) {
            statusMessage = bot.getName() + " passou.";
            if (checkGameOver() == false){
                turnoManager.advanceTurn();
        };;
            return false;
        }

        if (tabuleiro.isBoardEmpty()) {
            tabuleiro.placeDomino(chosen, true);
        } else {
            // Prefer left end
            boolean fitsLeft  = faseAtual.dominoEncaixa(chosen, tabuleiro.getLeftEnd());
            boolean fitsRight = faseAtual.dominoEncaixa(chosen, tabuleiro.getRightEnd());
            if (fitsLeft) {
                tabuleiro.orientAndPlace(chosen, tabuleiro.getLeftEnd(), true);
            } else {
                tabuleiro.orientAndPlace(chosen, tabuleiro.getRightEnd(), false);
            }
        }
        bot.removeDomino(chosen);
        statusMessage = bot.getName() + " jogou " + chosen;
        if (checkGameOver() == false){
            turnoManager.advanceTurn();
        };
        return true;
    }
    // ---- Getters ----

    public boolean isHumanTurn()       { return turnoManager.isHumanTurn(); }
    public boolean isGameOver()        { return turnoManager.isGameOver(); }
    public String getWinner()          { return turnoManager.getWinner(); }
    public String getStatusMessage()   { return statusMessage; }
    public List<Player> getPlayers()   { return turnoManager.getPlayers(); }
    public Player getHumanPlayer()     { return turnoManager.getHumanPlayer(); }
    public int getCurrentPlayerIndex() { return turnoManager.getCurrentPlayerIndex(); }
    public String getCurrentPlayerName() { return turnoManager.getCurrentPlayerName(); }
    public List<Domino> getBoard()  { return tabuleiro.getBoard();    }
    public String getLeftEnd()      { return tabuleiro.getLeftEnd();  }
    public String getRightEnd()     { return tabuleiro.getRightEnd(); }
    public boolean isBoardEmpty()   { return tabuleiro.isBoardEmpty();}
    
    
    public void setStatusMessage(String msg) { statusMessage = msg; }
}