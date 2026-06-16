package br.maua.dominox;

import java.awt.Color;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ValidateAccess {

    public void validarAcesso(LoginPage loginPage) {
        String email = loginPage.userIDField.getText();
        String senha = String.valueOf(loginPage.userPasswordField.getPassword());
        
        // CORREÇÃO 1: Adicionamos o 'tipo_usuario' na query
        String sql = "SELECT id_usuario, ativo, tipo_usuario FROM usuario WHERE email = ? AND senha = ?";
        
        try (Connection conn = ConnectionDB.getConexao()) {
            if (conn == null) {
                loginPage.messageLabel.setText("Erro: Banco de dados inacessível.");
                return;
            }
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, email);
                stmt.setString(2, senha);
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        int idRecuperado = rs.getInt("id_usuario");
                        boolean ativo = rs.getBoolean("ativo");
                        
                        // Resgata o tipo de usuário que veio do banco
                        String tipoUsuario = rs.getString("tipo_usuario");
                        
                        if (ativo) {
                            if (loginPage.remeberMeBox.isSelected()){
                                SessionManager.saveSession(email);
                            } else {
                                SessionManager.clearSession();
                            }
                            loginPage.frame.dispose(); // Fecha o login
                            
                            // CORREÇÃO 2: Roteamento baseado no cargo
                            if ("PROFESSOR".equals(tipoUsuario)) {
                                new PainelProfessor().setVisible(true);
                            } else {
                                new WindowPage();
                            }
                            
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