/**
 * File: AbstractSort.java
 * Author: Matthew Lukenich
 * Project: CSMC451 Project 1
 * 
 * AbstractSort class that provides a template for sorting algorithms.
 */
public abstract class AbstractSort {
    private long count;
    private long startTime;
    private long time;

    /**
     * Sorts the given list using the specified sorting algorithm.
     * 
     * @param list The list to sort.
     */
    public abstract void sort(int[] list);

    /**
     * Starts the sorting process.
     */
    protected void startSort() {
        count = 0;
        startTime = System.nanoTime();
    }

    /**
     * Ends the sorting process.
     */
    protected void endSort() {
        time = System.nanoTime() - startTime;
    }

    /**
     * Increments the count.
     */
    protected void incrementCount() {
        count++;
    }

    /**
     * Gets the count.
     * 
     * @return The count.
     */
    public long getCount() {
        return count;
    }

    /**
     * Gets the time it took to sort the list.
     * 
     * @return The time it took to sort the list.
     */
    public long getTime() {
        return time;
    }
}
