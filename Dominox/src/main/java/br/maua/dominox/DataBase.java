package br.maua.dominox;

import java.awt.Color;
import java.sql.*;
import javax.swing.*;

/*
TODO
adicionar data
*/

public class DataBase{

    public void cadastrarUsuario(SignUpPage sp) {
    String user = sp.userField.getText().trim();
    String pass = String.valueOf(sp.passField.getPassword());
    String confirmPass = String.valueOf(sp.confirmPassField.getPassword());

    try (Connection conn = ConnectionDB.getConexao()) {
        if (conn == null) {
            sp.messageLabel.setText("Erro: Sem conexão com o banco.");
            return;
        }
       
        setDB(conn);

        // Cadastra no DB
        String sql = "INSERT INTO usuario (email, senha, ativo) VALUES (?, ?, true)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user);
            stmt.setString(2, pass);

            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                sp.messageLabel.setForeground(new Color(0, 128, 0));
                sp.messageLabel.setText("Usuário cadastrado com sucesso!");
                
                Timer timer = new Timer(1500, e -> sp.frame.dispose());
                timer.setRepeats(false);
                timer.start();
            }
        }
        } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                sp.messageLabel.setForeground(Color.RED);
                sp.messageLabel.setText("Erro: Este usuário já existe.");
            } else {
                e.printStackTrace();
            }
        }
    }
    private void setDB(Connection conn) throws SQLException {
        String sql1 = "CREATE SCHEMA IF NOT EXISTS Dominox; ";
        String sql2 = "USE Dominox; ";
         String sql3 = "CREATE TABLE IF NOT EXISTS usuario (" +
                     "id_usuario INT AUTO_INCREMENT PRIMARY KEY, " +
                     "email VARCHAR(100) NOT NULL UNIQUE, " +
                     "senha VARCHAR(100) NOT NULL, " +
                     "ativo BOOLEAN DEFAULT TRUE);";
        
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql1);
            
        }
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql2);
            
        }
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql3);
            
        }
    }
}
