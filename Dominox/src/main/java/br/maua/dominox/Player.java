package br.maua.dominox;
import java.util.*;

public class Player {
    private final String name;
    private final boolean isHuman;
    private final List<Domino> Pecas;
    private final Fase faseAtual;

    public Player(String name, boolean isHuman, Fase faseAtual) {
        this.name = name;
        this.isHuman = isHuman;
        this.Pecas = new ArrayList<>();
        this.faseAtual = faseAtual;
    }

    public String getName()       { return name; }
    public boolean isHuman()      { return isHuman; }
    public List<Domino> getPecas() { return Pecas; }

    public void addDomino(Domino d) { Pecas.add(d); }

    public boolean removeDomino(Domino d) { return Pecas.remove(d); }

    public boolean hasPlayable(String leftEnd, String rightEnd, boolean boardEmpty) {
        for (Domino d : Pecas) {
            if (boardEmpty || encaixa(d, leftEnd) || encaixa(d, rightEnd))
                return true;
        }
        return false;
    }

    public Domino pickBotMove(String leftEnd, String rightEnd, boolean boardEmpty) {
        for (Domino d : Pecas) {
            if (boardEmpty || encaixa(d, leftEnd) || encaixa(d, rightEnd))
                return d;
        }
        return null;
    }

    public int countPips() {
        return Pecas.size();
    }

    public boolean hasEmptyPecas() { return Pecas.isEmpty(); }

    public Fase getFaseAtual() {
        return faseAtual;
    }

    private boolean encaixa(Domino d, String end) {
        return faseAtual != null && faseAtual.dominoEncaixa(d, end);
    }

    @Override
    public String toString() { return name; }
}
