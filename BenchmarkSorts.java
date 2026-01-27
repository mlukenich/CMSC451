
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class BenchmarkSorts {

    private static final int NUM_RUNS = 40;
    private static final int[] BENCHMARK_SIZES = new int[12];

    static {
        // Sizes: 1000, 2000, ..., 12000
        for (int i = 0; i < 12; i++) {
            BENCHMARK_SIZES[i] = (i + 1) * 1000;
        }
    }

    public static void main(String[] args) {
        AbstractSort quickSort = new QuickSort();
        AbstractSort mergeSort = new MergeSort();

        System.out.println("Starting JVM Warmup...");
        performWarmup(quickSort);
        performWarmup(mergeSort);
        System.out.println("Warmup complete.");

        try (PrintWriter quickWriter = new PrintWriter(new FileWriter("QuickSort.txt"));
             PrintWriter mergeWriter = new PrintWriter(new FileWriter("MergeSort.txt"))) {

            for (int size : BENCHMARK_SIZES) {
                StringBuilder quickLine = new StringBuilder();
                StringBuilder mergeLine = new StringBuilder();

                quickLine.append(size);
                mergeLine.append(size);

                for (int i = 0; i < NUM_RUNS; i++) {
                    int[] data = generateRandomData(size);
                    int[] dataCopy = Arrays.copyOf(data, data.length);

                    // QuickSort
                    runAndRecord(quickSort, data, quickLine);

                    // MergeSort
                    runAndRecord(mergeSort, dataCopy, mergeLine);
                }

                quickWriter.println(quickLine.toString());
                mergeWriter.println(mergeLine.toString());
                System.out.println("Finished benchmark for size: " + size);
            }

            System.out.println("Benchmarking complete. Output files generated.");

        } catch (IOException | UnsortedException e) {
            e.printStackTrace();
        }
    }

    private static void performWarmup(AbstractSort sorter) {
        // Run enough times to trigger JIT.
        // We use a small size to keep it fast, but enough iterations.
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

    private static void runAndRecord(AbstractSort sorter, int[] data, StringBuilder line) throws UnsortedException {
        sorter.startSort();
        sorter.sort(data);
        sorter.endSort();

        verifySorted(data);

        line.append(" ").append(sorter.getCount()).append(" ").append(sorter.getTime());
    }

    private static int[] generateRandomData(int size) {
        Random rand = new Random();
        int[] data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = rand.nextInt();
        }
        return data;
    }

    private static void verifySorted(int[] data) throws UnsortedException {
        for (int i = 0; i < data.length - 1; i++) {
            if (data[i] > data[i + 1]) {
                throw new UnsortedException("Array not sorted correctly.");
            }
        }
    }
}
