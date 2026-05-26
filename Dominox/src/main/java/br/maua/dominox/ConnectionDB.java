package br.maua.dominox;

import java.sql.DriverManager;
import java.sql.Connection;
import io.github.cdimascio.dotenv.Dotenv;


/*
TODO
registroDTO
 */
public class ConnectionDB {
    
    private static final Dotenv dotenv = Dotenv.load();
    private static final String DB_USER = dotenv.get("DB_USER");
    private static final String DB_PASS = dotenv.get("DB_PASSWORD");
    private static final String PORT = "3306";
    private static final String HOST = dotenv.get("DB_HOST");
    private static final String DB = dotenv.get("DB_NAME");;
    /* Tendo em mente que o jogo deve ser 
    iniciado em um computador com um servidor mySQL funcionando a porta
    permanecerá 3306 por toda a vida útil do programa */ 

    public static Connection getConexao() {
        
        try {
            String s = String.format(
                "jdbc:mysql://%s:%s/%s", HOST, PORT, DB);
            Connection conexao = DriverManager.getConnection(
                s, DB_USER, DB_PASS);
            return conexao;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static void main(String [] args){
        System.out.println(getConexao());
    }
}
