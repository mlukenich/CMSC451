# CMSC 451 Project 2: Analysis of Sorting Algorithm Results

Author: Matthew Lukenich  
Course: CMSC 451  
Reference implementation used: `csmc451-project1/CMSC451_Project1_Submission`

## 1. Brief Introduction of the Sorting Algorithms

The Project 1 implementation benchmarks `BucketSort` and `SelectionSort` on random integer arrays of size `1000` through `12000` (step `1000`), with 40 runs per size.

### 1.1 High-level pseudocode

#### BucketSort (with insertion sort inside each bucket)

```text
BUCKET_SORT(A):
  if |A| <= 1: return
  min, max <- scan A
  if min == max: return

  k <- floor(sqrt(|A|))
  create k empty buckets

  for each value x in A:
    count++
    idx <- floor((x - min) * k / (max - min + 1))
    place x into bucket[idx]

  for each bucket B:
    INSERTION_SORT(B):
      for i from 1 to |B|-1:
        key <- B[i]
        j <- i-1
        while j >= 0:
          count++
          if B[j] > key:
            B[j+1] <- B[j]
            j <- j-1
          else:
            break
        B[j+1] <- key

  concatenate buckets back into A
```

#### SelectionSort

```text
SELECTION_SORT(A):
  n <- |A|
  for i from 0 to n-2:
    min_idx <- i
    for j from i+1 to n-1:
      count++
      if A[j] < A[min_idx]:
        min_idx <- j
    if min_idx != i:
      swap A[i], A[min_idx]
```

### 1.2 Big-Theta analysis

- `SelectionSort`:
  - Critical operation count is deterministic: `n(n-1)/2` comparisons.
  - Time complexity is `Theta(n^2)` in best, average, and worst case.
- `BucketSort` in this implementation:
  - Uses `k = floor(sqrt(n))` buckets.
  - Distribution pass is `Theta(n)`.
  - Sorting buckets with insertion sort is approximately `Theta(sum b_i^2)`.
  - Under approximately uniform distribution, `sum b_i^2` is about `n^2/k`, giving `Theta(n^(3/2))` expected critical-operation growth.
  - Worst case remains `Theta(n^2)` if many values collapse into a small number of buckets.

### 1.3 JVM warm-up approach

The benchmark explicitly warms up each algorithm before measurements:

- Warm-up size: `100`
- Warm-up iterations: `10000`
- Mechanism: repeatedly sort copied arrays before benchmark runs begin

This reduces bias from JIT compilation and startup effects in measured execution times.

### 1.4 Critical operation counted

- `SelectionSort`: each inner-loop comparison `if (list[j] < list[minIdx])`.
  - Reason: comparisons dominate the algorithm and directly map to the known `Theta(n^2)` model.
- `BucketSort`: incremented for
  - each element-to-bucket distribution,
  - each comparison in bucket insertion sort.
  - Reason: these are the core work-driving operations in this implementation.

## 2. Analysis of Study Results

Benchmark outputs were generated from `BenchmarkSorts` in `CMSC451_Project1_Submission` and summarized into `analysis_summary.csv`.

### 2.1 Graphs of critical operations and execution times

#### Linear Scale
Linear graphs show the absolute performance gap between the quadratic growth of SelectionSort and the much more efficient BucketSort.

![Critical operations graph](critical_operations.svg)
![Execution times graph](execution_times.svg)

#### Log-Log Scale
Log-log visualizations are provided to better observe the specific growth trends of each algorithm, especially BucketSort, which otherwise appears near-flat on a linear scale compared to SelectionSort.

![Critical operations log graph](critical_operations_log.svg)
![Execution times log graph](execution_times_log.svg)

### 2.2 Performance comparison

Across all tested sizes, `BucketSort` is substantially faster than `SelectionSort`:

- At `n = 1000`: `176,777.5 / 56,402.5 ~= 3.13x` faster
- At `n = 6000`: `5,040,242.5 / 350,490 ~= 14.38x` faster
- At `n = 12000`: `20,260,940 / 772,872.5 ~= 26.22x` faster

The speedup increases with input size, which is consistent with asymptotically lower growth than `Theta(n^2)`.

### 2.3 Critical operation results vs actual time measurements

- `SelectionSort` critical counts match the exact formula `n(n-1)/2` (coefficient of variation is `0%` at every tested size).
- `SelectionSort` time also follows near-quadratic growth (`R^2` vs `n^2`: `0.9994`).

For `BucketSort`:

- Count growth shows an exceptional match to the $n^{1.5}$ model (`R^2 = 0.9999`), which aligns with the theoretical complexity of $\Theta(n^{1.5})$ when using $k = \sqrt{n}$ buckets and insertion sort.
- Time growth remains efficient across the range, with the $n^{1.5}$ model providing the strongest fit among common polynomial models (`R^2 = 0.9927`), significantly outperforming the quadratic model (`R^2 = 0.9784`).

### 2.4 Significance of coefficient of variance (data sensitivity)

Average CV values:

