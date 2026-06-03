package br.maua.dominox;

import java.util.Set;

public class Fase5Geral implements Fase {
    @Override
    public String getNome() {
        return "Geral";
    }

    @Override
    public int getNumeroFase() {
        return 5;
    }
    
    private static final String[][] PECAS = {
        {"HCl", "Base"},        // 1
        {"HCl", "Insolúvel"},   // 2
        {"H₂SO₄", "Sal"},       // 3
        {"H₂SO₄", "Fraco"},      // 4
        {"NaOH", "Ácido"},     // 5
        {"NaOH", "Covalente"},       // 6
        {"Al(OH)₃", "Óxido"},   // 7
        {"Al(OH)₃", "Forte"},    // 8
        {"NaCl", "Base"},       // 9
        {"NaCl", "Insolúvel"},    // 10
        {"CaCO₃", "Óxido"},     // 11
        {"CaCO₃", "Solúvel"},    // 12
        {"CO₂", "Sal"},    // 13
        {"CO₂", "Iônico"},      // 14
        {"CaO", "Ácido"},     // 15
        {"CaO", "Covalente"},       // 16
        {"Ácido", "Base"},    // 17
        {"Ácido", "Sal"},   // 18
        {"Ácido", "Óxido"},    // 19
        {"Base", "Sal"},    // 20
        {"Base", "Óxido"},       // 21
        {"Sal", "Óxido"},      // 22
        {"Forte", "Fraco"},     // 23
        {"Forte", "Solúvel"},   // 24
        {"Forte", "Iônico"},    // 25
        {"Fraco", "Insolúvel"},       // 26
        {"Fraco", "Covalente"},       // 27
        {"Solúvel", "Iônico"},       // 28
    };

    public String[][] getPecas() {
        return PECAS;
    }

    @Override
    public boolean validarConexao(String lado1, String lado2) {
        if (lado1 == null || lado2 == null) 
            return false;
        if (lado1.equals(lado2)) 
            return true;
        Set<String> vizinhos = conexoes.get(lado1);
        return vizinhos != null && vizinhos.contains(lado2);
    }
}

