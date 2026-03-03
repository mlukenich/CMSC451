# Project 2 Consolidated Report (Embedded SVG Edition)

This file is auto-generated from `Project2_Consolidated_Report.md` and has all SVG graph images embedded inline for single-file submission.

---

# CMSC 451 — Project 2 Consolidated Report

**Author:** Matthew Lukenich  
**Course:** CMSC 451  
**Algorithms analyzed:** BucketSort and SelectionSort  
**Reference implementation:** `CMSC451_Project1_Submission`

---

## 1) Assignment-Coverage Checklist

This single document consolidates the Project 2 deliverable requirements and the analysis.

| Project 2 Requirement | Covered In |
|---|---|
| Brief introduction of both sorting algorithms | Sections 2.1 and 2.2 |
| High-level pseudocode | Sections 2.1 and 2.2 |
| Big-Θ analysis | Section 2.3 |
| JVM warm-up approach | Section 3.2 |
| Critical operation definitions and rationale | Section 3.3 |
| Graphs of critical operations and execution times | Section 4.1 |
| Performance comparison | Section 4.2 |
| Critical operations vs measured time | Section 4.3 |
| Coefficient of variance significance and data sensitivity | Section 4.4 |
| Comparison to Big-Θ expectations | Section 4.5 |
| Conclusion | Section 5 |
| Revised code (if needed) | `Project2/analyze_results.py` (portable path + validation fixes) |

---

## 2) Sorting Algorithms Overview

### 2.1 BucketSort (integer range bucketing + insertion sort in buckets)

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

### 2.2 SelectionSort

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

### 2.3 Big-Θ analysis

- **SelectionSort**
  - Comparison count is deterministic: `n(n-1)/2`.
  - Runtime is `Θ(n²)` in best/average/worst case.
- **BucketSort in this implementation**
  - Uses `k = floor(sqrt(n))` buckets.
  - Distribution phase: `Θ(n)`.
  - Intra-bucket insertion sorting cost: `Θ(sum(b_i²))`.
  - Under roughly uniform random distribution, `sum(b_i²) ≈ n²/k`, so expected growth is `Θ(n^(3/2))`.
  - Worst case remains `Θ(n²)` if values cluster heavily.

---

## 3) Experiment and Measurement Methodology

### 3.1 Benchmark setup

- Input sizes: `1000` to `12000` (step `1000`)
- Runs per size: `40`
- Data: random Java `int` values
- Algorithms benchmarked on identical per-run inputs (copied arrays)
- Sortedness verified after every run

### 3.2 JVM warm-up strategy

Warm-up was explicitly performed before recording benchmark results:

- warm-up array size: `100`
- warm-up iterations: `10,000`
- both algorithms warmed separately

This reduces timing bias from JVM startup and JIT compilation.

### 3.3 Critical operations counted

- **SelectionSort:** one increment per inner-loop comparison (`if list[j] < list[minIdx]`)
- **BucketSort:**
  - one increment per distribution placement into a bucket
  - one increment per insertion-sort comparison inside a bucket

These choices track the dominant work for each implementation.

---

## 4) Results and Analysis

### 4.1 Required graphs

#### Linear scale


