import math
import os
import csv

def calculate_mean(values):
    return sum(values) / len(values) if values else 0

def calculate_cv(values, mean):
    if not values or mean == 0:
        return 0
    variance = sum((x - mean) ** 2 for x in values) / (len(values) - 1)
    std_dev = math.sqrt(variance)
    return (std_dev / mean) * 100

def linear_regression(x_vals, y_vals):
    n = len(x_vals)
    if n < 2:
        return 0, 0, 0
    sum_x = sum(x_vals)
    sum_y = sum(y_vals)
    sum_xx = sum(x * x for x in x_vals)
    sum_xy = sum(x * y for x, y in zip(x_vals, y_vals))
    
    denom = (n * sum_xx - sum_x**2)
    if denom == 0: return 0, 0, 0
    
    slope = (n * sum_xy - sum_x * sum_y) / denom
    intercept = (sum_y - slope * sum_x) / n
    
    # R^2 calculation
    y_mean = sum_y / n
    ss_tot = sum((y - y_mean)**2 for y in y_vals)
    ss_res = sum((y - (slope * x + intercept))**2 for x, y in zip(x_vals, y_vals))
    r_squared = 1 - (ss_res / ss_tot) if ss_tot != 0 else 1
    
    return slope, intercept, r_squared

def process_file(file_path):
    data = {}
    with open(file_path, 'r') as f:
        for line in f:
            parts = list(map(int, line.split()))
            if not parts: continue
            size = parts[0]
            counts = []
            times = []
            for i in range(1, len(parts), 2):
                counts.append(parts[i])
                times.append(parts[i+1])
            data[size] = {'counts': counts, 'times': times}
    return data

def generate_svg(filename, title, x_label, y_label, x_data, datasets, log_x=False, log_y=False):
    width = 900
    height = 520
    padding_left = 100
    padding_right = 150
    padding_top = 60
    padding_bottom = 80
    
    # Filter non-positive values for log scale
    def filter_log(xs, ys):
        return [(x, y) for x, y in zip(xs, ys) if x > 0 and y > 0]

    processed_datasets = []
    for label, color, ys in datasets:
        pts = filter_log(x_data, ys) if (log_x or log_y) else list(zip(x_data, ys))
        if pts:
            processed_datasets.append((label, color, pts))

    if not processed_datasets: return

    all_x = [p[0] for _, _, pts in processed_datasets for p in pts]
    all_y = [p[1] for _, _, pts in processed_datasets for p in pts]
    
    min_x, max_x = min(all_x), max(all_x)
    min_y, max_y = min(all_y), max(all_y)

    def tx(x):
        if log_x:
            return padding_left + (math.log10(x) - math.log10(min_x)) / (math.log10(max_x) - math.log10(min_x)) * (width - padding_left - padding_right)
        return padding_left + (x - 0) / (max_x - 0) * (width - padding_left - padding_right)

    def ty(y):
        if log_y:
            return height - padding_bottom - (math.log10(y) - math.log10(min_y)) / (math.log10(max_y) - math.log10(min_y)) * (height - padding_top - padding_bottom)
        return height - padding_bottom - (y - 0) / (max_y - 0) * (height - padding_top - padding_bottom)

    with open(filename, 'w') as f:
        f.write('<svg xmlns="http://www.w3.org/2000/svg" width="{}" height="{}" viewBox="0 0 {} {}">\n'.format(width, height, width, height))
        f.write('<rect width="100%" height="100%" fill="white"/>\n')
        f.write('<text x="{}" y="35" text-anchor="middle" font-size="22" font-family="Arial" font-weight="bold">{}</text>\n'.format(width/2, title))
        
        f.write('<line x1="{}" y1="{}" x2="{}" y2="{}" stroke="black" stroke-width="2"/>\n'.format(padding_left, height-padding_bottom, width-padding_right, height-padding_bottom))
        f.write('<line x1="{}" y1="{}" x2="{}" y2="{}" stroke="black" stroke-width="2"/>\n'.format(padding_left, padding_top, padding_left, height-padding_bottom))
        
        f.write('<text x="{}" y="{}" text-anchor="middle" font-size="14" font-family="Arial">{}</text>\n'.format(width/2, height-20, x_label))
        f.write('<text x="30" y="{}" transform="rotate(-90,30,{})" text-anchor="middle" font-size="14" font-family="Arial">{}</text>\n'.format(height/2, height/2, y_label))

        for i, (label, color, pts) in enumerate(processed_datasets):
            points_str = " ".join(["{},{}".format(tx(p[0]), ty(p[1])) for p in pts])
            f.write('<polyline points="{}" fill="none" stroke="{}" stroke-width="3"/>\n'.format(points_str, color))
            for p in pts:
                f.write('<circle cx="{}" cy="{}" r="4" fill="{}"/>\n'.format(tx(p[0]), ty(p[1]), color))
            
            lx = width - padding_right + 20
            ly = padding_top + i * 30
            f.write('<line x1="{}" y1="{}" x2="{}" y2="{}" stroke="{}" stroke-width="3"/>\n'.format(lx, ly, lx+25, ly, color))
            f.write('<text x="{}" y="{}" font-size="14" font-family="Arial">{}</text>\n'.format(lx+30, ly+5, label))
            
        f.write('</svg>')

