package org.example.algovisual;

import org.example.SortPanel;

import javax.swing.*;
import java.util.List;

public class InsertionSort extends SwingWorker<Void, SortState> {

    private int[] array;
    private SortPanel sortPanel;
    private int comparisons = 0;
    private int interchanges = 0;
    private String currentStatus = "Starting Insertion Sort...";
    private int interchangedIdx1 = -1;
    private int interchangedIdx2 = -1;
    private int comparedIdx1 = -1;
    private int comparedIdx2 = -1;

    private int speed;

    public InsertionSort(int[] array, SortPanel sortPanel, int speed) {
        this.array = array;
        this.sortPanel = sortPanel;
        this.speed = speed;
    }

    @Override
    protected Void doInBackground() throws Exception {
        for (int i = 1; i < array.length; i++) {
            int key = array[i];
            int j = i - 1;
            currentStatus = "Picking key" + key;
            comparedIdx1 = i;
            comparedIdx2 = -1;
            interchangedIdx1 = -1;
            interchangedIdx2 = -1;
            publish(new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2));
            Thread.sleep(speed);

            while (j >= 0){
                comparisons++;
                comparedIdx1 = j;
                comparedIdx2 = j + 1; // key
                interchangedIdx1 = -1;
                interchangedIdx2 = -1;
                currentStatus = "Comparing key: " + key + "with: " + array[j];

                publish(new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2));
                Thread.sleep(speed);
                if (array[j] > key){
                    interchanges++;
                    interchangedIdx1 = j;
                    interchangedIdx2 = j + 1;
                    comparedIdx2 = -1;
                    comparedIdx1 = -1;

                    currentStatus = "Shifting " + key + "to the right";
                    array[j + 1] = array[j];
                    publish(new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2));
                    Thread.sleep(speed);
                    j = j - 1;

                }else {
                    break;
                }
                array[j] = key;
                comparedIdx1 = -1;
                comparedIdx2 = -1;
                interchangedIdx1 = j + 1;
                interchangedIdx2 = -1;
                currentStatus = "Inserted " + key + "at place" + j;
                publish(new SortState(array, comparisons, interchanges, currentStatus, interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2));
                Thread.sleep(speed);
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
