
/**
 * File: BenchmarkSorts.java
 * Author: Matthew Lukenich
 * Project: CMSC451 Project 1
 */

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

/**
 * BenchmarkSorts class that benchmarks the sorting algorithms
 */
public class BenchmarkSorts {

    private static final int NUM_RUNS = 40;
    private static final int[] BENCHMARK_SIZES = new int[12];

    static {
        for (int i = 0; i < 12; i++) {
            BENCHMARK_SIZES[i] = (i + 1) * 1000;
        }
    }

    /**
     * Main method to run the benchmark
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        AbstractSort bucketSort = new BucketSort();
        AbstractSort selectionSort = new SelectionSort();

        System.out.println("Starting JVM Warmup...");
        performWarmup(bucketSort);
        performWarmup(selectionSort);
        System.out.println("Warmup complete.");

        try (PrintWriter bucketWriter = new PrintWriter(new FileWriter("BucketSort.txt"));
                PrintWriter selectionWriter = new PrintWriter(new FileWriter("SelectionSort.txt"))) {

            for (int size : BENCHMARK_SIZES) {
                StringBuilder bucketLine = new StringBuilder();
                StringBuilder selectionLine = new StringBuilder();

                bucketLine.append(size);
                selectionLine.append(size);

                for (int i = 0; i < NUM_RUNS; i++) {
                    int[] data = generateRandomData(size);
                    int[] dataCopy = Arrays.copyOf(data, data.length);

                    runAndRecord(bucketSort, data, bucketLine);
                    runAndRecord(selectionSort, dataCopy, selectionLine);
                }

                bucketWriter.println(bucketLine.toString());
                selectionWriter.println(selectionLine.toString());
                System.out.println("Finished benchmark for size: " + size);
            }

            System.out.println("Benchmarking complete. Output files generated.");

        } catch (IOException | UnsortedException e) {
            e.printStackTrace();
        }
    }

    /*
     * Performs warmup for the sorting algorithm
     * 
     * @param sorter The sorting algorithm
     */
    private static void performWarmup(AbstractSort sorter) {
        int warmupSize = 100;
        int iterations = 10000;
        int[] dummy = generateRandomData(warmupSize);

        for (int i = 0; i < iterations; i++) {
            int[] data = Arrays.copyOf(dummy, dummy.length);
            sorter.startSort();
            sorter.sort(data);
            sorter.endSort();
        }
    }

    /*
     * Runs the sorting algorithm and records the results
     * 
     * @param sorter The sorting algorithm
     * 
     * @param data The data to sort
     * 
     * @param line The line to append the results to
     * 
     * @throws UnsortedException If the data is not sorted correctly
     */
    private static void runAndRecord(AbstractSort sorter, int[] data, StringBuilder line) throws UnsortedException {
        sorter.startSort();
        sorter.sort(data);
        sorter.endSort();

        verifySorted(data);

        line.append(" ").append(sorter.getCount()).append(" ").append(sorter.getTime());
    }

    /*
     * Generates random data for the sorting algorithm
     * 
     * @param size The size of the data
     * 
     * @return The random data
     */
    private static int[] generateRandomData(int size) {
        Random rand = new Random();
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = rand.nextInt();
        }
        return data;
    }

    /*
     * Verifies that the data is sorted correctly
     * 
     * @param data The data to verify
     * 
     * @throws UnsortedException If the data is not sorted correctly
     */
    private static void verifySorted(int[] data) throws UnsortedException {
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] > data[i + 1]) {
                throw new UnsortedException("Array not sorted correctly.");
            }
        }
    }
}
