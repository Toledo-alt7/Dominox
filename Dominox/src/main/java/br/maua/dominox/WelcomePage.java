package br.maua.dominox;

import java.awt.*;
import javax.swing.*;

public class WelcomePage {

    JFrame frame = new JFrame("Menu Principal");
    JLabel welcomeLabel = new JLabel();
    JLabel logoLabel = new JLabel();
    JButton jogarButton = new JButton("Jogar");
    JButton editorButton = new JButton("Modo Editor");
    JButton sairButton = new JButton("Sair");

    public WelcomePage() {
        try {
            // Tenta carregar a imagem do mesmo pacote da classe
            java.net.URL imgURL = WelcomePage.class.getResource("logoDominox.png");
            
            if (imgURL != null) {
                ImageIcon logoIcon = new ImageIcon(imgURL);
                // Redimensiona a imagem para 150x100 pixels
                Image img = logoIcon.getImage().getScaledInstance(150, 100, Image.SCALE_SMOOTH);
                logoLabel.setIcon(new ImageIcon(img));
            } else {
                System.err.println("Erro: Arquivo logo.png não encontrado no pacote!");
                logoLabel.setText("Logo não encontrada");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logoLabel.setBounds(125, 20, 150, 100);
        
        welcomeLabel.setBounds(50, 30, 350, 35);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));

        jogarButton.setBounds(125, 180, 150, 40);
        editorButton.setBounds(125, 240, 150, 40);
        sairButton.setBounds(125, 300, 150, 40);
        
        sairButton.addActionListener(e -> {
            frame.dispose();
        });
        
        frame.add(logoLabel);
        frame.add(welcomeLabel);
        frame.add(jogarButton);
        frame.add(editorButton);
        frame.add(sairButton);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420, 450);
        frame.setLayout(null);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}