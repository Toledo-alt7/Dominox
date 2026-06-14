package br.maua.dominox;

import java.sql.*;
import java.util.*;

public class FaseRegistry {

    public static Fase getFase(int numeroFase) {
        // Queries SQL para buscar os dados específicos da fase solicitada
        String sqlFase = "SELECT nome FROM fases WHERE numero = ?";
        String sqlPecas = "SELECT lado_a, lado_b FROM pecas WHERE fase_numero = ?";
        String sqlConexoes = "SELECT termo_a, termo_b FROM conexoes WHERE fase_numero = ?";

        String nomeFase = null;
        List<String[]> listaPecas = new ArrayList<>();
        Map<String, Set<String>> mapaConexoes = new HashMap<>();

        // Usa a conexão centralizada do seu projeto
        try (Connection conn = ConnectionDB.getConexao()) {
            if (conn == null) {
                System.err.println("Erro: Sem conexão com o banco para buscar a fase.");
                return null;
            }

            // Busca o nome da Fase
            try (PreparedStatement stmt = conn.prepareStatement(sqlFase)) {
                stmt.setInt(1, numeroFase);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        nomeFase = rs.getString("nome");
                    } else {
                        System.err.println("Aviso: Fase " + numeroFase + " não cadastrada no Banco de Dados.");
                        return null;
                    }
                }
            }

            // Busca todas as peças da Fase
            try (PreparedStatement stmt = conn.prepareStatement(sqlPecas)) {
                stmt.setInt(1, numeroFase);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        listaPecas.add(new String[]{rs.getString("lado_a"), rs.getString("lado_b")});
                    }
                }
            }

            // Busca todas as regras de conexões da Fase
            try (PreparedStatement stmt = conn.prepareStatement(sqlConexoes)) {
                stmt.setInt(1, numeroFase);
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String termoA = rs.getString("termo_a");
                        String termoB = rs.getString("termo_b");

                        // Insere de forma bidirecional no mapa (A conecta a B, B conecta a A)
                        mapaConexoes.computeIfAbsent(termoA, k -> new HashSet<>()).add(termoB);
                        mapaConexoes.computeIfAbsent(termoB, k -> new HashSet<>()).add(termoA);
                    }
                }
            }

            // Converte a lista do Java para a matriz bidimensional de Strings exigida pela interface Fase
            String[][] matrizPecas = listaPecas.toArray(new String[0][0]);

            // Cria e retorna o objeto da fase preenchido com os dados do MySQL
            return new DynamicFase(nomeFase, numeroFase, matrizPecas, mapaConexoes);

        } catch (SQLException e) {
            System.err.println("Erro crítico ao acessar a fase " + numeroFase + " no banco de dados.");
            e.printStackTrace();
            return null;
        }
    }
}