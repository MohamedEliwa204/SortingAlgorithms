package org.example.control;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SortPanel extends JPanel {
    private int[] array;
    private int comparisons = 0;
    private int interChanges = 0;

    private String statusText = "Ready to Sort!";
    private int intechangedIdx1 = -1;
    private int interChangedIdx2 = -1;
    private int comparedIdx1 = -1;
    private int comparedIdx2 = -1;

    public SortPanel() {

        setBackground(new Color(30, 30, 35));
        ToolTipManager.sharedInstance().registerComponent(this);
    }

    public void updateVisualization(int[] array, int comparisons, int interChanges, String statusText, int intechangedIdx1, int intechangedIdx2, int comparedIdx1, int comparedIdx2) {
        this.array = array.clone();
        this.comparisons = comparisons;
        this.interChanges = interChanges;
        this.statusText = statusText;
        this.intechangedIdx1 = intechangedIdx1;
        this.interChangedIdx2 = intechangedIdx2;
        this.comparedIdx1 = comparedIdx1;
        this.comparedIdx2 = comparedIdx2;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (array == null || array.length == 0) {
            return;
        }

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / array.length;

        int max = 0;
        for (int val : array) {
            if (val > max) max = val;
        }

        for (int i = 0; i < array.length; i++) {
            int barHeight = (int) (((double) array[i] / max) * (height - 120));
            int x = i * barWidth;
            int y = height - barHeight - 15;

            Color topColor;
            Color bottomColor;
            if (i == intechangedIdx1 || i == interChangedIdx2) {
                topColor = new Color(255, 85, 85);     // Neon Red
                bottomColor = new Color(139, 0, 0);    // Dark Red
            } else if (i == comparedIdx1 || i == comparedIdx2) {
                topColor = new Color(255, 255, 85);    // Bright Yellow
                bottomColor = new Color(204, 153, 0);  // Gold
            } else {
                topColor = new Color(0, 212, 255);     // Neon Cyan
                bottomColor = new Color(9, 9, 121);    // Deep Blue
            }

            GradientPaint gp = new GradientPaint(x, y, topColor, x, height, bottomColor);
            g2d.setPaint(gp);
            g2d.fillRoundRect(x, y, barWidth - 2, barHeight + 15, 10, 10);

            g2d.setColor(new Color(0, 0, 0, 100));
            g2d.drawRoundRect(x, y, barWidth - 2, barHeight + 15, 10, 10);
            if (barWidth > 20) {
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));

                String valStr = String.valueOf(array[i]);
                FontMetrics fm = g2d.getFontMetrics();
                int textX = x + (barWidth - 2 - fm.stringWidth(valStr)) / 2;

                g2d.drawString(valStr, textX, y - 8);
            }
        }
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        g2d.setColor(Color.BLACK);
        g2d.drawString("Status: " + statusText, 17, 27);
        g2d.setColor(new Color(0, 255, 150));
        g2d.drawString("Status: " + statusText, 15, 25);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString("Comparisons: " + comparisons, 15, 50);
        g2d.drawString("Interchanges: " + interChanges, 15, 75);
    }

    @Override
    public String getToolTipText(MouseEvent e) {
        if (array == null || array.length == 0) {
            return null;
        }

        int barWidth = getWidth() / array.length;
        int MouseX = e.getX();
        int idx = MouseX / barWidth;

        if (idx >= 0 && idx < array.length) {
            return "Index: " + idx + " | Value: " + array[idx];
        }
        return null;
    }
}