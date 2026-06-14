package br.maua.dominox;

import java.util.Map;
import java.util.Set;

public class DynamicFase implements Fase{
    private final String nome;
    private final int numeroFase;
    private final String[][] pecas;
    private final Map<String, Set<String>> conexoes;

    // O construtor recebe todos os dados vindos do banco
    public DynamicFase(String nome, int numeroFase, String[][] pecas, Map<String, Set<String>> conexoes) {
        this.nome = nome;
        this.numeroFase = numeroFase;
        this.pecas = pecas;
        this.conexoes = conexoes;
    }

    @Override
    public String getNome() {
        return this.nome;
    }

    @Override
    public int getNumeroFase() {
        return this.numeroFase;
    }

    @Override
    public String[][] getPecas() {
        return this.pecas;
    }

    @Override
    public boolean validarConexao(String lado1, String lado2) {
        if (lado1 == null || lado2 == null) return false;
        
        // Se a peça conectar com ela mesma, é true automaticamente
        if (lado1.equals(lado2)) return true;
        
        Set<String> vizinhos = conexoes.get(lado1);
        return vizinhos != null && vizinhos.contains(lado2);
    }
}
