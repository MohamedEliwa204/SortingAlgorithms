package org.example.control;

import org.example.algovisual.*;

import javax.swing.*;
import java.io.File;

public class ControlPanel extends JPanel {
    private SortPanel sortPanel;
    private int[] currentArray;
    private JButton generateBtn;
    private JButton startBtn;
    private JButton pauseBtn;
    private JButton resumeBtn;
    private JButton stepBtn;
    private JComboBox<String> algoCombo;
    private JSlider speedSlider;
    private PlaybackControl playbackControl = new PlaybackControl();
    private JComboBox<String> typeCombo;
    private JSpinner sizeSpinner;

    private JButton chooseFilesBtn;
    private JComboBox<String> fileCombo;
    private java.io.File[] selectedFiles;
    public ControlPanel(SortPanel sortPanel){
        this.sortPanel = sortPanel;
        generateBtn = new JButton("Generate Array");
        String[] algorithms = {
                "Bubble Sort", "Selection Sort", "Insertion Sort",
                "Merge Sort", "Heap Sort", "Quick Sort"
        };

        sizeSpinner = new JSpinner(new SpinnerNumberModel(50, 5, 100, 5));
        String[] arrayTypes = {"Random", "Sorted", "Inversely Sorted", "File"};
        typeCombo = new JComboBox<>(arrayTypes);
        chooseFilesBtn = new JButton("Choose Files");
        fileCombo = new JComboBox<>();
        chooseFilesBtn.setEnabled(false); // Disabled until "File" is selected
        fileCombo.setEnabled(false);
        algoCombo = new JComboBox<>(algorithms);
        JLabel speedLabel = new JLabel("Speed (ms):");
        speedSlider = new JSlider(1, 200, 50);
        startBtn = new JButton("Start Visualization");

        add(new JLabel("Size:"));
        add(sizeSpinner);
        add(new JLabel("Type:"));
        add(typeCombo);
        add(chooseFilesBtn);
        add(fileCombo);
        add(generateBtn);
        add(startBtn);
        pauseBtn = new JButton("Pause");
        resumeBtn = new JButton("Resume");
        stepBtn = new JButton("Step Next");
        pauseBtn.setEnabled(false);
        resumeBtn.setEnabled(false);
        stepBtn.setEnabled(false);
        add(pauseBtn);
        add(resumeBtn);
        add(stepBtn);
        add(Box.createHorizontalStrut(20));

        add(new JLabel("  Algorithm:"));
        add(algoCombo);
        add(speedLabel);
        add(speedSlider);

        setupActions();
        generateBtn.doClick();
    }


    private void setupActions(){
        typeCombo.addActionListener(e -> {
            boolean isFile = typeCombo.getSelectedItem().equals("File");
            sizeSpinner.setEnabled(!isFile);
            chooseFilesBtn.setEnabled(isFile);
            fileCombo.setEnabled(isFile);
        });
        chooseFilesBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            int result = fileChooser.showOpenDialog(this);

            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFiles = fileChooser.getSelectedFiles();
                fileCombo.removeAllItems();
                for (File f : selectedFiles) {
                    fileCombo.addItem(f.getName());
                }
            }
        });
        generateBtn.addActionListener(e -> {

            String type = (String) typeCombo.getSelectedItem();

            if (type.equals("File")) {
                if (selectedFiles == null || selectedFiles.length == 0 || fileCombo.getSelectedIndex() == -1) {
                    JOptionPane.showMessageDialog(this, "Please choose files first!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                File selected = selectedFiles[fileCombo.getSelectedIndex()];
                try {
                    currentArray = DataGenerator.readFromFile(selected.getAbsolutePath());
                    if (currentArray.length > 150) {
                        JOptionPane.showMessageDialog(this, "Warning: Visualizing an array this large will squeeze the bars together and may look messy. Comparison Mode is better for massive files!", "Size Warning", JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error reading file!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else if (type.equals("Random")){
                int size = (int) sizeSpinner.getValue();
                currentArray = DataGenerator.generateRandom(size);
            } else if (type.equals("Sorted")) {
                int size = (int) sizeSpinner.getValue();
                currentArray = DataGenerator.generateSorted(size);
            } else if (type.equals("Inversely Sorted")) {
                int size = (int) sizeSpinner.getValue();
                currentArray = DataGenerator.generateReversed(size);
            }
            sortPanel.updateVisualization(currentArray, 0, 0, "Ready to Sort!", -1, -1, -1, -1);
            startBtn.setEnabled(true);
        });

        startBtn.addActionListener(e -> {
            if (currentArray == null){
                return;
            }
            playbackControl.resume();
            pauseBtn.setEnabled(true);
            resumeBtn.setEnabled(false);
            stepBtn.setEnabled(false);

            startBtn.setEnabled(false);
            generateBtn.setEnabled(false);
            String selectedAlgo = (String) algoCombo.getSelectedItem();
            int speed = (int) speedSlider.getValue();

            SwingWorker<Void, SortState> worker = null;
            switch (selectedAlgo){
                case "Bubble Sort":
                    worker = new BubbleSort(currentArray.clone(), sortPanel, speed, playbackControl);
                    break;
                case "Selection Sort":
                    worker = new SelectionSort(currentArray.clone(), sortPanel, speed, playbackControl);
                    break;
                case "Insertion Sort":
                    worker = new InsertionSort(currentArray.clone(), sortPanel, speed, playbackControl);
                    break;
                case  "Merge Sort":
                    worker = new MergeSort(currentArray.clone(), sortPanel, speed, playbackControl);
                    break;
                case "Heap Sort":
                    worker = new HeapSort(currentArray.clone(), sortPanel, speed, playbackControl);
                    break;
                case "Quick Sort":
                    worker = new QuickSort(currentArray.clone(), sortPanel, speed, playbackControl);
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

        pauseBtn.addActionListener(e -> {
            playbackControl.pause();
            pauseBtn.setEnabled(false);
            resumeBtn.setEnabled(true);
            stepBtn.setEnabled(true);
        });

        resumeBtn.addActionListener(e -> {
            playbackControl.resume();
            pauseBtn.setEnabled(true);
            resumeBtn.setEnabled(false);
            stepBtn.setEnabled(false);
        });

        stepBtn.addActionListener(e -> {
            playbackControl.stepNext();
            pauseBtn.setEnabled(false);
            resumeBtn.setEnabled(true);
        });
    }

}
