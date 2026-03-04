package org.example;

import javax.swing.*;
import java.util.List;

public class MergeSort extends SwingWorker<Void, int[]> {
    private int[] array;
    private SortPanel sortPanel;
    private int comparisons = 0;
    private int interchanges = 0;
    private String currentStatus = "Starting Merge Sort...";
    private int interchangedIdx1 = -1;
    private int interchangedIdx2 = -1;
    private int comparedIdx1 = -1;
    private int comparedIdx2 = -1;

    private int speed;

    public MergeSort(int[] array, SortPanel sortPanel, int speed) {
        this.array = array;
        this.sortPanel = sortPanel;
        this.speed = speed;
    }
    @Override
    protected Void doInBackground() throws Exception {
        mergeSort(0, array.length - 1);
        return null;
    }

    private void mergeSort(int start, int end) throws Exception{
        if (end < start){
            return;
        }
        int mid = start + (end - start) / 2;
        mergeSort(start, mid);
        mergeSort(mid + 1, end);

        merge(start, mid, end);

    }
    private void merge(int start, int mid, int end) throws Exception{
        int n1 = mid - start + 1;
        int n2 = end - mid;
        int[] leftArr = new int[n1];
        int[] rightArr = new int[n2];
        for (int i = 0; i < n1; i++) {
            leftArr[i] = array[start + i];
        }
        for (int i = 0; i < n2; i++) {
            rightArr[i] = array[mid + i + 1];
        }
        int i = 0;
        int j = 0;
        int k = start;

        while (i < n1 && j < n2){
            comparisons++;
            comparedIdx1 = start + i;
            comparedIdx2 = mid + j + 1;
            interchangedIdx1 = -1;
            interchangedIdx2 = -1;
            currentStatus = "Comparing " + leftArr[i] + "with" +rightArr[j];
            publish(array.clone());
            Thread.sleep(speed);
            if (leftArr[i] <= rightArr[j]){
                array[k] = leftArr[i];
                i++;
            }else {
                array[k] = rightArr[j];
                j++;
            }
            k++;
            interchanges++;
            comparedIdx2 = -1;
            comparedIdx1 = -1;
            interchangedIdx1 = k;
            interchangedIdx2 = -1;
            currentStatus = "Placing " + array[k] + "at index" + k;
            publish(array.clone());
            Thread.sleep(speed);

        }
        while (i < n1){
            array[k++] =leftArr[i++];
            interchanges++;
            interchanges++;
            comparedIdx2 = -1;
            comparedIdx1 = -1;
            interchangedIdx1 = k;
            interchangedIdx2 = -1;
            currentStatus = "Placing " + array[k] + "at index" + k;
            publish(array.clone());
            Thread.sleep(speed);
        }
        while (j < n2){
            array[k++] =leftArr[j++];
            interchanges++;
            interchanges++;
            comparedIdx2 = -1;
            comparedIdx1 = -1;
            interchangedIdx1 = k;
            interchangedIdx2 = -1;
            currentStatus = "Placing " + array[k] + "at index" + k;
            publish(array.clone());
            Thread.sleep(speed);
        }
    }

    @Override
    protected void process(List<int[]> chunks){
        int[] arr = chunks.getLast();
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
