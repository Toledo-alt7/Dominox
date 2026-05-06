package br.maua.dominox;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.sql.*;

public class LoginPage implements ActionListener {

    JFrame frame = new JFrame("Login - Dominox");
    JButton loginButton = new JButton("Login");
    JButton resetButton = new JButton("Limpar");
    JTextField userIDField = new JTextField();
    JPasswordField userPasswordField = new JPasswordField();
    JLabel userIDLabel = new JLabel("Email:");
    JLabel userPasswordLabel = new JLabel("Senha:");
    JLabel messageLabel = new JLabel();

    public LoginPage() {
        userIDLabel.setBounds(50, 100, 75, 25);
        userPasswordLabel.setBounds(50, 150, 75, 25);
        messageLabel.setBounds(50, 250, 320, 35);
        
        userIDField.setBounds(125, 100, 200, 25);
        userPasswordField.setBounds(125, 150, 200, 25);
        
        loginButton.setBounds(125, 200, 100, 25);
        loginButton.addActionListener(this);
        
        resetButton.setBounds(225, 200, 100, 25);
        resetButton.addActionListener(this);

        frame.add(userIDLabel);
        frame.add(userPasswordLabel);
        frame.add(messageLabel);
        frame.add(userIDField);
        frame.add(userPasswordField);
        frame.add(loginButton);
        frame.add(resetButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 420);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == resetButton) {
            userIDField.setText("");
            userPasswordField.setText("");
        }
        if (e.getSource() == loginButton) {
            validarAcesso();
        }
    }

    private void validarAcesso() {
        String email = userIDField.getText();
        String senha = String.valueOf(userPasswordField.getPassword());
        
        // Query que busca o ID e o Status do usuário
        String sql = "SELECT id_usuario, ativo FROM usuario WHERE email = ? AND senha = ?";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            if (conn == null) {
                messageLabel.setText("Erro: Banco de dados inacessível.");
                return;
            }

            stmt.setString(1, email);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idRecuperado = rs.getInt("id_usuario");
                boolean ativo = rs.getBoolean("ativo");

                if (ativo) {
                    frame.dispose();
                    new WelcomePage();
                } else {
                    messageLabel.setForeground(Color.orange);
                    messageLabel.setText("Esta conta está inativa.");
                }
            } else {
                messageLabel.setForeground(Color.red);
                messageLabel.setText("Email ou senha incorretos.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new LoginPage();
    });
    }
}
