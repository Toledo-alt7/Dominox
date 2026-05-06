package br.maua.dominox;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;
import javax.swing.*;

// Classe que configura o painel de login

public class SignUpPage implements ActionListener{
  
  JFrame frame = new JFrame();
  JButton confirmButton = new JButton("Registrar");
  JButton resetButton = new JButton("Cancelar");
  JTextField userIDField = new JTextField();
  JPasswordField userPasswordField = new JPasswordField();
  JPasswordField confirmPasswordField = new JPasswordField();
  JLabel userIDLabel = new JLabel("userID:");
  JLabel userPasswordLabel = new JLabel("password:");
  JLabel confirmPasswordLabel = new JLabel("Confirmar:");
  JLabel messageLabel = new JLabel();
  HashMap<String,String> logininfo;

  SignUpPage(HashMap<String,String> loginInfoOriginal){
    
    logininfo = loginInfoOriginal;
  
    userIDLabel.setBounds(50,100,75,25);
    userPasswordLabel.setBounds(50,150,75,25);
    confirmPasswordLabel.setBounds(50,200,75,25);

    messageLabel.setBounds(125,250,250,35);
    messageLabel.setFont(new Font(null,Font.ITALIC,25));

    userIDField.setBounds(125,100,200,25);
    userPasswordField.setBounds(125,150,200,25);
    confirmPasswordField.setBounds(125,200,200,25);

    confirmButton.setBounds(125,240,100,25);
    confirmButton.setFocusable(false);
    confirmButton.addActionListener(this);

    resetButton.setBounds(225,240,100,25);
    resetButton.setFocusable(false);
    resetButton.addActionListener(this);

    frame.setLocationRelativeTo(null);

    frame.add(userIDLabel);
    frame.add(userPasswordLabel);
    frame.add(messageLabel);
    frame.add(userIDField);
    frame.add(userPasswordField);
    frame.add(confirmPasswordLabel);
    frame.add(confirmPasswordField);
    frame.add(confirmButton);
    frame.add(resetButton);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(420,500);
    frame.setLayout(null);
    frame.setVisible(true);

  }
  public void actionPerformed(ActionEvent e){

    if(e.getSource() == resetButton) {
      frame.dispose(); //fecha o registro, volta para o login
    }

    if(e.getSource() == confirmButton){
      String userID = userIDField.getText().trim();
      String password = String.valueOf(userPasswordField.getPassword());
      String confirmPassword = String.valueOf(confirmPasswordField.getPassword());
    
    //Validações
      if(userID.isEmpty() || password.isEmpty()){
        messageLabel.setForeground(Color.red);
        messageLabel.setText("Preencha todos os campos");
        return;
    }
      if(logininfo.containsKey(userID)){
        messageLabel.setForeground(Color.red);
        messageLabel.setText("Usuário já existe");
        return;
      }
      if(!password.equals(confirmPassword)){
        messageLabel.setForeground(Color.red);
        messageLabel.setText("Senhas não coincidem");
        return;
      }
    
      // Cadastro bem-sucedido
      logininfo.put(userID, password);
      messageLabel.setForeground(Color.green);
      messageLabel.setText("Cadastro realizado!");
    
      // Fecha após 1 segundo 
      Timer timer = new Timer(1000, event -> frame.dispose());
      timer.setRepeats(false);
      timer.start();

    }

 }
}
