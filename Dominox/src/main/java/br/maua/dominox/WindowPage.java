package br.maua.dominox;

import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class WindowPage {
    JFrame frame = new JFrame();
    JLabel label = new JLabel("Hello!!");
    public WindowPage(){
        label.setBounds(0,0,100,50);
        label.setFont(new Font(null, Font.BOLD,25));
        frame.add(label);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(420,420);
        frame.setLayout(null);
        frame.setVisible(true);
    }
}