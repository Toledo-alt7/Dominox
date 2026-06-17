package br.maua.dominox;

import java.awt.*;
import javax.swing.*;



public class WindowPage {
    public JFrame frame = new JFrame("Dominox - Fases");
    public JButton[] faseButtons = new JButton[5];

    public WindowPage() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setMinimumSize(new Dimension(500, 350));
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        // Como não há mais painel de configurações, adicionamos o painel de fases diretamente
        frame.add(buildFasesPanel(), BorderLayout.CENTER);
        
        frame.setVisible(true);
    }
    
    private JPanel buildFasesPanel() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        
        JLabel titleLabel = new JLabel("Selecione uma fase", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        root.add(titleLabel, BorderLayout.NORTH);
        
        JPanel fasesPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        fasesPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        for (int i = 0; i < 5; i++) {
            faseButtons[i] = new JButton("<html><center>Fase<br>" + (i + 1) + "</center></html>");
            faseButtons[i].setFont(new Font("Arial", Font.BOLD, 16));
            faseButtons[i].setPreferredSize(new Dimension(90, 90));
            final int fase = i + 1;
            faseButtons[i].addActionListener(e -> iniciarFase(fase));
            fasesPanel.add(faseButtons[i]);
        }
        root.add(fasesPanel, BorderLayout.CENTER);
        
        // Rodapé agora contém apenas o botão "Sair da conta"
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        JButton sairButton = new JButton("Sair da conta");
        sairButton.setForeground(Color.RED); // Mantendo a cor vermelha original
        sairButton.addActionListener(e -> sairDaConta());
        
        footer.add(sairButton);
        root.add(footer, BorderLayout.SOUTH);

        return root;
    }       

    private void iniciarFase(int numeroFase) {
        Fase faseAtual = FaseRegistry.getFase(numeroFase);
        new DominoxJogo(faseAtual).setVisible(true);
        frame.dispose();
    }

    private void sairDaConta() {
        int confirm = JOptionPane.showConfirmDialog(frame, "Deseja sair da conta? Você precisará fazer login novamente", "Sair da conta", JOptionPane.YES_NO_OPTION);
        
        // Dica de correção lógica: O correto para checar se o usuário clicou em "Sim" é JOptionPane.YES_OPTION.
        if (confirm == JOptionPane.YES_OPTION) {
            SessionManager.clearSession();
            frame.dispose();
            new LoginPage();
        }
    }
}