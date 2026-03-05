package org.example;

import javax.swing.*;
import java.awt.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Assignment 1 - Sorting Algorithms");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1000, 700);
            JTabbedPane tabbedPane = new JTabbedPane();

            // visualization
            JPanel visualizationTab = new JPanel(new BorderLayout());
            SortPanel sortPanel = new SortPanel();
            ControlPanel controlPanel = new ControlPanel(sortPanel);
            visualizationTab.add(controlPanel, BorderLayout.NORTH);
            visualizationTab.add(sortPanel, BorderLayout.CENTER);


            // comparison
            JPanel comparisonTab = new JPanel(new BorderLayout());
            JLabel tempLabel = new JLabel("Sorting Comparison Table will go here!", SwingConstants.CENTER);
            tempLabel.setFont(new Font("Arial", Font.BOLD, 20));
            comparisonTab.add(tempLabel, BorderLayout.CENTER);


            tabbedPane.addTab("Visualization Mode", visualizationTab);
            tabbedPane.addTab("Comparison Mode", comparisonTab);
            frame.add(tabbedPane);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
    }
}