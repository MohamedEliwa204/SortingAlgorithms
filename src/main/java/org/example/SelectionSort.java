package org.example;

import javax.swing.*;
import java.util.List;

public class SelectionSort extends SwingWorker<Void, int[]> {
    private int[] array;
    private SortPanel sortPanel;
    private int comparisons = 0;
    private int interchanges = 0;
    private String currentStatus = "Starting Selection sort...";
    private int interchangedIdx1 = -1;
    private int interchangedIdx2 = -1;
    private int comparedIdx1 = -1;
    private int comparedIdx2 = -1;

    private int speed;

    public SelectionSort(int[] array, SortPanel sortPanel, int speed) {
        this.array = array;
        this.sortPanel = sortPanel;
        this.speed = speed;
    }

    @Override
    protected Void doInBackground() throws Exception {
        for (int i = 0; i < array.length - 1; i++) {
            int min = array[i];
            int idx = i;
            for (int j = i + 1; j < array.length; j++) {
                comparisons++;
                comparedIdx1 = j;
                comparedIdx2 = idx;
                interchangedIdx1 = -1;
                interchangedIdx2 = -1;
                currentStatus = "Comparing " + array[j] + "the minimum value in the sub array" + min;
                publish(array.clone());
                Thread.sleep(speed);
                if (array[j] < min) {
                    min = array[j];
                    idx = j;
                }
            }
            if (idx != i) {
                interchanges++;
                interchangedIdx1 = i;
                interchangedIdx2 = idx;
                comparedIdx1 = -1;
                comparedIdx2 = -1;
                currentStatus = "Swapping " + array[i] + " with" + array[idx];
            }
            int temp = array[idx];
            array[idx] = array[i];
            array[i] = temp;
            publish(array.clone());
            Thread.sleep(speed);
        }
        return null;
    }

    @Override
    protected void process(List<int[]> chunks) {
        int[] arr = chunks.getLast();
        sortPanel.updateVisualization(arr, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2);
    }

    @Override
    protected void done() {
        try {
            get();
            sortPanel.updateVisualization(array, comparisons, interchanges, "Sorting complete!", -1, -1, -1, -1);


        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
