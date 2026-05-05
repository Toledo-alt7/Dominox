package br.maua.dominox;

import java.awt.*;
import javax.swing.*;

// Classe que configura a janela que abre após o login


public class WelcomePage{

	JFrame frame = new JFrame();
	JLabel welcomeLabel = new JLabel("Hello!");
    JButton jogar = new JButton("Botão");
	JButton modoEditor = new JButton("Editar");
	JButton sair = new JButton("Sair");
	WelcomePage(String userID) {
		
		welcomeLabel.setBounds(0,0,200,35);
		welcomeLabel.setFont(new Font(null,Font.PLAIN,25));
		welcomeLabel.setText("Olá "+userID);
		
		frame.add(welcomeLabel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420, 420);
		frame.setLayout(null);
		frame.setVisible(true);

	}
} 
