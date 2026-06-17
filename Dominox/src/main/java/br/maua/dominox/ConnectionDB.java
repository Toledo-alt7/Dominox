package br.maua.dominox;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import io.github.cdimascio.dotenv.Dotenv;

public class ConnectionDB {
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASS = dotenv.get("DB_PASSWORD");
    private static final String HOST = dotenv.get("DB_HOST");
    private static final String DB_NAME = dotenv.get("DB_NAME");

    public static Connection getConexao() {
    try {
        //Conecta ao servidor raiz
        String urlServidor = String.format("jdbc:mysql://%s:3306/?allowMultiQueries=true", HOST); 
        Connection conn = DriverManager.getConnection(urlServidor, DB_USER, DB_PASS);

        //Cria o banco se ele não existir
        Statement stmt = conn.createStatement();
        stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS " + DB_NAME);
        stmt.close(); // Sempre feche o Statement
        conn.close(); // Fecha a conexão raiz
        
        //Agora conecta especificamente ao banco já criado
        String urlFinal = String.format("jdbc:mysql://%s:3306/%s?allowMultiQueries=true", HOST, DB_NAME);
        return DriverManager.getConnection(urlFinal, DB_USER, DB_PASS);
        
    } catch (Exception e) {
        System.err.println("Erro crítico ao conectar ou criar banco: " + e.getMessage());
        return null;
    }
}
}