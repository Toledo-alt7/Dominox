package br.maua.dominox;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class PainelProfessor extends JFrame {
    private JTable tabelaAlunos;
    private DefaultTableModel modeloTabela;

    public PainelProfessor() {
        setTitle("Dominox - Painel do Professor");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel lblTitulo = new JLabel("Relatório Geral de Alunos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(15, 0, 10, 0));
        add(lblTitulo, BorderLayout.NORTH);

        String[] colunas = {"ID", "Email", "Partidas", "Concluídas", "Média Pontos", "Média Tempo (s)"};
        modeloTabela = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Impede edição
        };
        
        tabelaAlunos = new JTable(modeloTabela);
        tabelaAlunos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tabelaAlunos), BorderLayout.CENTER);

        // Ação de clique duplo para abrir detalhes do aluno
        tabelaAlunos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                if (me.getClickCount() == 2) {
                    int row = tabelaAlunos.getSelectedRow();
                    if (row != -1) {
                        int idAluno = (int) modeloTabela.getValueAt(row, 0);
                        String emailAluno = (String) modeloTabela.getValueAt(row, 1);
                        new PainelAluno(idAluno, emailAluno).setVisible(true);
                    }
                }
            }
        });

        carregarDadosBanco();
    }

    private void carregarDadosBanco() {
        DataBase db = new DataBase();
        List<RelatorioAluno> relatorios = db.getRelatorioProfessor();
        
        for (RelatorioAluno r : relatorios) {
            modeloTabela.addRow(new Object[]{
                r.getIdUsuario(),
                r.getEmail(),
                r.getPartidas(),
                r.getConcluidas(),
                String.format("%.1f", r.getMediaPontuacao()),
                String.format("%.1f", r.getMediaTempo())
            });
        }
    }
}