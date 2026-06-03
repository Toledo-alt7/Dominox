package br.maua.dominox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Fase1AcidoBase implements Fase {
    @Override
    public String getNome() {
        return "Ácidos e Bases - 1";
    }
    @Override
    public int getNumeroFase() {
        return 1;
    }

    private static final String[][] PECAS = {
        {"Ácido",    "Base"},        // 1
        {"NaOH",     "Monoácido"},   // 2
        {"HCN",      "Forte"},       // 3
        {"H₂SO₄",    "Dibase"},      // 4
        {"H₃PO₄",    "Al(OH)₃"},     // 5
        {"Mg(OH)₂",  "Ácido"},       // 6
        {"NaOH",     "Hidrácido"},   // 7
        {"Al(OH)₃",  "Oxiácido"},    // 8
        {"Mg(OH)₂",  "Forte"},       // 9
        {"H₂SO₄",    "Monobase"},    // 10
        {"H₃PO₄",    "Tribase"},     // 11
        {"HCN",      "Moderado"},    // 12
        {"Fraco",    "Monobase"},    // 13
        {"Ácido",    "Dibase"},      // 14
        {"Ácido",    "Tribase"},     // 15
        {"Ácido",    "Fraco"},       // 16
        {"Base",     "Moderado"},    // 17
        {"Base",     "Hidrácido"},   // 18
        {"Base",     "Oxiácido"},    // 19
        {"Monoácido","Monobase"},    // 20
        {"Monoácido","Forte"},       // 21
        {"Diácido",  "Dibase"},      // 22
        {"Triácido", "Tribase"},     // 23
        {"Diácido",  "Hidrácido"},   // 24
        {"Diácido",  "Moderado"},    // 25
        {"Triácido", "Forte"},       // 26
        {"Triácido", "Fraco"},       // 27
        {"Oxiácido", "Fraco"},       // 28
    };

    public String[][] getPecas() {
        return PECAS;
    }

    private Map<String, Set<String>> conexoes;
    public Fase1AcidoBase(){
        conexoes = new HashMap<>();
        selfConnect(conexoes, "Ácido");
        selfConnect(conexoes, "Base");
        selfConnect(conexoes, "Monoácido");
        selfConnect(conexoes, "Monobase");
        selfConnect(conexoes,"Diácido");
        selfConnect(conexoes, "Dibase");
        selfConnect(conexoes, "Triácido");
        selfConnect(conexoes, "Tribase");
        selfConnect(conexoes, "Forte");
        selfConnect(conexoes, "Moderado");
        selfConnect(conexoes, "Fraco");
        selfConnect(conexoes, "Hidrácido");
        selfConnect(conexoes, "Oxiácido");
        selfConnect(conexoes, "NaOH");
        selfConnect(conexoes, "Al(OH)₃");
        selfConnect(conexoes, "Mg(OH)₂");
        selfConnect(conexoes, "H₂SO₄");
        selfConnect(conexoes, "H₃PO₄");
        selfConnect(conexoes, "HCN");

        connect(conexoes, "Ácido", "Monoácido");
        connect(conexoes, "Ácido", "Diácido");
        connect(conexoes, "Ácido", "Triácido");
        connect(conexoes, "Ácido", "Forte");
        connect(conexoes, "Ácido", "Moderado");
        connect(conexoes, "Ácido", "Fraco");
        connect(conexoes, "Ácido", "Hidrácido");
        connect(conexoes, "Ácido", "Oxiácido");
        connect(conexoes, "Ácido", "H2SO4");
        connect(conexoes, "Ácido", "H₃PO₄");
        connect(conexoes, "Ácido", "HCN");

        connect(conexoes, "Base", "Monobase");
        connect(conexoes, "Base", "Dibase");
        connect(conexoes, "Base", "Tribase");
        connect(conexoes, "Base", "Forte");
        connect(conexoes, "Base", "Fraco");
        connect(conexoes, "Base", "NaOH");
        connect(conexoes, "Base", "Al(OH)₃");
        connect(conexoes, "Base", "Mg(OH)₂");

        connect(conexoes, "Monoácido", "HCN");
        connect(conexoes, "Monobase", "NaOH");
        connect(conexoes, "Diácido", "H₂SO₄");
        connect(conexoes, "Dibase", "Mg(OH)₂");
        connect(conexoes, "Triácido", "H₃PO₄");
        connect(conexoes, "Tribase", "Al(OH)₃");
        connect(conexoes, "Forte", "NaOH");
        connect(conexoes, "Forte", "H₂SO₄");
        connect(conexoes, "Moderado", "H₃PO₄");
        connect(conexoes, "Fraco", "Al(OH)₃");
        connect(conexoes, "Fraco", "Mg(OH)₂");
        connect(conexoes, "Fraco", "HCN");
        connect(conexoes, "Hidrácido", "HCN");
        connect(conexoes, "Oxiácido", "H₂SO₄");
        connect(conexoes, "Oxiácido", "H₃PO₄");
        
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