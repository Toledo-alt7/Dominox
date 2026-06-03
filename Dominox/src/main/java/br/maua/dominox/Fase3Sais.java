package br.maua.dominox;

import java.util.HashMap;
import java.util.Map;
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
        {"Na₂CO₃",  "Insolúvel"},       // 9
        {"Na₂CO₃",    "Halóide"},    // 10
        {"PbCl₂",    "Solúvel"},     // 11
        {"PbCl₂",      "Oxissal"},    // 12
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

    private Map<String, Set<String>> conexoes;
    public Fase3Sais(){
        conexoes = new HashMap<>();
        selfConnect(conexoes, "Oxissal");
        selfConnect(conexoes, "Halóide");
        selfConnect(conexoes, "Insolúvel");
        selfConnect(conexoes, "Solúvel");
        selfConnect(conexoes, "Ácido");
        selfConnect(conexoes, "Neutro");
        selfConnect(conexoes, "Básico");
        selfConnect(conexoes, "NaCl");
        selfConnect(conexoes, "CaCO₃");
        selfConnect(conexoes, "NH₄Cl");
        selfConnect(conexoes, "BaSO₄");
        selfConnect(conexoes, "Na₂CO₃");
        selfConnect(conexoes, "PbCl₂");
        
        connect(conexoes, "Oxissal", "CaCO₃");
        connect(conexoes, "Oxissal", "BaSO₄");
        connect(conexoes, "Oxissal", "Na₂CO₃");

        connect(conexoes, "Halóide", "NaCl");
        connect(conexoes, "Halóide", "NH₄Cl");
        connect(conexoes, "Halóide", "PbCl₂");

        connect(conexoes, "Solúvel", "Na₂CO₃");
        connect(conexoes, "Solúvel", "NaCl");
        connect(conexoes, "Solúvel", "NH₄Cl");

        connect(conexoes, "Insolúvel", "CaCO₃");
        connect(conexoes, "Insolúvel", "BaSO₄");
        connect(conexoes, "Insolúvel", "PbCl₂");

        connect(conexoes, "Neutro", "BaSO₄");
        connect(conexoes, "Neutro", "NaCl");

        connect(conexoes, "Ácido", "NH₄Cl");
        connect(conexoes, "Ácido", "PbCl₂");

        connect(conexoes, "Básico", "CaCO₃");
        connect(conexoes, "Básico", "Na₂CO₃");
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