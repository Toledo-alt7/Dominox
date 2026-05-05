package br.maua.dominox;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
public class PainelAluno implements ActionListener{
    JFrame frame = new JFrame();
	JLabel welcomeLabel = new JLabel("Dominox");
    JButton playButton = new JButton("Jogar");
	JButton modoEditor = new JButton("Editar");
	JButton sair = new JButton("Sair");
	PainelAluno(String userID) {
		
		welcomeLabel.setBounds(150,0,200,35);
		welcomeLabel.setFont(new Font(null,Font.PLAIN,25));

		playButton.setBounds(135, 100, 150, 50);
		playButton.setFocusable(false);
		//playButton.setaddActionListener(this);

		

		frame.setLocationRelativeTo(null);
		
		
		frame.add(playButton);
		frame.add(welcomeLabel);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(420, 420);
		frame.setResizable(true);
		frame.setLayout(null);
		frame.setVisible(true);

		
    }
	public void actionPerformed(ActionEvent e){

	}
}
