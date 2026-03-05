package org.example.algo;

import org.example.algovisual.SortState;

public class PureHeapSort implements SortAlgorithm{

    int comparisons = 0;
    int interchanges = 0;
    long startTime;
    long endTime;
    int[] array;


    @Override
    public String getName() {
        return "Heap Sort";
    }

    @Override
    public SortResult sort(int[] arr) {
        array = arr.clone();
        interchanges = 0;
        comparisons = 0;
        startTime = System.nanoTime();
        int n = array.length;
        for (int i = n / 2 - 1; i >= 0 ; i--) {
            heapify(n, i);
        }

        for (int i =  n - 1; i > 0 ; i--) {
            interchanges++;
            int temp = array[0];
            array[0] = array[i];
            array[i] = temp;
            heapify(i, 0);
        }
        endTime = System.nanoTime();
        return new SortResult(endTime - startTime, comparisons, interchanges);
    }
    
    private void heapify(int n, int i){
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * (i + 1);

        if (left < n){
            comparisons++;

            if (array[left] > array[largest]){
                largest = left;
            }
        }

        if (right < n){
            comparisons++;
            if (array[right] > array[largest]){
                largest = right;
            }
        }

        if (largest != i){
            interchanges++;

            int temp = array[i];
            array[i] = array[largest];
            array[largest] = temp;

            heapify(n, largest);
        }
    }
}
