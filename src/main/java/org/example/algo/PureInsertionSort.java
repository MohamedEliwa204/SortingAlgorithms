package org.example.algo;

public class PureInsertionSort implements SortAlgorithm{
    @Override
    public String getName() {
        return "Insertion Sort";
    }



    @Override
    public SortResult sort(int[] arr) {
        int[] array = arr.clone();
        int comparisons = 0;
        int interchanges = 0;
        int key;
        int j;
        int temp;
        long startTime = System.nanoTime();
        for (int i = 1; i < array.length; i++) {
            key = array[i];
            j = i - 1;
            while (j >= 0){
                comparisons++;
                if(array[j] > key){
                    interchanges++;
                    array[j + 1] = array[j];
                    j--;
                }
                else {
                    break;
                }

            }
            array[j + 1] = key;
        }
        long endTime = System.nanoTime();
        return new SortResult(startTime - endTime, comparisons, interchanges);
    }
}
