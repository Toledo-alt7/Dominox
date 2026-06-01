package br.maua.dominox;
import javax.swing.*;
import java.awt.*;

public class DominoPanel extends JPanel {

    private String left;
    private String right;
    private boolean faceDown;
    private boolean selected;
    private boolean playable;


    private static final int HALF_W = 64;
    private static final int TILE_H = 36;
    private static final Font VALUE_FONT   = new Font("SansSerif", Font.BOLD, 10);
    private static final Font FACEDOWN_FONT = new Font("SansSerif", Font.BOLD, 12);

    public DominoPanel(String left, String right) {
        this.left = left;
        this.right = right;
        this.faceDown = false;
        setPreferredSize(new Dimension(HALF_W * 2 + 6, TILE_H + 6));
        setOpaque(false);
    }

    public void setSelected(boolean selected) { this.selected = selected; repaint(); }
    public void setPlayable(boolean playable) { this.playable = playable; repaint(); }
    public boolean isSelected() { return selected; }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,  RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int ox = 3, oy = 3;
        int totalW = HALF_W * 2;

        drawHalf(g2, ox, oy, left, false);
        drawHalf(g2, ox + HALF_W, oy, right, true);

        g2.setColor(new Color(80, 50, 20));
        g2.setStroke(new BasicStroke(2f));
        g2.drawLine(ox + HALF_W, oy + 4, ox + HALF_W, oy + TILE_H - 4);

        if (selected) {
            g2.setColor(new Color(255, 215, 0));
            g2.setStroke(new BasicStroke(3f));
        } else if (playable) {
            g2.setColor(new Color(80, 220, 80));
            g2.setStroke(new BasicStroke(2f));
        } else {
            g2.setColor(new Color(120, 80, 30));
            g2.setStroke(new BasicStroke(1.5f));
        }
        g2.drawRoundRect(ox, oy, totalW, TILE_H, 8, 8);
    }

    private void drawHalf(Graphics2D g2, int x, int y, String text, boolean rightHalf) {
        g2.setColor(new Color(255, 252, 228));
        if (rightHalf) {
            g2.fillRect(x, y, HALF_W, TILE_H);
        } else {
            g2.fillRoundRect(x, y, HALF_W + 6, TILE_H, 8, 8);
            g2.fillRect(x + 2, y, HALF_W - 2, TILE_H); // square right edge
        }

        g2.setFont(VALUE_FONT);
        g2.setColor(new Color(30, 10, 0));
        FontMetrics fm = g2.getFontMetrics();


        String[] lines = wrapText(text, fm, HALF_W - 6);
        int lineH = fm.getHeight();
        int totalTextH = lines.length * lineH;
        int startY = y + (TILE_H - totalTextH) / 2 + fm.getAscent();

        for (String line : lines) {
            int textW = fm.stringWidth(line);
            int startX = x + (HALF_W - textW) / 2;
            g2.drawString(line, startX, startY);
            startY += lineH;
        }
    }

    private String[] wrapText(String text, FontMetrics fm, int maxW) {
        if (fm.stringWidth(text) <= maxW) return new String[]{text};
        // Try to split at space or parenthesis
        for (int i = text.length() / 2; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == ' ' || c == '(') {
                String l1 = text.substring(0, i).trim();
                String l2 = text.substring(i).trim();
                return new String[]{l1, l2};
            }
        }
        return new String[]{text};
    }
}