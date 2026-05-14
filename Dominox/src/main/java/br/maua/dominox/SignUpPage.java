package br.maua.dominox;


import java.awt.*;
import javax.swing.*;

public class SignUpPage {
    public JFrame frame = new JFrame("Dominox - Cadastro");
    public JTextField userField = new JTextField();
    public JPasswordField passField = new JPasswordField();
    public JPasswordField confirmPassField = new JPasswordField();
    public JButton registerButton = new JButton("Confirmar Cadastro");
    public JButton backButton = new JButton("Voltar");
    public JLabel messageLabel = new JLabel("Crie sua conta preenchendo os dados abaixo.");
    
    public SignUpPage() {
        frame.setSize(450, 400);
        frame.setLayout(new GridBagLayout());
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
        addComponents();

        registerButton.addActionListener(e -> {
            DataBase db = new DataBase();
            db.cadastrarUsuario(this); 
        });

        backButton.addActionListener(e -> frame.dispose());

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addComponents() {
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 10, 10, 10);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        messageLabel.setHorizontalAlignment(JLabel.CENTER);
        frame.add(messageLabel, c);
        frame.setMinimumSize(new Dimension(450, 400));

        c.gridwidth = 1;
        c.gridy = 1;
        frame.add(new JLabel("E-mail/Usuário:"), c);
        c.gridx = 1;
        frame.add(userField, c);

        c.gridx = 0;
        c.gridy = 2;
        frame.add(new JLabel("Senha:"), c);
        c.gridx = 1;
        frame.add(passField, c);

        c.gridx = 0;
        c.gridy = 3;
        frame.add(new JLabel("Confirmar Senha:"), c);
        c.gridx = 1;
        frame.add(confirmPassField, c);

        c.gridx = 0;
        c.gridy = 4;
        frame.add(registerButton, c);
        c.gridx = 1;
        frame.add(backButton, c);
    }
}