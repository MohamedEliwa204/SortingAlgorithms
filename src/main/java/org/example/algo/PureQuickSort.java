package org.example.algo;

import org.example.algovisual.SortState;

import java.util.concurrent.ThreadLocalRandom;

public class PureQuickSort  implements SortAlgorithm{
    int comparisons = 0;
    int interchanges = 0;
    long startTime;
    long endTime;
    int[] array;


    @Override
    public String getName() {
        return "Quick Sort";
    }

    @Override
    public SortResult sort(int[] arr) {
        array = arr.clone();
        comparisons = 0;
        interchanges = 0;
        startTime = System.nanoTime();
        quickSort(0, array.length - 1);
        endTime = System.nanoTime();

        return new SortResult(endTime - startTime, comparisons, interchanges);
    }

    private void quickSort(int start, int end){
        if (end <= start){
            return;
        }
        int pi = partition(start, end);

        quickSort(start, pi - 1);
        quickSort(pi + 1, end);

    }

    private int partition(int start, int end){
        int randomIndex = ThreadLocalRandom.current().nextInt(start, end + 1);
        swap(randomIndex, end);
        int pivot = array[end];
        int i = start - 1;
        for (int j = start; j < end; j++) {
            comparisons++;
            if (array[j] < pivot) {
                i++;
                swap(i, j);

            }
        }
        swap(i + 1, end);
        return i + 1;
    }

    private void swap(int i, int j){
        if (i == j) return;
        interchanges++;
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}
