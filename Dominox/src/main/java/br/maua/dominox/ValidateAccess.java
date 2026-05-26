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
                            // Salva sessão se checkbox marcado, limpa se não
                            if (loginPage.remeberMeBox.isSelected()){
                                SessionManager.saveSession(email);
                            } 
                            else{
                                SessionManager.clearSession();
                            }
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