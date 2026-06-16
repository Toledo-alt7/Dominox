package br.maua.dominox;

import java.util.ArrayList;
import java.util.List;

public class Tabuleiro {
    private List<Domino> board;
    private String leftEnd;
    private String rightEnd;
    private Fase fase;

    public Tabuleiro(Fase fase){
        this.fase = fase;

    }
    void placeDomino(Domino d, boolean leftFacesLeft) {
        board.add(d);
        if (leftFacesLeft) {
            leftEnd  = d.getLeft();
            rightEnd = d.getRight();
        } else {
            leftEnd  = d.getRight();
            rightEnd = d.getLeft();
        }
    }


    void orientAndPlace(Domino d, String targetEnd, boolean toLeft) {
        boolean leftConnects  = fase.validarConexao(d.getLeft(), targetEnd);
        boolean rightConnects = fase.validarConexao(d.getRight(), targetEnd);

        if (toLeft) {
            if (!rightConnects && leftConnects) {
                d.flip();
            }
            board.add(0, d);
            leftEnd = d.getLeft();
        } else {
            if (!leftConnects && rightConnects) {
                d.flip();
            }
            board.add(d);
            rightEnd = d.getRight();
        }
    }

    public void resetar (){
        board = new ArrayList<>();
        leftEnd = null;
        rightEnd = null;
    }

    public boolean isBoardEmpty() { return board.isEmpty(); }
    public List<Domino> getBoard()     { return board; }
    public String getLeftEnd()         { return leftEnd; }
    public String getRightEnd()        { return rightEnd; }
}