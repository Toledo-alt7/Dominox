package br.maua.dominox;

import java.util.HashMap;
import java.util.Map;
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

    private Map<String, Set<String>> conexoes;
    public Fase2AcidoBase(){
        conexoes = new HashMap<>();
        selfConnect(conexoes, "Ácido");
        selfConnect(conexoes, "Base");
        selfConnect(conexoes, "Mono-");
        selfConnect(conexoes,"Di-");
        selfConnect(conexoes, "Tri-");
        selfConnect(conexoes, "Forte");
        selfConnect(conexoes, "Moderado");
        selfConnect(conexoes, "Fraco");
        selfConnect(conexoes, "Hidrácido");
        selfConnect(conexoes, "Oxiácido");
        selfConnect(conexoes, "HCl");
        selfConnect(conexoes, "H₂S");
        selfConnect(conexoes, "H₃PO₄");
        selfConnect(conexoes, "H₄P₂O₇");
        selfConnect(conexoes, "NaOH");
        selfConnect(conexoes, "Ca(OH)₂");
        selfConnect(conexoes, "Al(OH)₃");
        selfConnect(conexoes, "Sn(OH)₄");

        connect(conexoes, "Ácido", "Mono-");
        connect(conexoes, "Ácido", "Di-");
        connect(conexoes, "Ácido", "Tri-");
        connect(conexoes, "Ácido", "Tetra-");
        connect(conexoes, "Ácido", "Fraco");
        connect(conexoes, "Ácido", "Moderado");
        connect(conexoes, "Ácido", "Forte");
        connect(conexoes, "Ácido", "Hidrácido");
        connect(conexoes, "Ácido", "Oxiácido");
        connect(conexoes, "Ácido", "HCl");
        connect(conexoes, "Ácido", "H₂S");
        connect(conexoes, "Ácido", "H₃PO₄");
        connect(conexoes, "Ácido", "H₄P₂O₇");

        connect(conexoes, "Base", "Mono-");
        connect(conexoes, "Base", "Di-");
        connect(conexoes, "Base", "Tri-");
        connect(conexoes, "Base", "Tetra-");
        connect(conexoes, "Base", "Fraco");
        connect(conexoes, "Base", "Forte");
        connect(conexoes, "Base", "NaOH");
        connect(conexoes, "Base", "Ca(OH)₂");
        connect(conexoes, "Base", "Al(OH)₃");
        connect(conexoes, "Base", "Sn(OH)₄");

        connect(conexoes, "Mono-", "NaOH");
        connect(conexoes, "Mono-", "HCl");

        connect(conexoes, "Di-", "Ca(OH)₂");
        connect(conexoes, "Di-", "H₂S");

        connect(conexoes, "Tri-", "Al(OH)₃");
        connect(conexoes, "Tri-", "H₃PO₄");

        connect(conexoes, "Tetra-", "Sn(OH)₄");
        connect(conexoes, "Tetra-", "H₄P₂O₇");

        connect(conexoes, "Fraco", "H₂S");
        connect(conexoes, "Fraco", "Al(OH)₃");
        connect(conexoes, "Fraco", "Sn(OH)₄");

        connect(conexoes, "Forte", "HCl");
        connect(conexoes, "Forte", "NaOH");
        connect(conexoes, "Forte", "H₄P₂O₇");
        connect(conexoes, "Forte", "Ca(OH)₂");

        connect(conexoes, "Moderado", "H₃PO₄");

        connect(conexoes, "Oxiácido", "H₃PO₄");
        connect(conexoes, "Oxiácido", "H₄P₂O₇");

        connect(conexoes, "Hidrácido", "HCl");
        connect(conexoes, "Hidrácido", "H₂S");
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
