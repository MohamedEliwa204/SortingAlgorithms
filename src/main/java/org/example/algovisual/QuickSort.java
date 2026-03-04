package org.example.algovisual;

import org.example.SortPanel;

import javax.swing.*;
import java.util.List;

public class QuickSort extends SwingWorker<Void, SortState> {
    private int[] array;
    private SortPanel sortPanel;
    private int comparisons = 0;
    private int interchanges = 0;
    private String currentStatus = "Starting Quick Sort...";
    private int interchangedIdx1 = -1;
    private int interchangedIdx2 = -1;
    private int comparedIdx1 = -1;
    private int comparedIdx2 = -1;

    private int speed;

    public QuickSort(int[] array, SortPanel sortPanel, int speed) {
        this.array = array;
        this.sortPanel = sortPanel;
        this.speed = speed;
    }

    @Override
    protected Void doInBackground() throws Exception {
        int n = array.length;
        quickSort(0, n - 1);
        return null;
    }

    private void quickSort(int start, int end) throws Exception{
        if (end <= start) {
            return;
        }
        int pi = partition(start, end);
        quickSort(start, pi - 1);
        quickSort(pi + 1, end);
    }

    private int partition(int start, int end) throws Exception {
        int pivot = array[end]; // i will randomize this operation later
        comparedIdx1 = end;
        comparedIdx2 = -1;
        interchangedIdx1 = -1;
        interchangedIdx2 = -1;
        currentStatus = "Selected Pivot: " + pivot;
        publish(new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2));
        Thread.sleep(speed);
        int i = start - 1;
        for (int j = start; j < end; j++) {
            comparisons++;
            comparedIdx1 = j;
            comparedIdx2 = end;
            interchangedIdx1 = -1;
            interchangedIdx2 = -1;
            currentStatus = "Comparing " + array[j] + " with pivot " + pivot;
            publish(new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2));
            Thread.sleep(speed);
            if (array[j] < pivot) {

                i++;
                swap(i, j);

            }
        }
        swap(i + 1, end);
        return i + 1;

    }

    private void swap(int i, int j) throws Exception {
        if (i == j) return;
        interchanges++;
        comparedIdx1 = -1;
        comparedIdx2 = -1;
        interchangedIdx1 = i;
        interchangedIdx2 = j;
        currentStatus = "Swapping " + array[i] + " and " + array[j];
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
        publish(new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2));
        Thread.sleep(speed);
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


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
