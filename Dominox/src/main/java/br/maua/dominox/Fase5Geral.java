package br.maua.dominox;

import java.util.HashMap;
import java.util.Map;
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
        {"HCl", "Base (Básico)"},        // 1
        {"HCl", "Insolúvel"},   // 2
        {"HCN", "Sal"},       // 3
        {"HCN", "Covalente"},      // 4
        {"NaOH", "Ácido"},     // 5
        {"NaOH", "Fraco"},       // 6
        {"Al(OH)₃", "Óxido"},   // 7
        {"Al(OH)₃", "Forte"},    // 8
        {"NaCl", "Base (Básico)"},       // 9
        {"NaCl", "Insolúvel"},    // 10
        {"CaCO₃", "Óxido"},     // 11
        {"CaCO₃", "Solúvel"},    // 12
        {"CO₂", "Sal"},    // 13
        {"CO₂", "Iônico"},      // 14
        {"CaO", "Ácido"},     // 15
        {"CaO", "Covalente"},       // 16
        {"Ácido", "Base (Básico)"},    // 17
        {"Ácido", "Sal"},   // 18
        {"Ácido", "Óxido"},    // 19
        {"Base (Básico)", "Sal"},    // 20
        {"Base (Básico)", "Óxido"},       // 21
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

    private Map<String, Set<String>> conexoes;
    public Fase5Geral(){
        conexoes = new HashMap<>();
        selfConnect(conexoes, "Ácido");
        selfConnect(conexoes, "Base (Básico)");
        selfConnect(conexoes, "Sal");
        selfConnect(conexoes, "Forte");
        selfConnect(conexoes, "Fraco");
        selfConnect(conexoes, "Iônico");
        selfConnect(conexoes, "Covalente");
        selfConnect(conexoes, "Insolúvel");
        selfConnect(conexoes, "Solúvel");
        selfConnect(conexoes, "NaOH");
        selfConnect(conexoes, "Al(OH)₃");
        selfConnect(conexoes, "HCl");
        selfConnect(conexoes, "HCN");
        selfConnect(conexoes, "NaCl");
        selfConnect(conexoes, "CaCO₃");
        selfConnect(conexoes, "CO₂");
        selfConnect(conexoes, "CaO");

        connect(conexoes, "Ácido", "Sal");
        connect(conexoes, "Ácido", "Óxido");
        connect(conexoes, "Ácido", "Forte");
        connect(conexoes, "Ácido", "Fraco");
        connect(conexoes, "Ácido", "HCl");
        connect(conexoes, "Ácido", "HCN");
        connect(conexoes, "Ácido", "CO₂");

        connect(conexoes, "Base (Básico)", "Sal");
        connect(conexoes, "Base (Básico)", "Óxido");
        connect(conexoes, "Base (Básico)", "Forte");
        connect(conexoes, "Base (Básico)", "Fraco");
        connect(conexoes, "Base (Básico)", "NaOH");
        connect(conexoes, "Base (Básico)", "Al(OH)₃");
        connect(conexoes, "Base (Básico)", "CaO");
        connect(conexoes, "Base (Básico)", "CaCO₃");

        connect(conexoes, "Sal", "NaCl");
        connect(conexoes, "Sal", "CaCO₃");
        connect(conexoes, "Sal", "Solúvel");
        connect(conexoes, "Sal", "Insolúvel");

        connect(conexoes, "Óxido", "CO₂");
        connect(conexoes, "Óxido", "CaO");
        connect(conexoes, "Óxido", "Iônico");
        connect(conexoes, "Óxido", "Covalente");

        connect(conexoes, "Forte", "HCl");
        connect(conexoes, "Forte", "NaOH");

        connect(conexoes, "Fraco", "Al(OH)₃");
        connect(conexoes, "Fraco", "HCN");

        connect(conexoes, "Solúvel", "NaCl");
        connect(conexoes, "Insolúvel", "CaCO₃");

        connect(conexoes, "Covalente", "CO₂");
        connect(conexoes, "Iônico", "CaO");
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