<figure>
<svg xmlns="http://www.w3.org/2000/svg" width="900" height="520" viewBox="0 0 900 520">
<rect width="100%" height="100%" fill="white"/>
<text x="450.0" y="35" text-anchor="middle" font-size="22" font-family="Arial" font-weight="bold">Critical Operations vs Input Size</text>
<line x1="100" y1="440" x2="750" y2="440" stroke="black" stroke-width="2"/>
<line x1="100" y1="60" x2="100" y2="440" stroke="black" stroke-width="2"/>
<text x="450.0" y="500" text-anchor="middle" font-size="14" font-family="Arial">Input Size (n)</text>
<text x="30" y="260.0" transform="rotate(-90,30,260.0)" text-anchor="middle" font-size="14" font-family="Arial">Count</text>
<polyline points="154.16666666666666,439.94762121148983 208.33333333333331,439.8599828735728 262.5,439.74995420451705 316.66666666666663,439.6242653276662 370.83333333333337,439.4774873322777 425.0,439.32251200100006 479.1666666666667,439.1501462621885 533.3333333333333,438.96952776620276 587.5,438.7696155374059 641.6666666666667,438.5779963121927 695.8333333333333,438.35509441064534 750.0,438.1343663430286" fill="none" stroke="#1f77b4" stroke-width="3"/>
<circle cx="154.16666666666666" cy="439.94762121148983" r="4" fill="#1f77b4"/>
<circle cx="208.33333333333331" cy="439.8599828735728" r="4" fill="#1f77b4"/>
<circle cx="262.5" cy="439.74995420451705" r="4" fill="#1f77b4"/>
<circle cx="316.66666666666663" cy="439.6242653276662" r="4" fill="#1f77b4"/>
<circle cx="370.83333333333337" cy="439.4774873322777" r="4" fill="#1f77b4"/>
<circle cx="425.0" cy="439.32251200100006" r="4" fill="#1f77b4"/>
<circle cx="479.1666666666667" cy="439.1501462621885" r="4" fill="#1f77b4"/>
<circle cx="533.3333333333333" cy="438.96952776620276" r="4" fill="#1f77b4"/>
<circle cx="587.5" cy="438.7696155374059" r="4" fill="#1f77b4"/>
<circle cx="641.6666666666667" cy="438.5779963121927" r="4" fill="#1f77b4"/>
<circle cx="695.8333333333333" cy="438.35509441064534" r="4" fill="#1f77b4"/>
<circle cx="750.0" cy="438.1343663430286" r="4" fill="#1f77b4"/>
<line x1="770" y1="60" x2="795" y2="60" stroke="#1f77b4" stroke-width="3"/>
<text x="800" y="65" font-size="14" font-family="Arial">BucketSort</text>
<polyline points="154.16666666666666,437.3635302941912 208.33333333333331,429.4488429591355 262.5,416.2559379948329 316.66666666666663,397.7848154012834 370.83333333333337,374.0354751784871 425.0,345.0079173264439 479.1666666666667,310.70214184515373 533.3333333333333,271.11814873461674 587.5,226.25593799483292 641.6666666666667,176.11550962580213 695.8333333333333,120.69686362752452 750.0,60.0" fill="none" stroke="#d62728" stroke-width="3"/>
<circle cx="154.16666666666666" cy="437.3635302941912" r="4" fill="#d62728"/>
<circle cx="208.33333333333331" cy="429.4488429591355" r="4" fill="#d62728"/>
<circle cx="262.5" cy="416.2559379948329" r="4" fill="#d62728"/>
<circle cx="316.66666666666663" cy="397.7848154012834" r="4" fill="#d62728"/>
<circle cx="370.83333333333337" cy="374.0354751784871" r="4" fill="#d62728"/>
<circle cx="425.0" cy="345.0079173264439" r="4" fill="#d62728"/>
<circle cx="479.1666666666667" cy="310.70214184515373" r="4" fill="#d62728"/>
<circle cx="533.3333333333333" cy="271.11814873461674" r="4" fill="#d62728"/>
<circle cx="587.5" cy="226.25593799483292" r="4" fill="#d62728"/>
<circle cx="641.6666666666667" cy="176.11550962580213" r="4" fill="#d62728"/>
<circle cx="695.8333333333333" cy="120.69686362752452" r="4" fill="#d62728"/>
<circle cx="750.0" cy="60.0" r="4" fill="#d62728"/>
<line x1="770" y1="90" x2="795" y2="90" stroke="#d62728" stroke-width="3"/>
<text x="800" y="95" font-size="14" font-family="Arial">SelectionSort</text>
</svg>
<figcaption><em>Critical operations (linear)</em></figcaption>
</figure>


