package org.example.algovisual;

import org.example.SortPanel;

import javax.swing.*;
import java.util.List;

public class SelectionSort extends SwingWorker<Void, SortState> {
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
                publish(new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2));
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
            publish(new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2));
            Thread.sleep(speed);
        }
        return null;
    }

    @Override
    protected void process(List<SortState> chunks) {
        SortState state = chunks.getLast();
        sortPanel.updateVisualization(state.array, state.comparisons,
                state.interchanges, state.currentStatus,
                state.interchangedIdx1, state.interchangedIdx2,
                state.comparedIdx1, state.comparedIdx2);
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
