/**
 * File: SelectionSort.java
 * Author: Matthew Lukenich
 * Project: CMSC451 Project 1
 * 
 * SelectionSort class that sorts an array using the selection sort algorithm.
 */
public class SelectionSort extends AbstractSort {

    /**
     * Standard SelectionSort algorithm structure
     * 
     * @param list The list to sort
     */
    @Override
    public void sort(int[] list) {
        if (list == null || list.length <= 1) {
            return;
        }
        selectionSort(list);
    }

    /*
     * Sorts the given list using the selection sort algorithm.
     * Selection sort repeatedly finds the minimum element from the unsorted
     * portion and places it at the beginning.
     * 
     * @param list The list to sort
     */
    private void selectionSort(int[] list) {
        int n = list.length;

        for (int i = 0; i < n - 1; i++) {
            // Find the minimum element in the unsorted portion
            int minIdx = i;
            for (int j = i + 1; j < n; j++) {
                incrementCount();
                if (list[j] < list[minIdx]) {
                    minIdx = j;
                }
            }

            // Swap the found minimum element with the first element of unsorted portion
            if (minIdx != i) {
                int temp = list[minIdx];
                list[minIdx] = list[i];
                list[i] = temp;
            }
        }
    }
}
