package org.example.algo;

import org.example.algovisual.SortState;

public class PureMergeSort implements SortAlgorithm{
    int comparisons = 0;
    int interchanges = 0;
    long startTime;
    long endTime;
    int[] array;
    @Override
    public String getName() {
        return "Merge Sort";
    }

    @Override
    public SortResult sort(int[] arr) {
        array = arr.clone();
        comparisons = 0;
        interchanges = 0;
        startTime = System.nanoTime();
        mergeSort(0, array.length - 1);
        endTime = System.nanoTime();
        return new SortResult(endTime - startTime, comparisons, interchanges);
    }
    
    private void mergeSort(int start, int end){
        if (end <= start){
            return;
        }
        int mid = start + (end - start) / 2;
        mergeSort(start, mid);
        mergeSort(mid + 1, end);
        merge(start, mid, end);
    }
    
    private void merge(int start, int mid, int end){
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
            if (leftArr[i] <= rightArr[j]){
                array[k] = leftArr[i];
                i++;
            }else {
                array[k] = rightArr[j];
                j++;
            }
            k++;
            interchanges++;


        }
        while (i < n1){
            array[k++] =leftArr[i++];
            interchanges++;


        }
        while (j < n2){
            array[k++] =rightArr[j++];
            interchanges++;

        }
        
    }
}
