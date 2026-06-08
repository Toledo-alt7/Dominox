package br.maua.dominox;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

public class DominoxJogo extends JFrame {

    private static final Color RED       = new Color(190, 30, 30);
    private static final Color RED_DARK  = new Color(140, 18, 18);
    private JLabel          lblErros;
    private BufferedImage   bgImagem;
    private GameEngine      engine;
    private Domino          dominoSelecionado = null;
    private Fase            faseAtual;

    private JPanel          painelTabuleiro;
    private JScrollPane     scrollTabuleiro;

    private JPanel playerPecasPainel;

    private JLabel bot1Label, bot2Label, bot3Label;

    private JButton btnEsquerdo, btnDireito, btnPassar;

    public DominoxJogo(Fase faseAtual) {
        this.faseAtual = faseAtual;
        try { bgImagem = ImageIO.read(new File("src\\main\\java\\br\\maua\\dominox\\background.png")); }
        catch (Exception e) { bgImagem = null; }
        engine = new GameEngine(faseAtual);
        iniciarUI();
        reiniciar();
    }

    // ═══════════════════════════════ UI ═══════════════════════════════

    private void iniciarUI() {
        setTitle("DomiNox");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Root: paints PNG background
        JPanel root = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                if (bgImagem != null)
                    g.drawImage(bgImagem, 0, 0, getWidth(), getHeight(), this);
                else {
                    g.setColor(new Color(0, 110, 120));
                    g.fillRect(0, 0, getWidth(), getHeight());
                }
            }
        };
        root.setOpaque(true);

        // Barra De Cima
        root.add(construirBarraCima(), BorderLayout.NORTH);

        // Área do jogo
        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.add(construirBotEsquerda(),   BorderLayout.WEST);
        main.add(construirCenterArea(), BorderLayout.CENTER);
        main.add(construirBotRight(),  BorderLayout.EAST);
        root.add(main, BorderLayout.CENTER);

        // Embaixo
        root.add(construirEmbaixo(), BorderLayout.SOUTH);

        setContentPane(root);
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 600));
        setLocationRelativeTo(null);
    }

    // Barra de CIma

    private JPanel construirBarraCima() {
    JPanel bar = new JPanel(new BorderLayout());
    bar.setOpaque(false);
    bar.setBorder(new EmptyBorder(12, 16, 6, 16));

    JButton btnVoltar = fazerBtnCircular("<", 90);
    btnVoltar.addActionListener(e -> newJogo());

    // Novo label de erros
    lblErros = new JLabel("Pontos: 0");
    lblErros.setForeground(Color.WHITE);
    lblErros.setFont(new Font("SansSerif", Font.BOLD, 20));

    bar.add(wrap(btnVoltar, FlowLayout.LEFT), BorderLayout.WEST);
    bar.add(lblErros, BorderLayout.EAST); // Coloca no canto direito da barra
    return bar;
    }

    // BOT ESQUERDA (Bot 1, vertical)

    private JPanel construirBotEsquerda() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.setBorder(new EmptyBorder(8, 14, 8, 6));
        outer.setPreferredSize(new Dimension(110, 0));

        JPanel card = fazerCartaoBot();
        card.setLayout(new GridBagLayout());

        bot1Label = new JLabel("<html><center>Bot 1<br>7 peças</center></html>", SwingConstants.CENTER);
        bot1Label.setForeground(Color.WHITE);
        bot1Label.setFont(new Font("SansSerif", Font.BOLD, 13));

        card.add(bot1Label);
        outer.add(card, BorderLayout.CENTER);
        return outer;
    }

    // BOT DIREITA (Bot 3, vertical)

    private JPanel construirBotRight() {
        JPanel outer = new JPanel(new BorderLayout());
        outer.setOpaque(false);
        outer.setBorder(new EmptyBorder(8, 6, 8, 14));
        outer.setPreferredSize(new Dimension(110, 0));

        JPanel card = fazerCartaoBot();
        card.setLayout(new GridBagLayout());

        bot3Label = new JLabel("<html><center>Bot 3<br>7 peças</center></html>", SwingConstants.CENTER);
        bot3Label.setForeground(Color.WHITE);
        bot3Label.setFont(new Font("SansSerif", Font.BOLD, 13));

        card.add(bot3Label);
        outer.add(card, BorderLayout.CENTER);
        return outer;
    }

    // ── CENTRO: Bot 2 (Cima) + tabuleiro ─────────────────────────────────────────

    private JPanel construirCenterArea() {
        JPanel center = new JPanel(new BorderLayout(0, 6));
        center.setOpaque(false);
        center.setBorder(new EmptyBorder(4, 0, 0, 0));

        // Bot 2 at top center (horizontal strip)
        JPanel bot2Outer = new JPanel(new BorderLayout());
        bot2Outer.setOpaque(false);
        bot2Outer.setBorder(new EmptyBorder(0, 0, 4, 0));

        JPanel bot2Card = fazerCartaoBot();
        bot2Card.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 6));

        bot2Label = new JLabel("Bot 2 — 7 peças", SwingConstants.CENTER);
        bot2Label.setForeground(Color.WHITE);
        bot2Label.setFont(new Font("SansSerif", Font.BOLD, 13));

        bot2Card.add(bot2Label);
        bot2Outer.add(bot2Card, BorderLayout.CENTER);

        painelTabuleiro = new TabuleiroPanel();

        scrollTabuleiro = new JScrollPane(painelTabuleiro);
        scrollTabuleiro.setOpaque(false);
        scrollTabuleiro.getViewport().setOpaque(false);
        scrollTabuleiro.setBorder(null);
        scrollTabuleiro.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollTabuleiro.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollTabuleiro.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 4));

        center.add(bot2Outer,   BorderLayout.NORTH);
        center.add(scrollTabuleiro, BorderLayout.CENTER);
        return center;
    }

    // embaixo: status + Pecas + buttons
    // ── Embaixo: peças + botões ───────────────────────────────

    private JPanel construirEmbaixo() {
        JPanel embaixo = new JPanel(new BorderLayout(0, 4));
        embaixo.setOpaque(false);
        embaixo.setBorder(new EmptyBorder(2, 14, 10, 14));

        JPanel pecasOuter = new JPanel(new BorderLayout());
        pecasOuter.setOpaque(false);
        pecasOuter.setBorder(new EmptyBorder(4, 0, 30, 0));

        JPanel esquerdaSpacer = new JPanel(); esquerdaSpacer.setOpaque(false);
        JPanel rightSpacer    = new JPanel(); rightSpacer.setOpaque(false);
        esquerdaSpacer.setPreferredSize(new Dimension(300, 0));
        rightSpacer   .setPreferredSize(new Dimension(300, 0));

        JPanel pecasArea = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(RED);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 50, 50);
            }
        };
        pecasArea.setOpaque(false);
        pecasArea.setBorder(new EmptyBorder(10, 12, 10, 12));

        playerPecasPainel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 3));
        playerPecasPainel.setOpaque(false);
        pecasArea.add(playerPecasPainel, BorderLayout.CENTER);

        pecasOuter.add(esquerdaSpacer, BorderLayout.WEST);
        pecasOuter.add(pecasArea,      BorderLayout.CENTER);
        pecasOuter.add(rightSpacer,    BorderLayout.EAST);

        JPanel btnRow = new JPanel(new BorderLayout());
        btnRow.setOpaque(false);
        btnRow.setBorder(new EmptyBorder(4, 0, 0, 0));

        JPanel btnsEsquerda = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        btnsEsquerda.setOpaque(false);
        btnEsquerdo = fazerBtnAcao("← Esquerda");
        btnDireito  = fazerBtnAcao("Direita →");
        btnPassar   = fazerBtnAcao("Passar ⏭");
        btnEsquerdo.addActionListener(e -> doPlay(true));
        btnDireito .addActionListener(e -> doPlay(false));
        btnPassar  .addActionListener(e -> doPass());
        btnsEsquerda.add(btnEsquerdo);
        btnsEsquerda.add(btnDireito);
        btnsEsquerda.add(btnPassar);

        // Botão de ajuda verde no canto direito
        JPanel btnsDireita = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnsDireita.setOpaque(false);
        btnsDireita.add(fazerBtnAjuda());

        btnRow.add(btnsEsquerda, BorderLayout.CENTER);
        btnRow.add(btnsDireita,  BorderLayout.EAST);

        embaixo.add(pecasOuter, BorderLayout.CENTER);
        embaixo.add(btnRow,     BorderLayout.SOUTH);
        return embaixo;
    }
    

    // ── HELPERS ─────────────────────────────────────────────────────────────

    private JPanel fazerCartaoBot() {
        JPanel p = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 0, 0, 50));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            }
        };
        p.setOpaque(false);
        p.setBorder(new EmptyBorder(6, 6, 6, 6));
        return p;
    }

    private JPanel wrap(Component c, int align) {
        JPanel p = new JPanel(new FlowLayout(align, 0, 0));
        p.setOpaque(false);
        p.add(c);
        return p;
    }

    private JButton fazerBtnCircular(String text, int size) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 150, 165, 160));
                g2.fillOval(0, 0, getWidth(), getHeight());
                int pad = 5;
                g2.setColor(RED_DARK);
                g2.fillOval(pad, pad, getWidth()-pad*2, getHeight()-pad*2);
                g2.setColor(RED);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawOval(pad, pad, getWidth()-pad*2, getHeight()-pad*2);
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(size, size));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 60));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private JButton fazerBtnAcao(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(isEnabled() ? getBackground() : getBackground().darker());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        btn.setBackground(RED_DARK);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setBorder(new EmptyBorder(7, 18, 7, 18));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(RED);      btn.repaint(); }
            public void mouseExited (MouseEvent e) { btn.setBackground(RED_DARK); btn.repaint(); }
        });
        return btn;
    }

    private JButton fazerBtnAjuda() {
        JButton btn = new JButton("?") {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(20, 130, 60, 180));
                g2.fillOval(0, 0, getWidth(), getHeight());
                int pad = 5;
                g2.setColor(new Color(30, 160, 75));
                g2.fillOval(pad, pad, getWidth()-pad*2, getHeight()-pad*2);
                g2.setColor(new Color(50, 200, 90));
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawOval(pad, pad, getWidth()-pad*2, getHeight()-pad*2);
                super.paintComponent(g);
            }
        };
        btn.setPreferredSize(new Dimension(55, 55));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("SansSerif", Font.BOLD, 22));
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> PainelAjuda.mostrar(this, faseAtual));
        return btn;
    }


    // ═══════════════════════════════ reiniciar ════════════════════════════════

    private void reiniciar() {
        reiniciarBoard();
        reiniciarBotPecas();
        reiniciarPlayerPecas();
        reiniciarButtons();
    }

    private void reiniciarBoard() {
        painelTabuleiro.removeAll();
        List<Domino> board = engine.getBoard();
        if (board.isEmpty()) {
            JLabel lbl = new JLabel("Mesa vazia — jogue a primeira peça");
            lbl.setForeground(new Color(255, 255, 255, 160));
            lbl.setFont(new Font("SansSerif", Font.ITALIC, 14));
            painelTabuleiro.add(lbl);
        } else {
            for (Domino d : board)
                painelTabuleiro.add(new DominoPanel(d.getLeft(), d.getRight()));
        }
        painelTabuleiro.revalidate();
        painelTabuleiro.repaint();
        SwingUtilities.invokeLater(() -> {
            JScrollBar sb = scrollTabuleiro.getHorizontalScrollBar();
            sb.setValue(sb.getMaximum());
        });
    }

    private void reiniciarBotPecas() {
        List<Player> players = engine.getPlayers();

        // Bot 1 — LEFT
        Player bot1 = players.get(1);
        boolean active1 = engine.getCurrentPlayerIndex() == 1;
        bot1Label.setText("<html><center>Bot 1<br>" + bot1.getPecas().size() + " peças</center></html>");
        bot1Label.setForeground(active1 ? new Color(255, 230, 80) : Color.WHITE);

        // Bot 2 — TOP
        Player bot2 = players.get(2);
        boolean active2 = engine.getCurrentPlayerIndex() == 2;
        bot2Label.setText("Bot 2 — " + bot2.getPecas().size() + " peças");
        bot2Label.setForeground(active2 ? new Color(255, 230, 80) : Color.WHITE);

        // Bot 3 — RIGHT
        Player bot3 = players.get(3);
        boolean active3 = engine.getCurrentPlayerIndex() == 3;
        bot3Label.setText("<html><center>Bot 3<br>" + bot3.getPecas().size() + " peças</center></html>");
        bot3Label.setForeground(active3 ? new Color(255, 230, 80) : Color.WHITE);
    }

    private void reiniciarPlayerPecas() {
        playerPecasPainel.removeAll();
        List<Domino> Pecas = engine.getHumanPlayer().getPecas();
        boolean myTurn = engine.isHumanTurn();
        for (Domino d : Pecas) {
            DominoPanel dp = new DominoPanel(d.getLeft(), d.getRight());
            boolean sel = (d == dominoSelecionado);
            boolean playable = myTurn && !engine.isBoardEmpty() &&
                (faseAtual.dominoEncaixa(d, engine.getLeftEnd()) || faseAtual.dominoEncaixa(d, engine.getRightEnd())); 
            dp.setSelected(sel);
            dp.setPlayable(!sel && playable);
            dp.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            final Domino ref = d;
            dp.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (!engine.isHumanTurn()) return;
                    dominoSelecionado = (dominoSelecionado == ref) ? null : ref;
                    reiniciarPlayerPecas(); reiniciarButtons();
                }
            });
            playerPecasPainel.add(dp);
        }
        playerPecasPainel.revalidate(); playerPecasPainel.repaint();
    }

    private void reiniciarButtons() {
        boolean myTurn = engine.isHumanTurn() && !engine.isGameOver();
        boolean hasSel = dominoSelecionado != null;
        btnEsquerdo .setEnabled(myTurn && hasSel);
        btnDireito.setEnabled(myTurn && hasSel);
        btnPassar .setEnabled(myTurn);
    }

    // ═══════════════════════════════ ACTIONS ════════════════════════════════

    private void doPlay(boolean toLeft) {
        if (dominoSelecionado == null) { showMsg("Selecione uma peça primeiro!"); return; }
        
        boolean ok = engine.humanPlay(dominoSelecionado, toLeft);
        
        // Atualiza o Label independente de ser acerto ou erro
        lblErros.setText("Pontos: " + engine.getPoints());
        
        // Feedback visual opcional: mudar cor se a pontuação estiver negativa
        if(engine.getPoints() < 0) lblErros.setForeground(Color.RED);
        else lblErros.setForeground(Color.WHITE);

        if (!ok) { 
            showMsg(engine.getStatusMessage()); 
            return; 
        }
        
        dominoSelecionado = null;
        reiniciar(); 
        checkJogoOver();
        if (!engine.isGameOver() && !engine.isHumanTurn()) {
            scheduleBotTurns();
        }
    }

    private void doPass() {
        boolean ok = engine.humanPass();
        if (!ok) { showMsg(engine.getStatusMessage()); return; }
        dominoSelecionado = null;
        reiniciar(); checkJogoOver();
        if (!engine.isGameOver()) scheduleBotTurns();
    }

    private void scheduleBotTurns() {
        if (engine.isHumanTurn() || engine.isGameOver()) return;
        Timer t = new Timer(900, e -> {
            engine.botPlay(); reiniciar(); checkJogoOver();
            if (!engine.isGameOver() && !engine.isHumanTurn()) scheduleBotTurns();
        });
        t.setRepeats(false); t.start();
    }

    private void checkJogoOver() {
        if (!engine.isGameOver()) return;
        reiniciar();
        int choice = JOptionPane.showConfirmDialog(this,
            engine.getWinner() + "\n\nDeseja jogar novamente?",
            "Fim de Jogo", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) newJogo();
    }

    private void newJogo() {
        engine.startNewJogo(); dominoSelecionado = null; reiniciar();
        if (!engine.isHumanTurn()) scheduleBotTurns();
    }

    private void showMsg(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Aviso", JOptionPane.WARNING_MESSAGE);
    }

    public Fase getFaseAtual() {
        return faseAtual;
    }
    // ═══════════════════════════════ MAIN ═══════════════════════════════════

//     public static void main(String[] args) {
//         SwingUtilities.invokeLater(() -> {
//             try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
//             catch (Exception ignored) {}
//             new DominoxJogo().setVisible(true);
//         });
//     }
 }