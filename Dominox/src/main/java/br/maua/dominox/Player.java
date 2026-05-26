package br.maua.dominox;
import java.util.*;

public class Player {
    private final String name;
    private final boolean isHuman;
    private final List<Domino> Pecas;

    public Player(String name, boolean isHuman) {
        this.name = name;
        this.isHuman = isHuman;
        this.Pecas = new ArrayList<>();
    }

    public String getName()       { return name; }
    public boolean isHuman()      { return isHuman; }
    public List<Domino> getPecas() { return Pecas; }

    public void addDomino(Domino d) { Pecas.add(d); }

    public boolean removeDomino(Domino d) { return Pecas.remove(d); }

    public boolean hasPlayable(String leftEnd, String rightEnd, boolean boardEmpty) {
        for (Domino d : Pecas) {
            if (boardEmpty || ChemistryRules.dominoFits(d, leftEnd) || ChemistryRules.dominoFits(d, rightEnd))
                return true;
        }
        return false;
    }

    /** Bot AI: pick the first domino that fits either end. Returns null if none. */
    public Domino pickBotMove(String leftEnd, String rightEnd, boolean boardEmpty) {
        for (Domino d : Pecas) {
            if (boardEmpty || ChemistryRules.dominoFits(d, leftEnd) || ChemistryRules.dominoFits(d, rightEnd))
                return d;
        }
        return null;
    }

    public int countPips() {
        // For scoring: count total "weight" — just Pecas size since values aren't numeric
        return Pecas.size();
    }

    public boolean hasEmptyPecas() { return Pecas.isEmpty(); }

    @Override
    public String toString() { return name; }
}
