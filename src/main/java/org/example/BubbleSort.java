package org.example;

import javax.swing.*;
import java.util.List;

public class BubbleSort extends SwingWorker<Void, int[]> {

    private int[] array;
    private SortPanel sortPanel;
    private int comparisons = 0;
    private int interchanges = 0;
    private String currentStatus = "Starting...";
    private int interchangedIdx1 = -1;
    private int interchangedIdx2 = -1;
    private int comparedIdx1 = -1;
    private int comparedIdx2 = -1;

    private int speed;

    public BubbleSort(int[] array, SortPanel sortPanel, int speed) {
        this.array = array;
        this.sortPanel = sortPanel;
        this.speed = speed;
    }

    @Override
    protected Void doInBackground() throws Exception {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                comparisons++;
                comparedIdx1 = j;
                comparedIdx2 = j + 1;
                interchangedIdx1 = -1;
                interchangedIdx2 = -1;
                currentStatus = "Comparing " + array[j] + " and " + array[j + 1];
                publish(array.clone());
                Thread.sleep(speed);
                if (array[j] > array[j + 1]) {
                    interchanges++;
                    comparedIdx2 = -1;
                    comparedIdx1 = -1;
                    interchangedIdx1 = j;
                    interchangedIdx2 = j + 1;
                    currentStatus = "Swaping " + array[j] + "and" + array[j + 1];
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    publish(array.clone());
                    Thread.sleep(speed);

                }
            }
        }
        return null;
    }

    @Override
    protected void process(List<int[]> chunks) {
        int[] arr = chunks.get(chunks.size() - 1);
        sortPanel.updateVisualization(arr, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2);
    }

    @Override
    protected void done(){
        try {
            get();
            sortPanel.updateVisualization(array, comparisons, interchanges, "Sorting complete!", -1, -1, -1, -1);


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
