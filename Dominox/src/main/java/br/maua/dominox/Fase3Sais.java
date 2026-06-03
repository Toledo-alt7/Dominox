package br.maua.dominox;

import java.util.Set;

public class Fase3Sais implements Fase {
    @Override
    public String getNome() {
        return "Sais";
    }

    @Override
    public int getNumeroFase() {
        return 3;
    }

    private static final String[][] PECAS = {
        {"NaCl", "Oxissal"},        // 1
        {"NaCl", "Insolúvel"},   // 2
        {"CaCO₃", "Solúvel"},       // 3
        {"CaCO₃", "Halóide"},      // 4
        {"BaSO₄", "Solúvel"},     // 5
        {"BaSO₄", "Halóide"},       // 6
        {"NH₄Cl", "Oxissal"},   // 7
        {"NH₄Cl",  "Insolúvel"},    // 8
        {"KNO₃",  "Insolúvel"},       // 9
        {"KNO₃",    "Halóide"},    // 10
        {"AgCl",    "Solúvel"},     // 11
        {"AgCl",      "Oxissal"},    // 12
        {"Solúvel",    "Neutro"},    // 13
        {"Solúvel",    "Ácido"},      // 14
        {"Solúvel",    "Oxissal"},     // 15
        {"Halóide",    "Neutro"},       // 16
        {"Insolúvel",     "Ácido"},    // 17
        {"Insolúvel",     "Básico"},   // 18
        {"Neutro",     "Oxissal"},    // 19
        {"Neutro","Básico"},    // 20
        {"Neutro","Halóide"},       // 21
        {"Básico",  "Oxissal"},      // 22
        {"Ácido", "Oxissal"},     // 23
        {"Ácido",  "Básico"},   // 24
        {"Ácido",  "Halóide"},    // 25
        {"Halóide", "Oxissal"},       // 26
        {"Básico", "Halóide"},       // 27
        {"Neutro", "Insolúvel"},       // 28
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