package br.maua.dominox;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TabuleiroPanel extends JPanel {

    static final int PECA_W       = 110;
    static final int PECA_H       = 50;
    static final int GAP          = 10;
    static final int CELULA_W     = PECA_W + GAP;
    static final int CELULA_H     = PECA_H + GAP;
    static final int PECAS_POR_LINHA = 10;

    private static final Font FONTE      = new Font("SansSerif", Font.BOLD, 9);
    private static final Font FONTE_VERT = new Font("SansSerif", Font.BOLD, 8);

    private List<Domino> pecas;

    public TabuleiroPanel() {
        setOpaque(false);
    }

    public void atualizar(List<Domino> pecas, String leftEnd, String rightEnd) {
        this.pecas = pecas;
        recalcularTamanho();
        repaint();
    }

    // ── Tamanho preferido ─────────────────────────────────────

    private void recalcularTamanho() {
        if (pecas == null || pecas.isEmpty()) {
            setPreferredSize(new Dimension(400, PECA_H + GAP * 4));
            return;
        }
        Point[] posicoes = calcularPosicoes();
        int maxX = 0, maxY = 0;
        for (int i = 0; i < pecas.size(); i++) {
            boolean vert = eCurvaPeca(i);
            int w = vert ? PECA_H : PECA_W;
            int h = vert ? PECA_W : PECA_H;
            maxX = Math.max(maxX, posicoes[i].x + w);
            maxY = Math.max(maxY, posicoes[i].y + h);
        }
        setPreferredSize(new Dimension(maxX + GAP * 2, maxY + GAP * 2));
    }

    // ── Layout em serpentina ──────────────────────────────────

    private Point[] calcularPosicoes() {
        int n = pecas.size();
        Point[] pos = new Point[n];

        int x = GAP;
        int y = GAP;
        int dx = 1;         // +1 direita, -1 esquerda
        int contLinha = 0;

        for (int i = 0; i < n; i++) {
            pos[i] = new Point(x, y);
            contLinha++;

            if (contLinha == PECAS_POR_LINHA && i < n - 1) {
                // Fim de linha: próxima peça é a curva (vertical), desce
                y += CELULA_W;
                // Depois da curva, inverte direção e reposiciona x
                dx = -dx;
                contLinha = 0;
                x = (dx == 0) ? GAP : GAP + (PECAS_POR_LINHA - 1) * CELULA_W;
            } else {
                x += dx * CELULA_W;
            }
        }
        return pos;
    }

    private boolean eCurvaPeca(int idx) {
        // É curva se é a última peça de uma linha cheia e ainda há peças depois
        return (idx % PECAS_POR_LINHA == PECAS_POR_LINHA - 1) && (idx < pecas.size() - 1);
    }

    // ── Pintura ───────────────────────────────────────────────

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (pecas == null || pecas.isEmpty()) {
            g.setColor(new Color(255, 255, 255, 120));
            g.setFont(new Font("SansSerif", Font.ITALIC, 13));
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,      RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        Point[] posicoes = calcularPosicoes();

        for (int i = 0; i < pecas.size(); i++) {
            Domino d = pecas.get(i);
            Point  p = posicoes[i];
            if (eCurvaPeca(i)) {
                desenharPecaVertical(g2, p.x, p.y, d);
            } else {
                desenharPecaHorizontal(g2, p.x, p.y, d);
            }
        }
    }

    // ── Peça horizontal ───────────────────────────────────────

    private void desenharPecaHorizontal(Graphics2D g2, int x, int y, Domino d) {
        int hw = PECA_W / 2;

        desenharMetade(g2, x, y, hw, PECA_H, d.getLeft(), false);
        desenharMetade(g2, x + hw, y, hw, PECA_H, d.getRight(), true);

        g2.setColor(new Color(150, 150, 150));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(x + hw, y + 4, x + hw, y + PECA_H - 4);

        g2.setColor(new Color(180, 180, 180));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(x, y, PECA_W, PECA_H, 7, 7);
    }

    private void desenharMetade(Graphics2D g2, int x, int y, int w, int h, String texto, boolean ladoDireito) {
        g2.setColor(new Color(242, 242, 242));
        if (!ladoDireito) {
            g2.fillRoundRect(x, y, w + 4, h, 7, 7);
            g2.fillRect(x + w - 2, y, 6, h);
        } else {
            g2.fillRoundRect(x - 4, y, w + 4, h, 7, 7);
            g2.fillRect(x, y, 6, h);
        }
        desenharTexto(g2, texto, x, y, w, h, FONTE);
    }

    // ── Peça vertical (curva) ─────────────────────────────────

    private void desenharPecaVertical(Graphics2D g2, int x, int y, Domino d) {
        int vw = PECA_H;
        int vh = PECA_W;
        int hh = vh / 2;

        desenharMetadeV(g2, x, y, vw, hh, d.getLeft(), false);
        desenharMetadeV(g2, x, y + hh, vw, hh, d.getRight(), true);

        g2.setColor(new Color(150, 150, 150));
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawLine(x + 4, y + hh, x + vw - 4, y + hh);

        g2.setColor(new Color(180, 180, 180));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(x, y, vw, vh, 7, 7);
    }

    private void desenharMetadeV(Graphics2D g2, int x, int y, int w, int h,
                                 String texto, boolean ladoBaixo) {
        g2.setColor(new Color(242, 242, 242));
        if (!ladoBaixo) {
            g2.fillRoundRect(x, y, w, h + 4, 7, 7);
            g2.fillRect(x, y + h - 2, w, 6);
        } else {
            g2.fillRoundRect(x, y - 4, w, h + 4, 7, 7);
            g2.fillRect(x, y, w, 6);
        }
        desenharTexto(g2, texto, x, y, w, h, FONTE_VERT);
    }

    // ── Texto centralizado ────────────────────────────────────

    private void desenharTexto(Graphics2D g2, String texto, int x, int y,
                               int w, int h, Font fonte) {
        g2.setFont(fonte);
        g2.setColor(new Color(30, 30, 30));
        FontMetrics fm = g2.getFontMetrics();

        String[] linhas = quebrarTexto(texto, fm, w - 4);
        int altTotal = linhas.length * fm.getHeight();
        int startY   = y + (h - altTotal) / 2 + fm.getAscent();

        for (String linha : linhas) {
            int lw = fm.stringWidth(linha);
            g2.drawString(linha, x + (w - lw) / 2, startY);
            startY += fm.getHeight();
        }
    }

    private String[] quebrarTexto(String texto, FontMetrics fm, int maxW) {
        if (fm.stringWidth(texto) <= maxW) return new String[]{texto};
        for (int i = texto.length() / 2; i < texto.length(); i++) {
            char c = texto.charAt(i);
            if (c == ' ' || c == '(') {
                return new String[]{texto.substring(0, i).trim(),
                                    texto.substring(i).trim()};
            }
        }
        return new String[]{texto};
    }
}