- `BucketSort` count CV: `0.90%`
- `SelectionSort` count CV: `0.00%`
- `BucketSort` time CV: `21.88%`
- `SelectionSort` time CV: `7.02%`

#### Timing Outliers
A notable spike in Coefficient of Variation for `BucketSort` time occurs at `n = 6000` (CV: `88.81%`). Inspection of the raw data reveals a significant outlier of **2,260,500 ns**, while most other runs at that size are approximately **300,000 ns**. This suggests intermittent system-level interference, such as a JVM Garbage Collection event or background OS task, rather than an algorithmic flaw.

#### Data Sensitivity Interpretation
The results highlight a fundamental difference in how these algorithms interact with data:

- **SelectionSort** is structurally data-insensitive. It performs exactly the same number of comparisons for a given `n` regardless of the initial order or distribution of values. This is reflected in its `0%` count CV.
- **BucketSort** is data-sensitive. Its efficiency relies on an approximately uniform distribution of elements across buckets. If many elements fall into a single bucket, the performance of the internal insertion sort degrades toward $O(n^2)$. Even with random data, slight variations in bucket occupancy lead to the observed non-zero CV in operation counts.

### 2.5 Comparison to Big-Theta analysis

- `SelectionSort` empirical counts and time strongly support `Theta(n^2)`.
- `BucketSort` empirical critical counts support the expected `Theta(n^(3/2))` behavior for this specific `k = sqrt(n)` + insertion-sort bucket design under random input.
- The observed runtime trend for `BucketSort` is better than `SelectionSort` over all tested sizes and remains consistent with the theoretical expectation of lower growth than quadratic in this setup.

### 2.6 Summary table (means and CVs)

| n | Bucket mean count | Bucket CV count (%) | Bucket mean time (ns) | Bucket CV time (%) | Selection mean count | Selection CV count (%) | Selection mean time (ns) | Selection CV time (%) |
|---:|---:|---:|---:|---:|---:|---:|---:|---:|
| 1000 | 9,923.58 | 1.64 | 56,402.50 | 21.35 | 499,500.00 | 0.00 | 176,777.50 | 3.27 |
| 2000 | 26,527.35 | 1.37 | 124,802.50 | 23.01 | 1,999,000.00 | 0.00 | 618,540.00 | 3.46 |
| 3000 | 47,373.15 | 1.16 | 188,055.00 | 14.41 | 4,498,500.00 | 0.00 | 1,349,675.00 | 2.05 |
| 4000 | 71,185.90 | 1.01 | 196,362.50 | 7.64 | 7,998,000.00 | 0.00 | 2,324,697.50 | 2.53 |
| 5000 | 98,994.15 | 1.03 | 264,782.50 | 18.49 | 12,497,500.00 | 0.00 | 3,567,397.50 | 4.25 |
| 6000 | 128,355.45 | 0.91 | 350,490.00 | 88.81 | 17,997,000.00 | 0.00 | 5,040,242.50 | 5.66 |
| 7000 | 161,011.50 | 0.60 | 364,370.00 | 14.49 | 24,496,500.00 | 0.00 | 6,793,427.50 | 9.57 |
| 8000 | 195,231.10 | 0.61 | 434,432.50 | 12.33 | 31,996,000.00 | 0.00 | 8,895,362.50 | 8.88 |
| 9000 | 233,106.05 | 0.68 | 538,950.00 | 37.75 | 40,495,500.00 | 0.00 | 11,188,605.00 | 7.91 |
| 10000 | 269,409.83 | 0.59 | 587,257.50 | 8.93 | 49,995,000.00 | 0.00 | 14,528,415.00 | 13.35 |
| 11000 | 311,640.35 | 0.61 | 675,937.50 | 5.91 | 60,494,500.00 | 0.00 | 16,934,647.50 | 10.36 |
| 12000 | 353,459.03 | 0.57 | 772,872.50 | 9.44 | 71,994,000.00 | 0.00 | 20,260,940.00 | 12.95 |

## 3. Conclusion

The study shows a clear and widening performance gap between the two algorithms. `SelectionSort` behaves exactly as expected for `Theta(n^2)` growth in both critical operations and runtime. `BucketSort`, implemented with `sqrt(n)` buckets and insertion sort per bucket, demonstrates much lower practical cost: critical-operation growth consistent with `Theta(n^(3/2))` and significantly lower execution times at every tested size.

The coefficient-of-variance analysis also highlights that operation counts are more stable than wall-clock timings, and that `BucketSort` runtime is more sensitive to data/runtime conditions than `SelectionSort`. Even with that variability, `BucketSort` remains decisively superior for the tested range.

## Notes

- No source-code revisions were required for Project 1 beyond generating fresh benchmark outputs (`BucketSort.txt`, `SelectionSort.txt`) from the existing submission code.
- Supporting analysis files generated in `Project2`:
  - `analysis_summary.csv`
  - `fit_metrics.txt`
  - `critical_operations.svg`
  - `critical_operations_log.svg`
  - `execution_times.svg`
  - `execution_times_log.svg`
  - `analyze_results.py` (Script used for automated analysis and graph generation)