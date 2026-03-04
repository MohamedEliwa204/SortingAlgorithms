package org.example;

public class SortState {
    public int[] array;
    public int comparisons;
    public int interchanges;
    public String currentStatus;
    public int interchangedIdx1, interchangedIdx2, comparedIdx1, comparedIdx2;
    public SortState(int[] array, int comp, int swap, String status, int s1, int s2, int c1, int c2){
        this.array = array.clone();
        this.comparisons = comp;
        this.interchanges = swap;
        this.interchangedIdx1 = s1;
        this.interchangedIdx2 = s2;
        this.comparedIdx1 = c1;
        this.comparedIdx2 = c2;
        this.currentStatus = status;
    }
}