<figure>
<svg xmlns="http://www.w3.org/2000/svg" width="900" height="520" viewBox="0 0 900 520">
<rect width="100%" height="100%" fill="white"/>
<text x="450.0" y="35" text-anchor="middle" font-size="22" font-family="Arial" font-weight="bold">Execution Times vs Input Size</text>
<line x1="100" y1="440" x2="750" y2="440" stroke="black" stroke-width="2"/>
<line x1="100" y1="60" x2="100" y2="440" stroke="black" stroke-width="2"/>
<text x="450.0" y="500" text-anchor="middle" font-size="14" font-family="Arial">Input Size (n)</text>
<text x="30" y="260.0" transform="rotate(-90,30,260.0)" text-anchor="middle" font-size="14" font-family="Arial">Time (ns)</text>
<polyline points="154.16666666666666,438.94215421397035 208.33333333333331,437.65929172091717 262.5,436.47297213258616 316.66666666666663,436.317162481109 370.83333333333337,435.03392488206373 425.0,433.42645504107907 479.1666666666667,433.1661314825472 533.3333333333333,431.85208830389905 587.5,429.89183127732474 641.6666666666667,428.98580964160595 695.8333333333333,427.32258967254234 750.0,425.50454470523084" fill="none" stroke="#1f77b4" stroke-width="3"/>
<circle cx="154.16666666666666" cy="438.94215421397035" r="4" fill="#1f77b4"/>
<circle cx="208.33333333333331" cy="437.65929172091717" r="4" fill="#1f77b4"/>
<circle cx="262.5" cy="436.47297213258616" r="4" fill="#1f77b4"/>
<circle cx="316.66666666666663" cy="436.317162481109" r="4" fill="#1f77b4"/>
<circle cx="370.83333333333337" cy="435.03392488206373" r="4" fill="#1f77b4"/>
<circle cx="425.0" cy="433.42645504107907" r="4" fill="#1f77b4"/>
<circle cx="479.1666666666667" cy="433.1661314825472" r="4" fill="#1f77b4"/>
<circle cx="533.3333333333333" cy="431.85208830389905" r="4" fill="#1f77b4"/>
<circle cx="587.5" cy="429.89183127732474" r="4" fill="#1f77b4"/>
<circle cx="641.6666666666667" cy="428.98580964160595" r="4" fill="#1f77b4"/>
<circle cx="695.8333333333333" cy="427.32258967254234" r="4" fill="#1f77b4"/>
<circle cx="750.0" cy="425.50454470523084" r="4" fill="#1f77b4"/>
<line x1="770" y1="60" x2="795" y2="60" stroke="#1f77b4" stroke-width="3"/>
<text x="800" y="65" font-size="14" font-family="Arial">BucketSort</text>
<polyline points="154.16666666666666,436.6844850238933 208.33333333333331,428.39909698168003 262.5,414.68644100421795 316.66666666666663,396.3996018940878 370.83333333333337,373.09239107366193 425.0,345.468741825404 479.1666666666667,312.5872318855887 533.3333333333333,273.16481120816707 587.5,230.15436105136286 641.6666666666667,167.51522387411444 695.8333333333333,122.38561241482375 750.0,60.0" fill="none" stroke="#d62728" stroke-width="3"/>
<circle cx="154.16666666666666" cy="436.6844850238933" r="4" fill="#d62728"/>
<circle cx="208.33333333333331" cy="428.39909698168003" r="4" fill="#d62728"/>
<circle cx="262.5" cy="414.68644100421795" r="4" fill="#d62728"/>
<circle cx="316.66666666666663" cy="396.3996018940878" r="4" fill="#d62728"/>
<circle cx="370.83333333333337" cy="373.09239107366193" r="4" fill="#d62728"/>
<circle cx="425.0" cy="345.468741825404" r="4" fill="#d62728"/>
<circle cx="479.1666666666667" cy="312.5872318855887" r="4" fill="#d62728"/>
<circle cx="533.3333333333333" cy="273.16481120816707" r="4" fill="#d62728"/>
<circle cx="587.5" cy="230.15436105136286" r="4" fill="#d62728"/>
<circle cx="641.6666666666667" cy="167.51522387411444" r="4" fill="#d62728"/>
<circle cx="695.8333333333333" cy="122.38561241482375" r="4" fill="#d62728"/>
<circle cx="750.0" cy="60.0" r="4" fill="#d62728"/>
<line x1="770" y1="90" x2="795" y2="90" stroke="#d62728" stroke-width="3"/>
<text x="800" y="95" font-size="14" font-family="Arial">SelectionSort</text>
</svg>
<figcaption><em>Execution times (linear)</em></figcaption>
</figure>


#### Log-log scale


