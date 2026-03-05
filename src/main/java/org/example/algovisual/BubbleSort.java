package org.example.algovisual;

import org.example.control.SortPanel;

import javax.swing.*;
import java.util.List;

public class BubbleSort extends SwingWorker<Void, SortState> {

    private int[] array;
    private SortPanel sortPanel;
    private int comparisons = 0;
    private int interchanges = 0;
    private String currentStatus = "Starting Bubble Sort...";
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
                publish(new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2));
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
                    publish(new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2));
                    Thread.sleep(speed);

                }
            }
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
    protected void done(){
        try {
            get();
            sortPanel.updateVisualization(array, comparisons, interchanges, "Sorting complete!", -1, -1, -1, -1);


        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
