package br.maua.dominox;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

// Classe que configura o painel de login

public class LoginPage implements ActionListener{

	JFrame frame = new JFrame();
	JButton loginButton = new JButton("Login");
	JButton resetButton = new JButton("Cancelar");
    JButton registrateButton = new JButton("Registrar");
	JTextField userIDField = new JTextField();
	JPasswordField userPasswordField = new JPasswordField();
	JLabel userIDLabel = new JLabel("userID:");
	JLabel userPasswordLabel = new JLabel("password:");
	JLabel messageLabel = new JLabel();
	HashMap<String,String> logininfo = new HashMap<String,String>();
    IDandPasswords pp = new IDandPasswords();
    
    
	LoginPage(HashMap<String, String> loginInfoOriginal){

		logininfo = loginInfoOriginal;
		
		userIDLabel.setBounds(50,100,75,25);
		userPasswordLabel.setBounds(50,150,75,25);
		
		messageLabel.setBounds(125,250,250,35);
		messageLabel.setFont(new Font(null,Font.ITALIC,25));
		
                messageLabel.setBounds(125,250,250,35);
		messageLabel.setFont(new Font(null,Font.ITALIC,25));
                
		userIDField.setBounds(125,100,200,25);
		userPasswordField.setBounds(125,150,200,25);
		
		loginButton.setBounds(125,200,100,25);
		loginButton.setFocusable(false);
		loginButton.addActionListener(this);
		
		resetButton.setBounds(225,200,100,25);
		resetButton.setFocusable(false);
		resetButton.addActionListener(this);

        registrateButton.setBounds(125, 200, 100, 25);
        registrateButton.setFocusable(false);
        registrateButton.addActionListener(this);

		frame.setLocationRelativeTo(null);
		
		frame.add(userIDLabel);
		frame.add(userPasswordLabel);
		frame.add(messageLabel);
		frame.add(userIDField);
		frame.add(userPasswordField);
		frame.add(loginButton);
		frame.add(resetButton);
        frame.add(registrateButton);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420,420);
		frame.setLayout(null);
		frame.setVisible(true);

	}	
	public void actionPerformed(ActionEvent e){

		if(e.getSource()==resetButton) {
			userIDField.setText("");
			userPasswordField.setText("");
		}
		
		if(e.getSource()==loginButton) {
			
			String userID = userIDField.getText();
			String password = String.valueOf(userPasswordField.getPassword());

			if(userID.equals("felipe")){ // consertar dps que o banco
			                             // de dados estiver pronto
				pp.ID = 1;
			}
			else{
				pp.ID = 0;
			}

			if(logininfo.containsKey(userID)) {
				if(logininfo.get(userID).equals(password)) {
				    if(pp.ID == 1){
					
					
						messageLabel.setForeground(Color.green);
						messageLabel.setText("Login concluido!");
						frame.dispose();
						WelcomePage welcomePage = new WelcomePage(userID);
				    }
				    else if(pp.ID == 0){
						messageLabel.setForeground(Color.green);
						messageLabel.setText("Login concluido!");
				      	frame.dispose();
				       	PainelAluno pa = new PainelAluno(userID);
					
				    }

				}
				
				else {
					messageLabel.setForeground(Color.red);
					messageLabel.setText("Senha incorreta");

				}
			}
			else {
				messageLabel.setForeground(Color.red);
				messageLabel.setText("Usuário não encontrado");

			}
		}
	} 
}