<figure>
<svg xmlns="http://www.w3.org/2000/svg" width="900" height="520" viewBox="0 0 900 520">
<rect width="100%" height="100%" fill="white"/>
<text x="450.0" y="35" text-anchor="middle" font-size="22" font-family="Arial" font-weight="bold">Critical Operations vs Input Size (Log-Log)</text>
<line x1="100" y1="440" x2="750" y2="440" stroke="black" stroke-width="2"/>
<line x1="100" y1="60" x2="100" y2="440" stroke="black" stroke-width="2"/>
<text x="450.0" y="500" text-anchor="middle" font-size="14" font-family="Arial">Input Size (log n)</text>
<text x="30" y="260.0" transform="rotate(-90,30,260.0)" text-anchor="middle" font-size="14" font-family="Arial">Count (log)</text>
<polyline points="100.0,440.0 281.3129146732344,397.96803938651635 387.3741706535312,373.1796923111667 462.6258293464688,355.77126813441475 520.9955505456978,341.67462188143816 568.6870853267653,330.5712947099218 609.009703440515,320.8816328584203 643.9387440197032,312.64385423845306 674.7483413070622,305.06432945294625 702.3084652189323,298.87705753738135 727.2396298878734,292.65234604853924 750.0,287.26968601054614" fill="none" stroke="#1f77b4" stroke-width="3"/>
<circle cx="100.0" cy="440.0" r="4" fill="#1f77b4"/>
<circle cx="281.3129146732344" cy="397.96803938651635" r="4" fill="#1f77b4"/>
<circle cx="387.3741706535312" cy="373.1796923111667" r="4" fill="#1f77b4"/>
<circle cx="462.6258293464688" cy="355.77126813441475" r="4" fill="#1f77b4"/>
<circle cx="520.9955505456978" cy="341.67462188143816" r="4" fill="#1f77b4"/>
<circle cx="568.6870853267653" cy="330.5712947099218" r="4" fill="#1f77b4"/>
<circle cx="609.009703440515" cy="320.8816328584203" r="4" fill="#1f77b4"/>
<circle cx="643.9387440197032" cy="312.64385423845306" r="4" fill="#1f77b4"/>
<circle cx="674.7483413070622" cy="305.06432945294625" r="4" fill="#1f77b4"/>
<circle cx="702.3084652189323" cy="298.87705753738135" r="4" fill="#1f77b4"/>
<circle cx="727.2396298878734" cy="292.65234604853924" r="4" fill="#1f77b4"/>
<circle cx="750.0" cy="287.26968601054614" r="4" fill="#1f77b4"/>
<line x1="770" y1="60" x2="795" y2="60" stroke="#1f77b4" stroke-width="3"/>
<text x="800" y="65" font-size="14" font-family="Arial">BucketSort</text>
<polyline points="100.0,272.4859160018024 281.3129146732344,213.20401527107236 387.3741706535312,178.5317110358951 462.6258293464688,153.932813429637 520.9955505456978,134.8530521767252 568.6870853267653,119.26407370720648 609.009703440515,106.08396722815803 643.9387440197032,94.66695802307584 674.7483413070622,84.59652182559466 702.3084652189323,75.5882658165068 727.2396298878734,67.43934808796524 750.0,60.0" fill="none" stroke="#d62728" stroke-width="3"/>
<circle cx="100.0" cy="272.4859160018024" r="4" fill="#d62728"/>
<circle cx="281.3129146732344" cy="213.20401527107236" r="4" fill="#d62728"/>
<circle cx="387.3741706535312" cy="178.5317110358951" r="4" fill="#d62728"/>
<circle cx="462.6258293464688" cy="153.932813429637" r="4" fill="#d62728"/>
<circle cx="520.9955505456978" cy="134.8530521767252" r="4" fill="#d62728"/>
<circle cx="568.6870853267653" cy="119.26407370720648" r="4" fill="#d62728"/>
<circle cx="609.009703440515" cy="106.08396722815803" r="4" fill="#d62728"/>
<circle cx="643.9387440197032" cy="94.66695802307584" r="4" fill="#d62728"/>
<circle cx="674.7483413070622" cy="84.59652182559466" r="4" fill="#d62728"/>
<circle cx="702.3084652189323" cy="75.5882658165068" r="4" fill="#d62728"/>
<circle cx="727.2396298878734" cy="67.43934808796524" r="4" fill="#d62728"/>
<circle cx="750.0" cy="60.0" r="4" fill="#d62728"/>
<line x1="770" y1="90" x2="795" y2="90" stroke="#d62728" stroke-width="3"/>
<text x="800" y="95" font-size="14" font-family="Arial">SelectionSort</text>
</svg>
<figcaption><em>Critical operations (log-log)</em></figcaption>
</figure>


