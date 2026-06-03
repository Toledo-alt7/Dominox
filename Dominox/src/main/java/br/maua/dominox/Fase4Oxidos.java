package br.maua.dominox;

import java.util.Set;

public class Fase4Oxidos implements Fase{
    @Override
    public String getNome() {
        return "Óxidos";
    }

    @Override
    public int getNumeroFase() {
        return 4;
    }

    private static final String[][] PECAS = {
        {"CO₂", "Básico"},        // 1
        {"CO₂", "Iônico"},   // 2
        {"SO₃", "Neutro"},       // 3
        {"SO₃", "Anfótero"},      // 4
        {"CaO", "Ácido"},     // 5
        {"CaO", "Covalente"},       // 6
        {"Na₂O", "Neutro"},   // 7
        {"Na₂O", "Anfótero"},    // 8
        {"CO", "Ácido"},       // 9
        {"CO", "Iônico"},    // 10
        {"Al₂O₃", "Ácido"},     // 11
        {"Al₂O₃", "Covalente"},    // 12
        {"Ácido", "Básico"},    // 13
        {"Básico", "Básico"},      // 14
        {"Ácido", "Neutro"},     // 15
        {"Ácido", "Anfótero"},       // 16
        {"Ácido", "Iônico"},    // 17
        {"Ácido", "Covalente"},   // 18
        {"Neutro", "Básico"},    // 19
        {"Anfótero", "Básico"},    // 20
        {"Básico", "Iônico"},       // 21
        {"Básico", "Covalente"},      // 22
        {"Neutro", "Anfótero"},     // 23
        {"Neutro", "Iônico"},   // 24
        {"Anfótero", "Iônico"},    // 25
        {"Neutro", "Covalente"},       // 26
        {"Anfótero", "Covalente"},       // 27
        {"Iônico", "Covalente"},       // 28
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