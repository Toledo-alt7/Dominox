package br.maua.dominox;

import java.util.Set;

public class Fase2AcidoBase implements Fase {
    @Override
    public String getNome() {
        return "Ácidos e Bases - 2";
    }

    @Override
    public int getNumeroFase() {
        return 2;
    }

    private static final String[][] PECAS = {
        {"Ácido", "Base"},        // 1
        {"HCl", "Tetra-"},   // 2
        {"H₂S", "Forte"},       // 3
        {"H₃PO₄", "Di-"},      // 4
        {"H₄P₂O₇", "Al(OH)₃"},     // 5
        {"NaOH", "Ácido"},       // 6
        {"Ca(OH)₂", "Hidrácido"},   // 7
        {"Sn(OH)₄", "Oxiácido"},    // 8
        {"Sn(OH)₄", "Forte"},       // 9
        {"HCl", "Mono-"},    // 10
        {"H₂S", "Tri-"},     // 11
        {"H₃PO₄", "Moderado"},    // 12
        {"H₄P₂O₇", "Mono-"},    // 13
        {"Ácido", "NaOH"},      // 14
        {"Ca(OH)₂", "Moderado"},     // 15
        {"Al(OH)₃", "Mono-"},       // 16
        {"Fraco", "Tetra-"},    // 17
        {"Fraco", "Oxiácido"},   // 18
        {"Ácido", "Tri-"},    // 19
        {"Base", "Moderado"},    // 20
        {"Base", "Hidrácido"},       // 21
        {"Base", "Oxiácido"},      // 22
        {"Forte", "Hidrácido"},     // 23
        {"Tetra-", "Forte"},   // 24
        {"Fraco", "Di-"},    // 25
        {"Forte", "Tri-"},       // 26
        {"Fraco", "Ácido"},       // 27
        {"Ácido", "Di-"},       // 28
    };

    public String[][] getPecas() {
        return PECAS;
    }

    //Conexões


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
