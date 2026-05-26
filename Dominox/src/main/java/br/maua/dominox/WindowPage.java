package br.maua.dominox;

import java.awt.*;
import javax.swing.*;



public class WindowPage {
    public JFrame frame = new JFrame("Dominox - Fases");
    public JButton[] faseButtons = new JButton[5];
    public JButton configButton = new JButton("⚙ Configurações");

    // trocar painel dentro do mesmo frame
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel cardPanel = new JPanel(cardLayout);

    private static final String TELA_FASES = "fases";
    private static final String TELA_CONFIG = "config";

    public WindowPage(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setMinimumSize(new Dimension(500, 350));
        frame.setLayout(new BorderLayout());
        frame.setLocationRelativeTo(null);

        cardPanel.add(buildFasesPanel(), TELA_FASES);
        cardPanel.add(buildConfigPanel(), TELA_CONFIG);

        frame.add(cardPanel, BorderLayout.CENTER);
        cardLayout.show(cardPanel, TELA_FASES);
        frame.setVisible(true);
    }
    
    private JPanel buildFasesPanel(){
        JPanel root = new JPanel(new BorderLayout(10, 10));
        JLabel titleLabel = new JLabel("Selecione uma fase", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        root.add(titleLabel, BorderLayout.NORTH);
        JPanel fasesPanel = new JPanel(new GridLayout(1, 5, 10, 0));
        fasesPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        for (int i = 0; i < 5; i++){
            faseButtons[i] = new JButton("<html><center>Fase<br>" + (i + 1) + "</center></html>");
            faseButtons[i].setFont(new Font("Arial", Font.BOLD, 16));
            faseButtons[i].setPreferredSize(new Dimension(90, 90));
            final int fase = i + 1;
            faseButtons[i].addActionListener(e -> iniciarFase(fase));
            fasesPanel.add(faseButtons[i]);
        }
        root.add(fasesPanel, BorderLayout.CENTER);
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        configButton.addActionListener(e -> cardLayout.show(cardPanel, TELA_CONFIG));
        footer.add(configButton);
        root.add(footer, BorderLayout.SOUTH);

        return root;
    }       
    // painel config
    private JPanel buildConfigPanel(){
        JPanel root = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(10, 15, 10, 15);

        //botão voltar
        JButton voltarButton = new JButton("← Voltar");
        voltarButton.addActionListener(e -> cardLayout.show(cardPanel, TELA_FASES));
        c.gridx = 0;
        c.gridy = 9;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        root.add(voltarButton, c);
        JLabel titleLabel = new JLabel("Configurações");
        titleLabel.setFont(new Font("Aria", Font.BOLD, 18));
        c.gridy = 0;
        root.add(titleLabel, c);

        //áduio
        JLabel audioLabel = new JLabel("Áudio");
        audioLabel.setFont(new Font("Arial", Font.BOLD, 14));
        c.gridy = 1;
        root.add(audioLabel, c);

        JCheckBox somCheckBox = new JCheckBox("Desativar som");
        somCheckBox.setSelected(false);
        c.gridy = 2;
        root.add(somCheckBox, c);

        JLabel volumeLabel = new JLabel("100", JLabel.CENTER);
        volumeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        c.gridy = 3;
        c.gridwidth = 2;
        c.weightx = 1.0;
        root.add(volumeLabel, c);
        
        JSlider volumeSlider = new JSlider(0, 100, 100);
        volumeSlider.setMajorTickSpacing(10);
        volumeSlider.setMinorTickSpacing(1);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setSnapToTicks(false);
        volumeSlider.addChangeListener(e -> volumeLabel.setText(String.valueOf(volumeSlider.getValue())));
        c.gridy = 4;
        root.add(volumeSlider, c);

        somCheckBox.addActionListener(e -> {boolean desativado = somCheckBox.isSelected();
            volumeSlider.setEnabled(!desativado);
            volumeLabel.setEnabled(!desativado);
        });

    // separador
    c.gridx = 0;
    c.gridy = 5;
    c.gridwidth = 2;
    root.add(new JSeparator(), c);

    //conta
    JLabel contaLabel = new JLabel("Conta");
    contaLabel.setFont(new Font("Arial", Font.BOLD, 14));
    c.gridy = 6;
    root.add(contaLabel, c);

    JButton sairButton = new JButton("Sair da conta");
    sairButton.setForeground(Color.RED);
    sairButton.addActionListener(e -> sairDaConta());
    c.gridy = 7;
    root.add(sairButton, c);

    return root;
    }

    private void iniciarFase(int numeroFase){
        JOptionPane.showMessageDialog(frame, "Iniciando Fase " +  numeroFase + "...");
    }

    private void sairDaConta(){
        int confirm = JOptionPane.showConfirmDialog(frame, "Deseja sair da conta? Você precisará fazer login novamente", "Sair da conta", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_NO_OPTION){
            SessionManager.clearSession();
            frame.dispose();
            new LoginPage();
        }
    }
}