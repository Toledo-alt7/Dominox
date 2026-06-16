package br.maua.dominox;

import java.awt.*;
import javax.swing.*;

public class LoginPage {

    public JFrame frame = new JFrame("Dominox");
    public JButton loginButton = new JButton("Login");
    public JButton resetButton = new JButton("Cancelar");
    public JButton registerButton = new JButton("Registrar-se");
    public JTextField userIDField = new JTextField();
    public JPasswordField userPasswordField = new JPasswordField();
    public JLabel messageLabel = new JLabel("Não possui uma conta?");
    public JLabel statusLabel = new JLabel("");
    public JCheckBox remeberMeBox = new JCheckBox("Manter login");


    public boolean loginPageStats = true;

    public LoginPage() {
        // verifica sessão salva antes de mostrar a tela 
        String savedUser = SessionManager.getSavedUser();
        if (savedUser != null){
            new WindowPage();
            return;
        }

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 500);
        frame.setMinimumSize(new Dimension(450, 400));
        frame.setLayout(new GridBagLayout());
		frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        addComponents();

        ActionHandler handler = new ActionHandler(this);
        loginButton.addActionListener(handler);
        resetButton.addActionListener(handler);
        registerButton.addActionListener(handler);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addComponents() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        
        c.gridx = 0;
        c.gridy = 0;
        frame.add(new JLabel("Usuário:"), c);

        c.gridx = 1;
        c.gridy = 0;
        c.weightx = 1.0;
        frame.add(userIDField, c);

        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 0;
        frame.add(new JLabel("Senha:"), c);

        c.gridx = 1;
        frame.add(userPasswordField, c);

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        frame.add(remeberMeBox, c);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 5, 0));
        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);
        
        c.gridx = 0;
        c.gridy = 3;
        c.gridwidth = 0;
        frame.add(buttonPanel, c);

        c.gridy = 4;
        statusLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(statusLabel, c);

        c.gridy = 5;
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(messageLabel, c);

        c.gridy = 6;
        frame.add(registerButton, c);
    }
}

