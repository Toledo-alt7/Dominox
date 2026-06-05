package br.maua.dominox;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class PainelAjuda extends JDialog {

    // ── Paleta ────────────────────────────────────────────────
    private static final Color BG_PAINEL    = new Color(18, 60, 35);
    private static final Color BG_TOPICO    = new Color(25, 90, 50);
    private static final Color BG_SUBTOPICO = new Color(30, 110, 60);
    private static final Color BG_DESC      = new Color(20, 75, 42);
    private static final Color VERDE        = new Color(50, 180, 90);
    private static final Color VERDE_ESCURO = new Color(30, 130, 60);
    private static final Color TEXTO        = new Color(220, 255, 230);
    private static final Color TEXTO_DIM    = new Color(160, 220, 180);

    // ── Estrutura de dados ────────────────────────────────────

    /** Um subtópico com título e descrição. */
    static class Subtopico {
        String titulo;
        String descricao;
        Subtopico(String titulo, String descricao) {
            this.titulo = titulo;
            this.descricao = descricao;
        }
    }

    /** Um tópico principal com lista de subtópicos. */
    static class Topico {
        String titulo;
        String descricaoGeral; // descrição do tópico pai
        List<Subtopico> subtopicos = new ArrayList<>();
        Topico(String titulo, String descricaoGeral) {
            this.titulo = titulo;
            this.descricaoGeral = descricaoGeral;
        }
        Topico add(String sub, String desc) {
            subtopicos.add(new Subtopico(sub, desc));
            return this;
        }
    }

    
    // ── Construtor ────────────────────────────────────────────

    public PainelAjuda(JFrame owner, Fase fase) {
        super(owner, fase.getNome(), true);
        setUndecorated(true);
        setSize(520, 580);
        setLocationRelativeTo(owner);

        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_PAINEL);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        root.setOpaque(false);
        root.setBorder(new EmptyBorder(0, 0, 0, 0));

        root.add(buildHeader(), BorderLayout.NORTH);
        root.add(buildContent(fase), BorderLayout.CENTER);

        // Arredondamento da janela
        setBackground(new Color(0, 0, 0, 0));
        getRootPane().setOpaque(false);
        ((JComponent) getContentPane()).setOpaque(false);
        setContentPane(root);
    }

    // ── Header ────────────────────────────────────────────────

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(VERDE_ESCURO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight() + 20, 20, 20);
            }
        };
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(14, 20, 14, 14));

        JLabel titulo = new JLabel("Dicas");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));

        JButton fechar = new JButton("X") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(200, 50, 50));
                g2.fillOval(0, 0, getWidth(), getHeight());
                super.paintComponent(g);
            }
        };
        fechar.setPreferredSize(new Dimension(45, 45));
        fechar.setForeground(Color.WHITE);
        fechar.setFont(new Font("SansSerif", Font.BOLD, 16));
        fechar.setFocusPainted(false);
        fechar.setContentAreaFilled(false);
        fechar.setBorderPainted(false);
        fechar.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        fechar.addActionListener(e -> dispose());

        header.add(titulo, BorderLayout.CENTER);
        header.add(fechar, BorderLayout.EAST);
        return header;
    }

    private List<Topico> getTopicosPorFase(Fase fase) {
    List<Topico> topicos = new ArrayList<>();

    // Tópicos
    switch (fase.getNumeroFase()) {
        case 1: // Ácidos e Bases - 1
            topicos.add(new Topico("Ácido", "")
                .add("Descrição Geral", "Compostos covalentes que têm a capacidade de ionizar em água e formar cargas, liberando o H+ como único cátion.")
                .add("Classificação po número de hidrogênios ionizáveis", "Monoácido - possuem apenas um hidrogênio ionizável.\nDiácido - possuem dois hidrogênios ionizáveis.\nTriácido - possuem três hidrogênios ionizáveis.")
                .add("Classificação por força", "Forte - possuem grau de ionização superior a 50%\nModerado - possuem grau de ionização entre 5% e 50%\nFraco - possuem grau de ionização inferior a 5%.")
                .add("Classificação por presença de Oxigênio", "Hidrácido - Não possui Oxigênio na sua composição.\nOxiácido - Possui Oxigênio na sua composição."));

            topicos.add(new Topico("Base", "")
                .add("Descrição Geral", "Compostos iônicos formados por cátions que liberam OH⁻ em solução.")
                .add("Classificação por número de hidroxilas", "Monobase - Apenas uma hidroxila (OH-)\nDibase - Duas hidroxilas (OH-)\nTribase - Três hidroxilas (OH-)")
                .add("Força", "Forte - possuem grau de dissociação praticamente 100%. São fortes: Bases de metais alcalinos e bases de metais alcalino-terrosos, com exceção de Be(OH)₂ e Mg(OH)₂ \nFraca - possuem grau de dissociação inferior a 5%."));
            break;

        case 2: // Ácidos e Bases - 2
            topicos.add(new Topico("Ácido", "")
                .add("Descrição Geral", "Compostos covalentes que têm a capacidade de ionizar em água e formar cargas, liberando o H+ como único cátion.")
                .add("Classificação po número de hidrogênios ionizáveis", "Monoácido - possuem apenas um hidrogênio ionizável.\nDiácido - possuem dois hidrogênios ionizáveis.\nTriácido - possuem três hidrogênios ionizáveis.\nTetrácido - possuem quatro hidrogênios ionizáveis.")
                .add("Classificação por força", "Forte - possuem grau de ionização superior a 50%\nModerado - possuem grau de ionização entre 5% e 50%\nFraco - possuem grau de ionização inferior a 5%.")
                .add("Classificação por presença de Oxigênio", "Hidrácido - Não possui Oxigênio na sua composição.\nOxiácido - Possui Oxigênio na sua composição."));

            topicos.add(new Topico("Base", "")
                .add("Descrição Geral", "Compostos iônicos formados por cátions que liberam OH⁻ em solução.")
                .add("Classificação por número de hidroxilas", "Monobase - Apenas uma hidroxila (OH-)\nDibase - Duas hidroxilas (OH-)\nTribase - Três hidroxilas (OH-)\nTetrabase - Quatro Hidroxilas (OH-)")
                .add("Força", "Forte - possuem grau de dissociação praticamente 100%. São fortes: Bases de metais alcalinos e bases de metais alcalino-terrosos, com exceção de Be(OH)₂ e Mg(OH)₂ \nFraca - possuem grau de dissociação inferior a 5%."));
            break;

        case 3: // Sais
            topicos.add(new Topico("Sais", "")
                .add("Classificação por caráter", "Neutro - Quando são dissolvidos em água não alteram o pH.\nÁcido - Quando são dissolvidos em água fazem o pH da solução ficar menor que 7.\nBásico - Quando são dissolvidos em água fazem o pH da solução ficar maior que 7.")
                .add("Classificação por solubilidade", "Solúvel - Nitratos, Cloratos, Acetatos (Excluindo Acetato de Prata), Cloretos, Brometos, Iodetos e Sulfatos\nInsolúvel - Sulfetos, Carbonatos e Fosfatos")
                .add("Classificação por presença de Oxigênio", "Halóides - Sem presença de Oxigênio.\nOxissais - Com presença de Oxigênio"));
            break;

        case 4: // Óxidos
            topicos.add(new Topico("Óxidos", "")
                .add("Classificação pelo caráter", "Ácido - Em solução aquosa reagem com a água e formam ácidos.\nBásico - Em solução aquosa alteram o pH para maior que 7.\nAnfótero - Podem se comportar como ácidos ou bases.\nNeutro - Alguns óxidos que não reagem com a água.")
                .add("Classificação pelas ligações", "Iônicos - Combinação do oxigênio com metais.\nCovalentes - Combinação do oxigênio com elementos não metálicos."));
            break;

        case 5: // Geral
            topicos.add(new Topico("Ácido", "")
                .add("Descrição Geral", "Compostos covalentes que têm a capacidade de ionizar em água e formar cargas, liberando o H+ como único cátion.")
                .add("Classificação por força", "Forte - possuem grau de ionização superior a 50%\nModerado - possuem grau de ionização entre 5% e 50%\nFraco - possuem grau de ionização inferior a 5%."));
            topicos.add(new Topico("Base", "")
                .add("Descrição Geral", "Compostos iônicos formados por cátions que liberam OH⁻ em solução.")
                .add("Força", "Forte - possuem grau de dissociação praticamente 100%. São fortes: Bases de metais alcalinos e bases de metais alcalino-terrosos, com exceção de Be(OH)₂ e Mg(OH)₂ \nFraca - possuem grau de dissociação inferior a 5%."));
            topicos.add(new Topico("Sais", "")
                .add("Descrição Geral", "Compostos iônicos que apresentam, no mínimo, um cátion diferente de H+ e um ânion diferente de OH-.")
                .add("Classificação por solubilidade", "Solúvel - Nitratos, Cloratos, Acetatos (Excluindo Acetato de Prata), Cloretos, Brometos, Iodetos e Sulfatos\nInsolúvel - Sulfetos, Carbonatos e Fosfatos"));
            topicos.add(new Topico("Óxidos", "")
                .add("Descrição Geral","São compostos binários (iônicos ou covalentes), que têm dois elementos. Possuem oxigênio na sua composição, sendo ele o seu elemento mais eletronegativo.")
                .add("Classificação pelas ligações", "Iônicos - Combinação do oxigênio com metais.\nCovalentes - Combinação do oxigênio com elementos não metálicos."));
            break;
    }

    return topicos;
}

    // ── Conteúdo com tópicos expansíveis ─────────────────────

    private JScrollPane buildContent(Fase fase) {
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(BG_PAINEL);
        content.setBorder(new EmptyBorder(10, 12, 12, 12));

        List<Topico> topicosDaFase = getTopicosPorFase(fase);

        for (Topico topico : topicosDaFase) {
            content.add(buildTopicoPanel(topico));
            content.add(Box.createVerticalStrut(6));
        }

        JScrollPane scroll = new JScrollPane(content);
        scroll.setOpaque(false);
        scroll.getViewport().setBackground(BG_PAINEL);
        scroll.setBorder(null);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll.getVerticalScrollBar().setUnitIncrement(12);
        return scroll;
    }

    /** Monta o bloco de um tópico principal com seus subtópicos colapsáveis. */
    private JPanel buildTopicoPanel(Topico topico) {
        // Container externo do tópico
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        // ── Header do tópico (clicável) ──
        JPanel headerTopico = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_TOPICO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            }
        };
        headerTopico.setOpaque(false);
        headerTopico.setBorder(new EmptyBorder(10, 14, 10, 14));
        headerTopico.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        headerTopico.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel setaTopico = new JLabel("▶");
        setaTopico.setForeground(VERDE);
        setaTopico.setFont(new Font("SansSerif", Font.BOLD, 12));

        JLabel tituloTopico = new JLabel(topico.titulo);
        tituloTopico.setForeground(Color.WHITE);
        tituloTopico.setFont(new Font("SansSerif", Font.BOLD, 14));
        tituloTopico.setBorder(new EmptyBorder(0, 8, 0, 0));

        headerTopico.add(setaTopico,   BorderLayout.WEST);
        headerTopico.add(tituloTopico, BorderLayout.CENTER);

        // ── Corpo do tópico (descrição geral + subtópicos) ──
        JPanel corpoTopico = new JPanel();
        corpoTopico.setLayout(new BoxLayout(corpoTopico, BoxLayout.Y_AXIS));
        corpoTopico.setOpaque(false);
        corpoTopico.setBorder(new EmptyBorder(4, 8, 4, 0));
        corpoTopico.setVisible(false); // começa colapsado

        // Descrição geral do tópico
        JTextArea descGeral = makeDescArea(topico.descricaoGeral, TEXTO_DIM);
        descGeral.setBorder(new EmptyBorder(6, 14, 8, 6));
        corpoTopico.add(descGeral);

        // Subtópicos
        for (Subtopico sub : topico.subtopicos) {
            corpoTopico.add(buildSubtopicoPanel(sub));
            corpoTopico.add(Box.createVerticalStrut(4));
        }

        // ── Toggle ao clicar no header ──
        MouseAdapter toggle = new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                boolean aberto = corpoTopico.isVisible();
                corpoTopico.setVisible(!aberto);
                setaTopico.setText(aberto ? "▶" : "▼");
                container.revalidate();
                container.repaint();
            }
            @Override public void mouseEntered(MouseEvent e) {
                headerTopico.setBackground(BG_TOPICO.brighter());
                headerTopico.repaint();
            }
            @Override public void mouseExited(MouseEvent e) {
                headerTopico.setBackground(BG_TOPICO);
                headerTopico.repaint();
            }
        };
        headerTopico.addMouseListener(toggle);

        container.add(headerTopico);
        container.add(corpoTopico);
        return container;
    }

    /** Monta o bloco de um subtópico com sua descrição colapsável. */
    private JPanel buildSubtopicoPanel(Subtopico sub) {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setOpaque(false);
        container.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Header do subtópico
        JPanel headerSub = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(BG_SUBTOPICO);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            }
        };
        headerSub.setOpaque(false);
        headerSub.setBorder(new EmptyBorder(7, 14, 7, 14));
        headerSub.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        headerSub.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel setaSub = new JLabel("›");
        setaSub.setForeground(VERDE);
        setaSub.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel tituloSub = new JLabel(sub.titulo);
        tituloSub.setForeground(TEXTO);
        tituloSub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tituloSub.setBorder(new EmptyBorder(0, 8, 0, 0));

        headerSub.add(setaSub,   BorderLayout.WEST);
        headerSub.add(tituloSub, BorderLayout.CENTER);

        // Descrição do subtópico
        JTextArea descSub = makeDescArea(sub.descricao, TEXTO_DIM);
        descSub.setBorder(new CompoundBorder(
            new EmptyBorder(0, 14, 6, 6),
            new EmptyBorder(6, 10, 6, 6)
        ));
        JPanel descPanel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setColor(BG_DESC);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
            }
        };
        descPanel.setOpaque(false);
        descPanel.setBorder(new EmptyBorder(0, 14, 0, 0));
        descPanel.add(descSub, BorderLayout.CENTER);
        descPanel.setVisible(false);

        // Toggle
        headerSub.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                boolean aberto = descPanel.isVisible();
                descPanel.setVisible(!aberto);
                setaSub.setText(aberto ? "›" : "⌄");
                container.revalidate();
                container.repaint();
            }
        });

        container.add(headerSub);
        container.add(descPanel);
        return container;
    }

    private JTextArea makeDescArea(String text, Color cor) {
        JTextArea area = new JTextArea(text);
        area.setForeground(cor);
        area.setFont(new Font("SansSerif", Font.PLAIN, 12));
        area.setEditable(false);
        area.setFocusable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setOpaque(false);
        area.setBackground(new Color(0, 0, 0, 0));
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        area.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return area;
    }

    // ── Método estático para abrir o painel ───────────────────

    public static void mostrar(JFrame owner, Fase fase) {
        PainelAjuda painel = new PainelAjuda(owner, fase);
        painel.setVisible(true);
    }
}