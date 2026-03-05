package org.example;

import org.example.control.ComparisonPanel;
import org.example.control.ControlPanel;
import org.example.control.SortPanel;

import javax.swing.*;
import java.awt.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                // If Nimbus isn't available, fall back to default
            }
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
            ComparisonPanel comparisonPanel = new ComparisonPanel();
            comparisonTab.add(comparisonPanel, BorderLayout.CENTER);


            tabbedPane.addTab("Visualization Mode", visualizationTab);
            tabbedPane.addTab("Comparison Mode", comparisonTab);
            frame.add(tabbedPane);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

        });
    }
}