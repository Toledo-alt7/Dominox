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

    // Esse Objeto valida se o acesso é valido 
    public void validarAcesso(LoginPage loginPage) {
        
        String email = loginPage.userIDField.getText();
        String senha = String.valueOf(loginPage.userPasswordField.getPassword());    
        String sql = "SELECT id_usuario, ativo FROM usuario WHERE email = ? AND senha = ?";
        
        // Testa se é possível conectar com o DB
        try (Connection conn = ConnectionDB.getConexao()) {
            if (conn == null) {
                loginPage.messageLabel.setText("Erro: Banco de dados inacessível.");
                return;
            }
            // Recebe o email e a senha
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, senha);

                // Faz a parte da validação e verifica se está ativo
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        
                        int idRecuperado = rs.getInt("id_usuario");
                        boolean ativo = rs.getBoolean("ativo");

                        if (ativo) {
                            loginPage.frame.dispose();
                            new WindowPage(); 
                        } else {
                            loginPage.messageLabel.setForeground(Color.orange);
                            loginPage.messageLabel.setText("Esta conta está inativa.");
                        }
                    } else {
                        
                        loginPage.messageLabel.setForeground(Color.red);
                        loginPage.messageLabel.setText("Email ou senha incorretos.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            loginPage.messageLabel.setText("Erro técnico: " + ex.getMessage());
        }
    }
}
