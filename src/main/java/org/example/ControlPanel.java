package org.example;

import javax.swing.*;
import java.util.zip.DataFormatException;

public class ControlPanel extends JPanel {
    private SortPanel sortPanel;
    private int[] currentArray;
    private JButton generateBtn;
    private JButton startBtn;
    private JComboBox<String> algoCombo;
    private JSlider speedSlider;

    private JComboBox<String> typeCombo;
    private JSpinner sizeSpinner;
    public ControlPanel(SortPanel sortPanel){
        this.sortPanel = sortPanel;
        generateBtn = new JButton("Generate Array");
        String[] algorithms = {
                "Bubble Sort", "Selection Sort", "Insertion Sort",
                "Merge Sort", "Heap Sort", "Quick Sort"
        };

        sizeSpinner = new JSpinner(new SpinnerNumberModel(50, 5, 100, 5));
        String[] arrayTypes = {"Random", "Sorted", "Inversely Sorted"};
        typeCombo = new JComboBox<>(arrayTypes);
        algoCombo = new JComboBox<>(algorithms);
        JLabel speedLabel = new JLabel("Speed (ms):");
        speedSlider = new JSlider(1, 200, 50);
        startBtn = new JButton("Start Visualization");

        add(new JLabel("Size:"));
        add(sizeSpinner);
        add(new JLabel("Type:"));
        add(typeCombo);

        add(generateBtn);
        add(Box.createHorizontalStrut(20));

        add(new JLabel("  Algorithm:"));
        add(algoCombo);
        add(speedLabel);
        add(speedSlider);
        add(startBtn);
        setupActions();
    }


    private void setupActions(){
        generateBtn.addActionListener(e -> {
            int size = (int) sizeSpinner.getValue();
            String type = (String) typeCombo.getSelectedItem();

            if (type.equals("Random")){
                currentArray = DataGenerator.generateRandom(size);
            } else if (type.equals("Sorted")) {
                currentArray = DataGenerator.generateSorted(size);
            } else if (type.equals("Inversely Sorted")) {
                currentArray = DataGenerator.generateReversed(size);
            }
        });
    }

}
