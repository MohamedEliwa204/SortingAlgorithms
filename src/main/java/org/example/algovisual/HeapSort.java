package org.example.algovisual;

import org.example.SortPanel;

import javax.swing.*;
import java.util.List;

public class HeapSort extends SwingWorker<Void, SortState> {

    private int[] array;
    private SortPanel sortPanel;
    private int comparisons = 0;
    private int interchanges = 0;
    private String currentStatus = "Starting Heap Sort...";
    private int interchangedIdx1 = -1;
    private int interchangedIdx2 = -1;
    private int comparedIdx1 = -1;
    private int comparedIdx2 = -1;

    private int speed;

    public HeapSort(int[] array, SortPanel sortPanel, int speed) {
        this.array = array;
        this.sortPanel = sortPanel;
        this.speed = speed;
    }
    @Override
    protected Void doInBackground() throws Exception {
        int n = array.length;
        currentStatus = "Building initial max Heap...";
        SortState currentState = new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2);
        publish(currentState);
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(n, i);
        }

        for (int i =  n - 1; i > 0 ; i--) {
            interchanges++;
            comparedIdx1 = -1;
            comparedIdx2 = -1;
            interchangedIdx1 = 0;
            interchangedIdx2 = i;
            currentStatus = "Moving max(" + array[0] + ") to sorted position: " + i;
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;
            SortState currentState1 = new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2);
            publish(currentState1);
            Thread.sleep(speed);
            heapify(i, 0);
        }
        return null;
    }

    private void heapify(int n, int i) throws Exception{
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * (i + 1);

        if (left < n){
            comparisons++;
            comparedIdx1 = largest;
            comparedIdx2 = left;
            interchangedIdx1 = -1;
            interchangedIdx2 = -1;
            currentStatus = "Comparing Parent " + array[largest] + "with left child " + array[left];
            SortState currentState = new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2);
            publish(currentState);
            Thread.sleep(speed);

            if (array[left] > array[largest]){
                largest = left;
            }
        }

        if (right < n){
            comparisons++;
            comparedIdx1 = largest;
            comparedIdx2 = right;
            interchangedIdx1 = -1;
            interchangedIdx2 = -1;
            currentStatus = "Comparing current max " + array[largest] + "with right child " + array[right];
            SortState currentState = new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2);
            publish(currentState);
            Thread.sleep(speed);

            if (array[right] > array[largest]){
                largest = right;
            }
        }

        if (largest != i){
            interchanges++;
            comparedIdx1 = -1;
            comparedIdx2 = -1;
            interchangedIdx1 = i;
            interchangedIdx2 = largest;
            currentStatus = "Swapping the parent " + array[i] + "with " + array[largest];
            int temp = array[i];
            array[i] = array[largest];
            array[largest] = temp;
            SortState currentState = new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2);
            publish(currentState);
            Thread.sleep(speed);
            heapify(n, largest);
        }
    }

    @Override
    protected void process(List<SortState> chunks){
        SortState state = chunks.getLast();
        sortPanel.updateVisualization(state.array, state.comparisons,
                state.interchanges, state.currentStatus,
                state.interchangedIdx1, state.interchangedIdx2,
                state.comparedIdx1, state.comparedIdx2);
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
