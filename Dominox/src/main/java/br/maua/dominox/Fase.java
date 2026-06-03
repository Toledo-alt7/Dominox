package br.maua.dominox;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public interface Fase {
    public String getNome();
    public int getNumeroFase();
    public String[][] getPecas();
    public boolean validarConexao(String lado1, String lado2);
    
    default void selfConnect(Map<String, Set<String>> conexoes, String a) {
        conexoes.computeIfAbsent(a, k -> new HashSet<>()).add(a);
    }

    default void connect(Map<String, Set<String>> conexoes, String a, String b) {
        conexoes.computeIfAbsent(a, k -> new HashSet<>()).add(b);
        conexoes.computeIfAbsent(b, k -> new HashSet<>()).add(a);
    }

    default boolean dominoEncaixa(Domino d, String boardEnd) {
        return validarConexao(d.getLeft(), boardEnd) || validarConexao(d.getRight(), boardEnd);
    }
}