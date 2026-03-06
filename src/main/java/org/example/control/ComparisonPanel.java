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
import java.io.PrintWriter;
import java.util.List;

public class ComparisonPanel extends JPanel {

    private JSpinner sizeSpinner;
    private JComboBox<String> typeCombo;
    private JSpinner runsSpinner;
    private JButton chooseFileBtn;
    private JLabel fileLabel;
    private JButton runBtn;
    private JButton exportBtn;
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
        runsSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 1000, 1));

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

        exportBtn = new JButton("Export CSV");
        exportBtn.setBackground(new Color(0, 255, 150));
        exportBtn.setForeground(Color.BLACK);
        exportBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        controlPanel.add(Box.createHorizontalStrut(10)); // A little gap between Run and Export
        controlPanel.add(exportBtn);
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

        exportBtn.addActionListener(e -> {
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No data to export!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Save as CSV");
            int userSelection = fileChooser.showSaveDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File fileToSave = fileChooser.getSelectedFile();

                if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                    fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".csv");
                }

                try (PrintWriter writer = new PrintWriter(fileToSave)) {

                    for (int i = 0; i < tableModel.getColumnCount(); i++) {
                        writer.print(tableModel.getColumnName(i));
                        if (i < tableModel.getColumnCount() - 1) writer.print(",");
                    }
                    writer.println();
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        for (int j = 0; j < tableModel.getColumnCount(); j++) {
                            writer.print(tableModel.getValueAt(i, j));
                            if (j < tableModel.getColumnCount() - 1) writer.print(",");
                        }
                        writer.println();
                    }
                    JOptionPane.showMessageDialog(this, "Export Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Error saving file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
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
                    new PureHeapSort(),
                    new PureQuickSort()
            };

            if (mode.equals("File")) {
                for (File file : files) {
                    int[] fileArray = DataGenerator.readFromFile(file.getAbsolutePath());

                    int[][] runArrays = new int[numRuns][];
                    for (int i = 0; i < numRuns; i++) {
                        runArrays[i] = fileArray; // Use the exact same file array
                    }
                    runAlgorithmsOnArrays(algorithms, runArrays, fileArray.length, file.getName());
                }
            } else {
                int[][] runArrays = new int[numRuns][];

                if (mode.equals("Sorted")) {
                    // Generate once
                    int[] singleSorted = DataGenerator.generateSorted(size);
                    for (int i = 0; i < numRuns; i++) {
                        runArrays[i] = singleSorted;
                    }
                } else if (mode.equals("Inversely Sorted")) {

                    int[] singleInverse = DataGenerator.generateReversed(size);
                    for (int i = 0; i < numRuns; i++) {
                        runArrays[i] = singleInverse;
                    }
                } else {
                    for (int i = 0; i < numRuns; i++) {
                        runArrays[i] = DataGenerator.generateRandom(size);
                    }
                }

                runAlgorithmsOnArrays(algorithms, runArrays, size, mode);
            }
            return null;
        }

        private void runAlgorithmsOnArrays(SortAlgorithm[] algorithms, int[][] runArrays, int currentSize, String modeText) {
            for (SortAlgorithm algo : algorithms) {
                long minTime = Long.MAX_VALUE;
                long maxTime = Long.MIN_VALUE;

                long totalTime = 0;
                long totalComp = 0;
                long totalSwap = 0;

                for (int i = 0; i < numRuns; i++) {

                    SortResult result = algo.sort(runArrays[i]);

                    long time = result.runTimeNano;
                    totalTime += time;
                    if (time < minTime) minTime = time;
                    if (time > maxTime) maxTime = time;

                    totalComp += result.comparisons;
                    totalSwap += result.interchanges;
                }


                long avgTime = totalTime / numRuns;
                long avgComp = totalComp / numRuns;
                long avgSwap = totalSwap / numRuns;

                Object[] rowData = {
                        algo.getName(), currentSize, modeText, numRuns,
                        avgTime, minTime, maxTime, avgComp, avgSwap
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