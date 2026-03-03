
/**
 * File: BucketSort.java
 * Author: Matthew Lukenich
 * Project: CMSC451 Project 1
 * 
 * BucketSort class that sorts an array using the bucket sort algorithm.
 */
import java.util.ArrayList;
import java.util.List;

/**
 * BucketSort class that sorts an array using the bucket sort algorithm.
 */
public class BucketSort extends AbstractSort {

    /**
     * Standard BucketSort algorithm structure adapted for integer arrays
     * 
     * @param list The list to sort
     */
    @Override
    public void sort(int[] list) {
        if (list == null || list.length <= 1) {
            return;
        }
        bucketSort(list);
    }

    /*
     * Sorts the given list using the bucket sort algorithm
     * Elements are distributed into buckets based on their value range
     * each bucket is sorted using insertion sort, then buckets are concatenated
     * 
     * @param list The list to sort
     */
    private void bucketSort(int[] list) {
        int n = list.length;

        // Find min and max values to determine bucket range
        int minValue = list[0];
        int maxValue = list[0];
        for (int i = 1; i < n; i++) {
            if (list[i] < minValue) {
                minValue = list[i];
            }
            if (list[i] > maxValue) {
                maxValue = list[i];
            }
        }

        // Handle edge case where all elements are the same
        if (minValue == maxValue) {
            return;
        }

        // Create buckets - use square root of n as number of buckets
        int numBuckets = (int) Math.sqrt(n);
        if (numBuckets < 1) {
            numBuckets = 1;
        }

        List<List<Integer>> buckets = new ArrayList<>(numBuckets);
        for (int i = 0; i < numBuckets; i++) {
            buckets.add(new ArrayList<>());
        }

        // Calculate range for each bucket using long to avoid overflow
        long valueRange = (long) maxValue - (long) minValue + 1;

        // Distribute elements into buckets
        for (int i = 0; i < n; i++) {
            incrementCount();
            // Use long to avoid overflow issues
            int bucketIndex = (int) (((long) list[i] - minValue) * numBuckets / valueRange);
            // Ensure bucket index is within valid range
            if (bucketIndex < 0) {
                bucketIndex = 0;
            } else if (bucketIndex >= numBuckets) {
                bucketIndex = numBuckets - 1;
            }
            buckets.get(bucketIndex).add(list[i]);
        }

        // Sort each bucket using insertion sort and count operations
        for (List<Integer> bucket : buckets) {
            insertionSort(bucket);
        }

        // Concatenate all buckets back into original array
        int index = 0;
        for (List<Integer> bucket : buckets) {
            for (int value : bucket) {
                list[index++] = value;
            }
        }
    }

    /*
     * Sorts a bucket using insertion sort
     * 
     * @param bucket The bucket to sort
     */
    private void insertionSort(List<Integer> bucket) {
        for (int i = 1; i < bucket.size(); i++) {
            int key = bucket.get(i);
            int j = i - 1;

            while (j >= 0) {
                incrementCount();
                if (bucket.get(j) > key) {
                    bucket.set(j + 1, bucket.get(j));
                    j--;
                } else {
                    break;
                }
            }
            bucket.set(j + 1, key);
        }
    }
}
