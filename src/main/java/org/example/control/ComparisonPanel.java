package org.example.control;

import org.example.algo.*;
import org.example.algovisual.HeapSort;
import org.example.control.DataGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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

    private File[] selectedFiles = null;

    public ComparisonPanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 35));

        JPanel controlPanel = new JPanel();
        controlPanel.setBackground(new Color(40, 40, 45));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sizeSpinner = new JSpinner(new SpinnerNumberModel(1000, 10, 10000, 100));
        String[] arrayTypes = {"Random", "Sorted", "Inversely Sorted", "File"};
        typeCombo = new JComboBox<>(arrayTypes);
        runsSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 50, 1));

        chooseFileBtn = new JButton("Choose File");
        chooseFileBtn.setEnabled(false);

        fileLabel = new JLabel("No file selected");
        fileLabel.setForeground(Color.LIGHT_GRAY);

        runBtn = new JButton("Run Benchmark");
        runBtn.setBackground(new Color(0, 212, 255));
        runBtn.setForeground(Color.BLACK);
        runBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        addStyledLabel(controlPanel, "Array Size:");
        controlPanel.add(sizeSpinner);
        addStyledLabel(controlPanel, "  Mode:");
        controlPanel.add(typeCombo);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(chooseFileBtn);
        controlPanel.add(Box.createHorizontalStrut(10));
        controlPanel.add(fileLabel);
        addStyledLabel(controlPanel, "  Runs:");
        controlPanel.add(runsSpinner);
        controlPanel.add(Box.createHorizontalStrut(20));
        controlPanel.add(runBtn);

        add(controlPanel, BorderLayout.NORTH);

        String[] columns = {
                "Algorithm", "Array Size", "Mode", "Runs",
                "Avg Runtime (ns)", "Min Runtime (ns)", "Max Runtime (ns)",
                "Comparisons", "Interchanges"
        };
        tableModel = new DefaultTableModel(columns, 0);
        resultTable = new JTable(tableModel);

        resultTable.setBackground(new Color(35, 35, 40));
        resultTable.setForeground(Color.WHITE);
        resultTable.setGridColor(new Color(60, 60, 70));
        resultTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resultTable.setRowHeight(35); // Taller rows for readability
        resultTable.setSelectionBackground(new Color(0, 212, 255)); // Cyan selection highlight
        resultTable.setSelectionForeground(Color.BLACK);
        resultTable.setFillsViewportHeight(true);

        JTableHeader header = resultTable.getTableHeader();
        header.setBackground(new Color(20, 20, 25));
        header.setForeground(new Color(0, 25, 150)); // Mint green headers
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setPreferredSize(new Dimension(header.getWidth(), 40));

        DefaultTableCellRenderer customRenderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);

                if (!isSelected) {
                    if (row % 2 == 0) {
                        c.setBackground(new Color(40, 40, 45));
                    } else {
                        c.setBackground(new Color(48, 48, 55));
                    }
                }
                return c;
            }
        };

        for (int i = 0; i < resultTable.getColumnCount(); i++) {
            resultTable.getColumnModel().getColumn(i).setCellRenderer(customRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.getViewport().setBackground(new Color(30, 30, 35));
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add some padding around the table

        add(scrollPane, BorderLayout.CENTER);

        setupActions();
    }

    private void addStyledLabel(JPanel panel, String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        panel.add(label);
    }

    private void setupActions() {
        typeCombo.addActionListener(e -> {
            boolean isFile = typeCombo.getSelectedItem().equals("File");
            chooseFileBtn.setEnabled(isFile);
            sizeSpinner.setEnabled(!isFile);
        });

        chooseFileBtn.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFiles = fileChooser.getSelectedFiles();
                if (selectedFiles.length == 1) {
                    fileLabel.setText(selectedFiles[0].getName());
                } else {
                    fileLabel.setText(selectedFiles.length + " files selected");
                }
            }
        });

        runBtn.addActionListener(e -> {
            String mode = (String) typeCombo.getSelectedItem();
            if (mode.equals("File") && (selectedFiles == null || selectedFiles.length == 0)) {
                JOptionPane.showMessageDialog(this, "Please select at least one file first!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            runBtn.setEnabled(false);
            runBtn.setText("Running...");

            int size = (int) sizeSpinner.getValue();
            int runs = (int) runsSpinner.getValue();
            BenchmarkWorker worker = new BenchmarkWorker(size, mode, runs, selectedFiles);
            worker.execute();
        });
    }

    private class BenchmarkWorker extends SwingWorker<Void, Object[]> {
        private int size;
        private String mode;
        private int numRuns;
        private File[] files;

        public BenchmarkWorker(int size, String mode, int numRuns, File[] files) {
            this.size = size;
            this.mode = mode;
            this.numRuns = numRuns;
            this.files = files;
        }

        @Override
        protected Void doInBackground() throws Exception {
            SortAlgorithm[] algorithms = {
                    new PureBubbleSort(),
                    new PureSelectionSort(),
                    new PureInsertionSort(),
                    new PureMergeSort(),
                    new PureQuickSort(),
                    new PureHeapSort()
            };

            if (mode.equals("File")) {
                for (File file : files) {
                    int[] baseArray = DataGenerator.readFromFile(file.getAbsolutePath());
                    runAlgorithmsOnArray(algorithms, baseArray, baseArray.length, file.getName());
                }
            } else {
                int[] baseArray;
                if (mode.equals("Sorted")) {
                    baseArray = DataGenerator.generateSorted(size);
                } else if (mode.equals("Inversely Sorted")) {
                    baseArray = DataGenerator.generateReversed(size);
                } else {
                    baseArray = DataGenerator.generateRandom(size);
                }
                runAlgorithmsOnArray(algorithms, baseArray, size, mode);
            }
            return null;
        }

        private void runAlgorithmsOnArray(SortAlgorithm[] algorithms, int[] baseArray, int currentSize, String modeText) {
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
                        algo.getName(), currentSize, modeText, numRuns,
                        avgTime, minTime, maxTime, comp, swap
                };
                publish(rowData);
            }
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
            runBtn.setBackground(new Color(0, 212, 255)); // Reset to Cyan
        }
    }
}