<figure>
<svg xmlns="http://www.w3.org/2000/svg" width="900" height="520" viewBox="0 0 900 520">
<rect width="100%" height="100%" fill="white"/>
<text x="450.0" y="35" text-anchor="middle" font-size="22" font-family="Arial" font-weight="bold">Execution Times vs Input Size (Log-Log)</text>
<line x1="100" y1="440" x2="750" y2="440" stroke="black" stroke-width="2"/>
<line x1="100" y1="60" x2="100" y2="440" stroke="black" stroke-width="2"/>
<text x="450.0" y="500" text-anchor="middle" font-size="14" font-family="Arial">Input Size (log n)</text>
<text x="30" y="260.0" transform="rotate(-90,30,260.0)" text-anchor="middle" font-size="14" font-family="Arial">Time (log ns)</text>
<polyline points="100.0,440.0 281.3129146732344,388.70726225661764 387.3741706535312,362.2282631298917 462.6258293464688,359.436488073466 520.9955505456978,340.129755609146 568.6870853267653,322.0192773277246 609.009703440515,319.5110398359027 643.9387440197032,308.15284764283246 674.7483413070622,294.22998474765654 702.3084652189323,288.68617506444775 727.2396298878734,279.6034559750708 750.0,270.9485155901084" fill="none" stroke="#1f77b4" stroke-width="3"/>
<circle cx="100.0" cy="440.0" r="4" fill="#1f77b4"/>
<circle cx="281.3129146732344" cy="388.70726225661764" r="4" fill="#1f77b4"/>
<circle cx="387.3741706535312" cy="362.2282631298917" r="4" fill="#1f77b4"/>
<circle cx="462.6258293464688" cy="359.436488073466" r="4" fill="#1f77b4"/>
<circle cx="520.9955505456978" cy="340.129755609146" r="4" fill="#1f77b4"/>
<circle cx="568.6870853267653" cy="322.0192773277246" r="4" fill="#1f77b4"/>
<circle cx="609.009703440515" cy="319.5110398359027" r="4" fill="#1f77b4"/>
<circle cx="643.9387440197032" cy="308.15284764283246" r="4" fill="#1f77b4"/>
<circle cx="674.7483413070622" cy="294.22998474765654" r="4" fill="#1f77b4"/>
<circle cx="702.3084652189323" cy="288.68617506444775" r="4" fill="#1f77b4"/>
<circle cx="727.2396298878734" cy="279.6034559750708" r="4" fill="#1f77b4"/>
<circle cx="750.0" cy="270.9485155901084" r="4" fill="#1f77b4"/>
<line x1="770" y1="60" x2="795" y2="60" stroke="#1f77b4" stroke-width="3"/>
<text x="800" y="65" font-size="14" font-family="Arial">BucketSort</text>
<polyline points="100.0,366.2222193529502 281.3129146732344,285.33443647773674 387.3741706535312,234.94338596297297 462.6258293464688,199.82813421665054 520.9955505456978,172.17086215637386 568.6870853267653,149.84995817111837 609.009703440515,130.5719577865836 643.9387440197032,113.16213421313444 674.7483413070622,98.34909329626106 702.3084652189323,81.47943452708506 727.2396298878734,71.58180797813105 750.0,60.0" fill="none" stroke="#d62728" stroke-width="3"/>
<circle cx="100.0" cy="366.2222193529502" r="4" fill="#d62728"/>
<circle cx="281.3129146732344" cy="285.33443647773674" r="4" fill="#d62728"/>
<circle cx="387.3741706535312" cy="234.94338596297297" r="4" fill="#d62728"/>
<circle cx="462.6258293464688" cy="199.82813421665054" r="4" fill="#d62728"/>
<circle cx="520.9955505456978" cy="172.17086215637386" r="4" fill="#d62728"/>
<circle cx="568.6870853267653" cy="149.84995817111837" r="4" fill="#d62728"/>
<circle cx="609.009703440515" cy="130.5719577865836" r="4" fill="#d62728"/>
<circle cx="643.9387440197032" cy="113.16213421313444" r="4" fill="#d62728"/>
<circle cx="674.7483413070622" cy="98.34909329626106" r="4" fill="#d62728"/>
<circle cx="702.3084652189323" cy="81.47943452708506" r="4" fill="#d62728"/>
<circle cx="727.2396298878734" cy="71.58180797813105" r="4" fill="#d62728"/>
<circle cx="750.0" cy="60.0" r="4" fill="#d62728"/>
<line x1="770" y1="90" x2="795" y2="90" stroke="#d62728" stroke-width="3"/>
<text x="800" y="95" font-size="14" font-family="Arial">SelectionSort</text>
</svg>
<figcaption><em>Execution times (log-log)</em></figcaption>
</figure>


