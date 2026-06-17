package br.maua.dominox;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PainelAluno extends JDialog {
    
    public PainelAluno(int idUsuario, String email) {
        setTitle("Histórico Detalhado: " + email);
        setSize(500, 350);
        setModal(true); // Bloqueia a tela do professor enquanto esta estiver aberta
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Histórico do Aluno: " + email, SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(lblTitulo, BorderLayout.NORTH);

        
        String[] colunas = {"Fase", "Pontos", "Tempo", "Acertos", "Erros"};
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable tabelaDetalhes = new JTable(modelo);
        tabelaDetalhes.setFont(new Font("Monospaced", Font.PLAIN, 14));
        tabelaDetalhes.setRowHeight(25);
        
        // Centralizando os dados nas células para ficar com cara de quadro
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for(int i = 0; i < tabelaDetalhes.getColumnCount(); i++){
            tabelaDetalhes.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        add(new JScrollPane(tabelaDetalhes), BorderLayout.CENTER);

        // Preenche a tabela acessando o DataBase
        DataBase db = new DataBase();
        List<Object[]> historico = db.getHistoricoDetalhadoAluno(idUsuario);
        
        for (Object[] linha : historico) {
            modelo.addRow(linha);
        }
        
        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        JPanel pnlSul = new JPanel();
        pnlSul.add(btnFechar);
        add(pnlSul, BorderLayout.SOUTH);
    }
}
