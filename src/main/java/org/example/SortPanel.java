package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class SortPanel extends JPanel {
    private int[] array;
    private int comparisons = 0;
    private int interChanges = 0;

    private String statusText = "Ready to Sort!";
    private int highlightIdx1 = -1;
    private int highlightIdx2 = -1;
    public SortPanel(){
        setBackground(Color.DARK_GRAY);
        ToolTipManager.sharedInstance().registerComponent(this); // for the value of  bars
    }
    public void updateVisualization(int[] array, int comparisons, int interChanges, String statusText, int highlightIdx1, int highlightIdx2){
        this.array = array.clone();
        this.comparisons = comparisons;
        this.interChanges = interChanges;
        this.statusText = statusText;
        this.highlightIdx1 = highlightIdx1;
        this.highlightIdx2 = highlightIdx2;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (array == null || array.length == 0){
            return;
        }
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        int barWidth = width / array.length;

        int max = 0;
        for (int val : array){
            if (val > max){
                max = val;
            }
        }

        for (int i = 0; i < array.length; i++) {
            int barHeight = (int) (((double) array[i] / max) * (height - 100));
            int x = i * barWidth;
            int y = height - barHeight;
            if (i == highlightIdx1 || i == highlightIdx2){
                g2d.setColor(Color.RED);
            }else{
                g2d.setColor(Color.CYAN);
            }

            g2d.fillRect(x, y, barWidth - 1, barHeight);
            if (barWidth > 20){
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                g2d.drawString(String.valueOf(array[i]), x + (barWidth / 4), y - 5);
            }


        }
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Status: "+ statusText, 15, 25);
        g2d.drawString("comparisons: " + comparisons, 15, 50);
        g2d.drawString("interchanges: " + interChanges, 15, 75);
    }

    @Override
    public String getToolTipText(MouseEvent e){
        if(array == null || array.length == 0){
            return null;
        }

        int barWidth = getWidth() / array.length;
        int MouseX = e.getX();
        int idx = MouseX / barWidth;

        if (idx >= 0 && idx < array.length){
            return "Value" + array[idx];
        }
        return null;
    }
}