### 4.2 Mean summary table (counts, time, CV)

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

### 4.3 Performance comparison

BucketSort is faster across all tested input sizes, and the speedup grows with `n`:

- `n=1000`: `176,777.5 / 56,402.5 ≈ 3.13×`
- `n=6000`: `5,040,242.5 / 350,490 ≈ 14.38×`
- `n=12000`: `20,260,940 / 772,872.5 ≈ 26.22×`

This widening gap is consistent with BucketSort’s lower expected growth in this configuration.

### 4.4 Critical-operation growth vs observed runtime

- **SelectionSort counts** exactly match `n(n-1)/2` at all tested sizes (CV = `0%` for count).
- **SelectionSort time** fits `n²` very strongly (`R² = 0.9994`).
- **BucketSort counts** best match `n^1.5` (`R² = 0.9999`).
- **BucketSort time** also best matches `n^1.5` among tested models (`R² = 0.9927`).

### 4.5 Coefficient of variation (CV) and data sensitivity

Average CV values:

- BucketSort count CV: `0.90%`
- SelectionSort count CV: `0.00%`
- BucketSort time CV: `21.88%`
- SelectionSort time CV: `7.02%`

Interpretation:

- SelectionSort’s structure makes operation count data-insensitive for fixed `n`.
- BucketSort is more sensitive to value distribution and bucket occupancy.
- A major time outlier appears for BucketSort at `n=6000` (one run near `2,260,500 ns`), likely due to runtime/system interference rather than algorithmic correctness issues.

### 4.6 Model-fit metrics table (`R²`)

| Metric | Value |
|---|---:|
| bucket_count_r2_n | 0.9859 |
| bucket_count_r2_n1.5 | 0.9999 |
| bucket_count_r2_n2 | 0.9873 |
| bucket_count_r2_nlogn | 0.9919 |
| bucket_time_r2_n | 0.9832 |
| bucket_time_r2_n1.5 | 0.9927 |
| bucket_time_r2_n2 | 0.9784 |
| bucket_time_r2_nlogn | 0.9879 |
| selection_count_r2_n | 0.9477 |
| selection_count_r2_n1.5 | 0.9891 |
| selection_count_r2_n2 | 1.0000 |
| selection_count_r2_nlogn | 0.9596 |
| selection_time_r2_n | 0.9451 |
| selection_time_r2_n1.5 | 0.9875 |
| selection_time_r2_n2 | 0.9994 |
| selection_time_r2_nlogn | 0.9572 |

---

## 5) Conclusion

The results clearly show that SelectionSort follows `Θ(n²)` behavior, while this BucketSort implementation (`k = sqrt(n)` + insertion sort in buckets) demonstrates empirically lower growth consistent with `Θ(n^(3/2))` under random input distributions. Across the full measured range, BucketSort is consistently and increasingly faster.

The CV analysis also reinforces expected sensitivity differences: SelectionSort has deterministic comparison counts, while BucketSort reflects data-distribution effects and occasional runtime noise in wall-clock timings.

---

## 6) Reproducibility and Included Artifacts

### Data and analysis files

- `CMSC451_Project1_Submission/BucketSort.txt`
- `CMSC451_Project1_Submission/SelectionSort.txt`
- `Project2/analysis_summary.csv`
- `Project2/fit_metrics.txt`
- `Project2/critical_operations.svg`
- `Project2/critical_operations_log.svg`
- `Project2/execution_times.svg`
- `Project2/execution_times_log.svg`
- `Project2/analyze_results.py`

### Commands

```bash
make report
javac CMSC451_Project1_Submission/*.java
```

### Single-file version with embedded graphs

If you need one standalone file that *includes* the SVG graph content, generate:

- `Project2/Project2_Consolidated_Report_Embedded.md`

using:

```bash
make embed
```


