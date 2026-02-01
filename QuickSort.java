/**
 * File: QuickSort.java
 * Author: Matthew Lukenich
 * Project: CSMC451 Project 1
 * 
 * QuickSort class that sorts an array using the quick sort algorithm.
 */
public class QuickSort extends AbstractSort {

    /*
     * Source: Standard QuickSort algorithm structure adapted from
     * "Introduction to Algorithms", Cormen, Leiserson, Rivest, Stein.
     */
    @Override
    public void sort(int[] list) {
        if (list == null || list.length == 0) {
            return;
        }
        quickSort(list, 0, list.length - 1);
    }

    /*
     * Sorts the given list using the quick sort algorithm.
     * 
     * @param list The list to sort.
     * 
     * @param low The left index.
     * 
     * @param high The right index.
     */
    private void quickSort(int[] list, int low, int high) {
        if (low < high) {
            int pi = partition(list, low, high);
            quickSort(list, low, pi - 1);
            quickSort(list, pi + 1, high);
        }
    }

    /*
     * Partitions the given list using the quick sort algorithm.
     * 
     * @param list The list to partition.
     * 
     * @param low The left index.
     * 
     * @param high The right index.
     * 
     * @return The pivot index.
     */
    private int partition(int[] list, int low, int high) {
        int pivot = list[high];
        int i = (low - 1);
        for (int j = low; j < high; j++) {
            // Critical operation: Comparison list[j] < pivot
            incrementCount();
            if (list[j] < pivot) {
                i++;
                // Swap list[i] and list[j]
                int temp = list[i];
                list[i] = list[j];
                list[j] = temp;
            }
        }
        // Swap list[i + 1] and list[high] (or pivot)
        int temp = list[i + 1];
        list[i + 1] = list[high];
        list[high] = temp;

        return i + 1;
    }
}
