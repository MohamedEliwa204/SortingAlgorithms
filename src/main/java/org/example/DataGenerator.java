package org.example;

import java.util.Arrays;
import java.util.Random;

public class DataGenerator {
    public static int[] generateRandom(int size){
        int[] arr = new int[size];
        Random rand = new Random();
        for (int i = 0; i < size; i++) {
            arr[i] = rand.nextInt(1000) + 1;
        }
        return arr;
    }

    public static int[] generateSorted(int size){
        int[] arr = generateRandom(size);
        Arrays.sort(arr);
        return arr;

    }

    public static int[] generateReversed(int size){
        int[] arr = generateSorted(size);
        int[] reversed = new int[size];
        for (int i = 0; i < size; i++) {
            reversed[i] = arr[size - 1 - i];
        }
        return reversed;
    }
}