# Paths
base_dir = r"C:\Users\mluke\antigravity_workspace\csmc451-project1"
data_dir = os.path.join(base_dir, "CMSC451_Project1_Submission")
out_dir = os.path.join(base_dir, "CSMC451", "Project2")

# Process raw data
bucket_data = process_file(os.path.join(data_dir, "BucketSort.txt"))
selection_data = process_file(os.path.join(data_dir, "SelectionSort.txt"))

sizes = sorted(bucket_data.keys())
summary = []

for size in sizes:
    b_counts = bucket_data[size]['counts']
    b_times = bucket_data[size]['times']
    s_counts = selection_data[size]['counts']
    s_times = selection_data[size]['times']
    
    b_mean_c = calculate_mean(b_counts)
    b_cv_c = calculate_cv(b_counts, b_mean_c)
    b_mean_t = calculate_mean(b_times)
    b_cv_t = calculate_cv(b_times, b_mean_t)
    
    s_mean_c = calculate_mean(s_counts)
    s_cv_c = calculate_cv(s_counts, s_mean_c)
    s_mean_t = calculate_mean(s_times)
    s_cv_t = calculate_cv(s_times, s_mean_t)
    
    summary.append({
        'n': size,
        'bucket_mean_count': b_mean_c,
        'bucket_cv_count_pct': b_cv_c,
        'bucket_mean_time_ns': b_mean_t,
        'bucket_cv_time_pct': b_cv_t,
        'selection_mean_count': s_mean_c,
        'selection_cv_count_pct': s_cv_c,
        'selection_mean_time_ns': s_mean_t,
        'selection_cv_time_pct': s_cv_t
    })

# Write summary CSV
with open(os.path.join(out_dir, 'analysis_summary.csv'), 'w', newline='') as f:
    writer = csv.DictWriter(f, fieldnames=summary[0].keys())
    writer.writeheader()
    writer.writerows(summary)

# Calculate fits
fits = {}
b_counts_all = [s['bucket_mean_count'] for s in summary]
b_times_all = [s['bucket_mean_time_ns'] for s in summary]
s_counts_all = [s['selection_mean_count'] for s in summary]
s_times_all = [s['selection_mean_time_ns'] for s in summary]

models = {
    'n': lambda n: n,
    'n2': lambda n: n**2,
    'nlogn': lambda n: n * math.log2(n) if n > 0 else 0,
    'n1.5': lambda n: n**1.5
}

for algo, counts, times in [('bucket', b_counts_all, b_times_all), ('selection', s_counts_all, s_times_all)]:
    for model_name, func in models.items():
        x_transformed = [func(n) for n in sizes]
        _, _, r2_c = linear_regression(x_transformed, counts)
        _, _, r2_t = linear_regression(x_transformed, times)
        fits[f'{algo}_count_r2_{model_name}'] = r2_c
        fits[f'{algo}_time_r2_{model_name}'] = r2_t

# Write fits
with open(os.path.join(out_dir, 'fit_metrics.txt'), 'w') as f:
    for k in sorted(fits.keys()):
        f.write('{}={}\n'.format(k, fits[k]))
    f.write('avg_bucket_cv_count_pct={}\n'.format(calculate_mean([s["bucket_cv_count_pct"] for s in summary])))
    f.write('avg_bucket_cv_time_pct={}\n'.format(calculate_mean([s["bucket_cv_time_pct"] for s in summary])))
    f.write('avg_selection_cv_count_pct={}\n'.format(calculate_mean([s["selection_cv_count_pct"] for s in summary])))
    f.write('avg_selection_cv_time_pct={}\n'.format(calculate_mean([s["selection_cv_time_pct"] for s in summary])))

# Generate SVGs
datasets_c = [
    ('BucketSort', '#1f77b4', b_counts_all),
    ('SelectionSort', '#d62728', s_counts_all)
]
generate_svg(os.path.join(out_dir, 'critical_operations.svg'), 'Critical Operations vs Input Size', 'Input Size (n)', 'Count', sizes, datasets_c)
generate_svg(os.path.join(out_dir, 'critical_operations_log.svg'), 'Critical Operations vs Input Size (Log-Log)', 'Input Size (log n)', 'Count (log)', sizes, datasets_c, log_x=True, log_y=True)

datasets_t = [
    ('BucketSort', '#1f77b4', b_times_all),
    ('SelectionSort', '#d62728', s_times_all)
]
generate_svg(os.path.join(out_dir, 'execution_times.svg'), 'Execution Times vs Input Size', 'Input Size (n)', 'Time (ns)', sizes, datasets_t)
generate_svg(os.path.join(out_dir, 'execution_times_log.svg'), 'Execution Times vs Input Size (Log-Log)', 'Input Size (log n)', 'Time (log ns)', sizes, datasets_t, log_x=True, log_y=True)

print("Analysis complete.")
