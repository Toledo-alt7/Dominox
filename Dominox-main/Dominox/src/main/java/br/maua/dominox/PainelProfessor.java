package br.maua.dominox;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PainelProfessor extends JFrame {

    private JTable tabelaMetricas;
    private DefaultTableModel modeloTabela;
    private JLabel lblMaiorPontuacao;
    private JLabel lblPontuacaoMedia;
    private JLabel lblTempoMedio;
    private JLabel lblMediaAcertos;
    private JLabel lblMediaErros;

    public PainelProfessor() {
        setTitle("Dominox - Painel de Controle do Professor");
        setSize(850, 520);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(15, 15));

        // titulo superior
        JLabel lblTitulo = new JLabel("Desempenho Geral dos Alunos", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        add(lblTitulo, BorderLayout.NORTH);

        // grid de dados (tabela central)
        String[] colunas = {"Número da Fase", "Pontos Obtidos", "Tempo Gasto", "Acertos", "Erros" };
        modeloTabela = new DefaultTableModel(colunas, 0);
        tabelaMetricas = new JTable(modeloTabela);
        add(new JScrollPane(tabelaMetricas), BorderLayout.CENTER);

        // painel inferior contendo os calculos requisitados
        JPanel painelCards = new JPanel(new GridLayout(1, 5, 10, 0));
        painelCards.setBorder(BorderFactory.createEmptyBorder(0, 15, 15, 15));

        lblMaiorPontuacao = new JLabel("Maior Pontuação: 0", SwingConstants.CENTER);
        lblPontuacaoMedia = new JLabel("Pontuação Média: 0.0", SwingConstants.CENTER);
        lblTempoMedio = new JLabel("Tempo Médio na Fase: 00:00", SwingConstants.CENTER);
        lblMediaAcertos = new JLabel("Média Acertos: 0.0", SwingConstants.CENTER);
        lblMediaErros = new JLabel("Média Erros: 0.0", SwingConstants.CENTER);

        // estilização visual dos cards de dados
        for (JLabel card : new JLabel[]{lblMaiorPontuacao, lblPontuacaoMedia, lblTempoMedio}) {
            card.setFont(new Font("Arial", Font.BOLD, 14));
            card.setOpaque(true);
            card.setBackground(new Color(245, 245, 245));
            card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            painelCards.add(card);
        }

        // agrupador Sul
        JPanel painelInferiorCompleto = new JPanel(new BorderLayout(10, 10));
        painelInferiorCompleto.add(painelCards, BorderLayout.CENTER);

        JButton btnVoltar = new JButton("Voltar ao Menu");
        btnVoltar.addActionListener(e -> dispose());
        JPanel containerBotao = new JPanel();
        containerBotao.add(btnVoltar);
        painelInferiorCompleto.add(containerBotao, BorderLayout.SOUTH);

        add(painelInferiorCompleto, BorderLayout.SOUTH);

        // processa as contas baseadas nas GameEngines na memória
        gerarRelatorioEstatistico();
    }

    private void gerarRelatorioEstatistico() {
        List<GameEngine> historico = FaseRegistry.getHistoricoPartidas();

        if (historico.isEmpty()) {
            return; 
        }

        int maiorPontos = Integer.MIN_VALUE;
        int somaPontos = 0;
        int somaTempo = 0;
        int somaAcertos = 0;
        int somaErros = 0;

        for (GameEngine engine : historico) {
            Fase fase = engine.getFaseAtual();
            int pontos = engine.getPoints(); // 
            int tempo = engine.getTempoSegundos();
            int acertos = engine.getJogadasCertas();
            int erros = engine.getJogadasErradas();

            // resgata o número da fase de dentro do objeto DynamicFase
            int numFase = (fase != null) ? fase.getNumeroFase() : 0;

            // insere os valores na tabela visual
            String tempoFormatado = String.format("%02d:%02d", tempo / 60, tempo % 60);
            modeloTabela.addRow(new Object[]{
                "Fase " + numFase, pontos, tempoFormatado, acertos, erros
            });

            // maior pontuação encontrada
            if (pontos > maiorPontos) {
                maiorPontos = pontos;
            }

            somaPontos += pontos;
            somaTempo += tempo;
            somaAcertos += acertos;
            somaErros += erros;
        }

        // calculo das medias
        double mediaPontos = (double) somaPontos / historico.size();
        int tempoMedioSegundos = somaTempo / historico.size();
        double mediaAcertos = (double) somaAcertos / historico.size();
        double mediaErros = (double) somaErros / historico.size();

        // atualiza os labels com os resultados calculados
        lblMaiorPontuacao.setText("Maior Pontuação: " + maiorPontos);
        lblPontuacaoMedia.setText(String.format("Pontuação Média: %.1f", mediaPontos));
        lblTempoMedio.setText(String.format("Tempo Médio: %02d:%02d", tempoMedioSegundos / 60, tempoMedioSegundos % 60));
        lblMediaAcertos.setText(String.format("Média Acertos: %.1f", mediaAcertos));
        lblMediaErros.setText(String.format("Média Erros: %.1f", mediaErros));
    }
}