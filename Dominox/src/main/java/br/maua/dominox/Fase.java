package br.maua.dominox;


public interface Fase {
    
    // Métodos obrigatórios que a FaseDinamica já está implementando
    public String getNome();
    public int getNumeroFase();
    public String[][] getPecas();
    public boolean validarConexao(String lado1, String lado2);

    // Esse método serve para o jogo testar se a peça (Domino) 
    // que o aluno está segurando encaixa na ponta do tabuleiro (boardEnd).
    default boolean dominoEncaixa(Domino d, String boardEnd) {
        return validarConexao(d.getLeft(), boardEnd) || validarConexao(d.getRight(), boardEnd);
    }
}