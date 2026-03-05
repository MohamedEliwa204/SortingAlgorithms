package org.example.control;

import org.example.algovisual.*;

import javax.swing.*;

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
        add(startBtn);
        add(Box.createHorizontalStrut(20));

        add(new JLabel("  Algorithm:"));
        add(algoCombo);
        add(speedLabel);
        add(speedSlider);

        setupActions();
        generateBtn.doClick();
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
            sortPanel.updateVisualization(currentArray, 0, 0, "Ready to Sort!", -1, -1, -1, -1);
            startBtn.setEnabled(true);
        });

        startBtn.addActionListener(e -> {
            if (currentArray == null){
                return;
            }

            startBtn.setEnabled(false);
            generateBtn.setEnabled(false);
            String selectedAlgo = (String) algoCombo.getSelectedItem();
            int speed = (int) speedSlider.getValue();

            SwingWorker<Void, SortState> worker = null;
            switch (selectedAlgo){
                case "Bubble Sort":
                    worker = new BubbleSort(currentArray.clone(), sortPanel, speed);
                    break;
                case "Selection Sort":
                    worker = new SelectionSort(currentArray.clone(), sortPanel, speed);
                    break;
                case "Insertion Sort":
                    worker = new InsertionSort(currentArray.clone(), sortPanel, speed);
                    break;
                case  "Merge Sort":
                    worker = new MergeSort(currentArray.clone(), sortPanel, speed);
                    break;
                case "Heap Sort":
                    worker = new HeapSort(currentArray.clone(), sortPanel, speed);
                    break;
                case "Quick Sort":
                    worker = new QuickSort(currentArray.clone(), sortPanel, speed);
                    break;
            }

            if (worker != null){
                worker.addPropertyChangeListener(ev -> {
                    if (ev.getNewValue() == SwingWorker.StateValue.DONE){
                        startBtn.setEnabled(true);
                        generateBtn.setEnabled(true);
                    }
                });
                worker.execute();
            }
        });
    }

}
