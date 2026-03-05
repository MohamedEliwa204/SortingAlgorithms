package org.example.algo;

public class PureSelectionSort implements SortAlgorithm{
    @Override
    public String getName() {
        return "Selection Sort";
    }

    @Override
    public SortResult sort(int[] arr) {
        int[] array = arr.clone();
        int comparisons = 0;
        int interchanges = 0;
        long startTime = System.nanoTime();
        int min;
        int temp;
        for (int i = 0; i < array.length; i++) {
            min = array[i];
            int idx = i;
            for (int j = i + 1; j < array.length; j++) {
                comparisons++;
                if (array[j] < min){
                    min = array[j];
                    idx = j;
                }
            }
            if (idx != i){
                interchanges++;
                temp = array[idx];
                array[idx] = array[i];
                array[i] = temp;
            }
        }
        long endTime = System.nanoTime();

        return new SortResult(endTime - startTime, comparisons, interchanges);
    }
}
