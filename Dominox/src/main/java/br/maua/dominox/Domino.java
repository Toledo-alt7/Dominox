package br.maua.dominox;

public class Domino {
    private String left;
    private String right;
    private final int id;

    public Domino(int id, String left, String right) {
        this.id = id;
        this.left = left;
        this.right = right;
    }

    public int getId()     { return id; }
    public String getLeft()  { return left; }
    public String getRight() { return right; }

    public boolean matches(String value) {
        return left.equals(value) || right.equals(value);
    }

    public void flip() {
        String temp = left;
        left = right;
        right = temp;
    }

    @Override
    public String toString() {
        return "[" + left + " | " + right + "]";
    }
}
