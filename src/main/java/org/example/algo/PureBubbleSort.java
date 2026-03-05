package org.example.algo;

public class PureBubbleSort implements SortAlgorithm {

    @Override
    public String getName() {
        return "Bubble Sort";
    }

    @Override
    public SortResult sort(int[] arr) {
        int[] array = arr.clone();
        int interchanges = 0;
        int comparisons = 0;
        int n = array.length;
        boolean swapped;
        int temp;
        long startTime = System.nanoTime();
        for (int i = 0; i < n - 1; i++) {
            swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                comparisons++;
                if (array[j] > array[j + 1]){
                    swapped=true;
                    interchanges++;
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
            if (!swapped){
                break;
            }
        }
        long endTime = System.nanoTime();
        return new SortResult(endTime - startTime, comparisons, interchanges);
    }
}
