package org.example;

import org.example.algo.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.util.List;

public class ComparisonPanel extends JPanel {

    private JSpinner sizeSpinner;
    private JComboBox<String> typeCombo;
    private JSpinner runsSpinner;
    private JButton chooseFileBtn;
    private JLabel fileLabel;
    private JButton runBtn;

    private DefaultTableModel tableModel;
    private JTable resultTable;

    private File selectedFile = null;

    public ComparisonPanel() {
        setLayout(new BorderLayout());
        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(Color.LIGHT_GRAY);
        sizeSpinner = new JSpinner(new SpinnerNumberModel(1000, 10, 10000, 100)); // Up to 10,000
        String[] arrayTypes = {"Random", "Sorted", "Inversely Sorted", "File"};
        typeCombo = new JComboBox<>(arrayTypes);
        runsSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 50, 1)); // Number of runs

        chooseFileBtn = new JButton("Choose File");
        chooseFileBtn.setEnabled(false);
        fileLabel = new JLabel("No file selected");

        runBtn = new JButton("Run Benchmark");

        controlPanel.add(new JLabel("Array Size:"));
        controlPanel.add(sizeSpinner);
        controlPanel.add(new JLabel("  Mode:"));
        controlPanel.add(typeCombo);
        controlPanel.add(chooseFileBtn);
        controlPanel.add(fileLabel);
        controlPanel.add(new JLabel("  Runs:"));
        controlPanel.add(runsSpinner);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(runBtn);

        add(controlPanel, BorderLayout.NORTH);
        String[] columns = {
                "Algorithm", "Array Size", "Generation Mode", "Runs",
                "Avg Runtime (ns)", "Min Runtime (ns)", "Max Runtime (ns)",
                "Comparisons", "Interchanges"
        };
        tableModel = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);
        resultTable.setFillsViewportHeight(true);

        JScrollPane scrollPane = new JScrollPane(resultTable);
        add(scrollPane, BorderLayout.CENTER);
        setupActions();
    }

    private void setupActions() {
        typeCombo.addActionListener(e -> {
            boolean isFile = typeCombo.getSelectedItem().equals("File");
            chooseFileBtn.setEnabled(isFile);
            sizeSpinner.setEnabled(!isFile);
        });


        chooseFileBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                fileLabel.setText(selectedFile.getName());
            }
        });

        runBtn.addActionListener(e -> {
            String mode = (String) typeCombo.getSelectedItem();
            if (mode.equals("File") && selectedFile == null) {
                JOptionPane.showMessageDialog(this, "Please select a file first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            runBtn.setEnabled(false);
            runBtn.setText("Running...");
            int size = (int) sizeSpinner.getValue();
            int runs = (int) runsSpinner.getValue();
            BenchmarkWorker worker = new BenchmarkWorker(size, mode, runs, selectedFile);
            worker.execute();
        });
    }
    private class BenchmarkWorker extends SwingWorker<Void, Object[]> {
        private int size;
        private String mode;
        private int numRuns;
        private File file;

        public BenchmarkWorker(int size, String mode, int numRuns, File file) {
            this.size = size;
            this.mode = mode;
            this.numRuns = numRuns;
            this.file = file;
        }

        @Override
        protected Void doInBackground() throws Exception {
            int[] baseArray;
            String modeText = mode;

            if (mode.equals("File")) {
                baseArray = DataGenerator.readFromFile(file.getAbsolutePath());
                size = baseArray.length; // Override size with actual file size
                modeText = file.getName();
            } else if (mode.equals("Sorted")) {
                baseArray = DataGenerator.generateSorted(size);
            } else if (mode.equals("Inversely Sorted")) {
                baseArray = DataGenerator.generateReversed(size);
            } else {
                baseArray = DataGenerator.generateRandom(size);
            }

            SortAlgorithm[] algorithms = {
                    new PureBubbleSort(),
                    new PureSelectionSort(),
                    new PureInsertionSort(),
                    new PureMergeSort(),
                    new PureHeapSort(),
                    new PureQuickSort()
            };
            for (SortAlgorithm algo : algorithms) {
                long minTime = Long.MAX_VALUE;
                long maxTime = Long.MIN_VALUE;
                long totalTime = 0;
                long comp = 0;
                long swap = 0;

                for (int i = 0; i < numRuns; i++) {
                    SortResult result = algo.sort(baseArray);

                    long time = result.runTimeNano;
                    totalTime += time;
                    if (time < minTime) minTime = time;
                    if (time > maxTime) maxTime = time;
                    comp = result.comparisons;
                    swap = result.interchanges;
                }

                long avgTime = totalTime / numRuns;
                Object[] rowData = {
                        algo.getName(), size, modeText, numRuns,
                        avgTime, minTime, maxTime, comp, swap
                };
                publish(rowData);
            }

            return null;
        }

        @Override
        protected void process(List<Object[]> chunks) {
            for (Object[] row : chunks) {
                tableModel.addRow(row);
            }
        }

        @Override
        protected void done() {
            runBtn.setEnabled(true);
            runBtn.setText("Run Benchmark");
        }
    }
}