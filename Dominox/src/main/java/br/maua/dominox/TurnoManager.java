package br.maua.dominox;

import java.util.List;

public class TurnoManager {
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean gameOver;
    private String winner;
    private GameEngine engine;

    public void iniciar (List<Player> players){
        this.players = players;
        currentPlayerIndex = 0;
    }

    public void advanceTurn() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
        Player next = players.get(currentPlayerIndex);
        if (!next.isHuman()) {
            // Status will be set by botPlay()
        } else {
            engine.setStatusMessage("Sua vez! " + (engine.humanCanPlay() ? "Selecione uma peça." : "Sem jogadas — passe."));
        }
    }

    public int findStartingPlayer() {
        return 0;
    }

    public boolean isHumanTurn()       { return currentPlayerIndex == 0; }
    public boolean isGameOver()        { return gameOver; }
    public String getWinner()          { return winner; }
    public List<Player> getPlayers()   { return players; }
    public Player getHumanPlayer()     { return players.get(0); }
    public int getCurrentPlayerIndex() { return currentPlayerIndex; }
    public String getCurrentPlayerName() { return players.get(currentPlayerIndex).getName(); }

    public void setGameOver(boolean gameOver){
        this.gameOver = gameOver;
    }
    public void setWinner(String winner){
        this.winner = winner;
    }
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }
}