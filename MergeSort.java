
public class MergeSort extends AbstractSort {

    // Source: Standard MergeSort algorithm structure.

    @Override
    public void sort(int[] list) {
        if (list == null || list.length <= 1) {
            return;
        }
        mergeSort(list, 0, list.length - 1);
    }

    private void mergeSort(int[] list, int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;

            mergeSort(list, l, m);
            mergeSort(list, m + 1, r);

            merge(list, l, m, r);
        }
    }

    private void merge(int[] list, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int[] L = new int[n1];
        int[] R = new int[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = list[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = list[m + 1 + j];

        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            // Critical operation: Comparison L[i] <= R[j]
            incrementCount();
            if (L[i] <= R[j]) {
                list[k] = L[i];
                i++;
            } else {
                list[k] = R[j];
                j++;
            }
            k++;
        }

        while (i < n1) {
            list[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            list[k] = R[j];
            j++;
            k++;
        }
    }
}
