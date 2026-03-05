package org.example.algo;

public class SortResult {
    public long runTimeNano;
    public long comparisons;
    public long interchanges;

    public SortResult(long runTimeNano, long comparisons, long interchanges) {
        this.runTimeNano = runTimeNano;
        this.comparisons = comparisons;
        this.interchanges = interchanges;
    }

}
