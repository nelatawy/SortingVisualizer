import pandas as pd
import matplotlib.pyplot as plt
import seaborn as sns
import numpy as np

# Set plot style
sns.set_theme(style="whitegrid")
plt.rcParams['figure.figsize'] = (12, 6)

def analyze_sorting_data(csv_path='stats.csv'):
    try:
        print(f"Loading data from {csv_path}...")
        
        # Manually parse to handle mismatched columns (e.g. 8 headers but 7 values)
        with open(csv_path, 'r') as f:
            lines = f.readlines()
        
        header = lines[0].strip().split(',')
        data = []
        for line in lines[1:]:
            if not line.strip(): continue
            row = line.strip().split(',')
            # Pad with NaN if columns are missing
            while len(row) < len(header):
                row.append(np.nan)
            data.append(row)
        
        df = pd.DataFrame(data, columns=header)
        
        # Convert numeric columns
        numeric_cols = ['Size', 'Comparisons', 'Swaps', 'Writes', 'Min Runtime', 'Max Runtime', 'Mean Runtime']
        for col in numeric_cols:
            df[col] = pd.to_numeric(df[col], errors='coerce')
        
        # Fix missing Mean Runtime
        df['Mean Runtime'] = df['Mean Runtime'].fillna((df['Min Runtime'] + df['Max Runtime']) / 2)
        
        print("Data Loaded and Cleaned Successfully.\n")
        
        # Validation
        print("### Validation & Fairness Check ###")
        algorithms = df['Algorithm'].unique()
        sizes = sorted(df['Size'].unique())
        
        fairness_pass = True
        for alg in algorithms:
            alg_sizes = set(df[df['Algorithm'] == alg]['Size'])
            if set(sizes) != alg_sizes:
                missing = set(sizes) - alg_sizes
                print(f"[WARNING] {alg} is missing sizes: {missing}")
                fairness_pass = False
        
        if fairness_pass:
            print("[PASS] Fairness: All algorithms tested on all sizes.")
            
        negative_vals = (df.select_dtypes(include=[np.number]) < 0).any().any()
        if not negative_vals:
            print("[PASS] Integrity: No negative values.")
        
        # Visualization
        print("\nGenerating Plots...")
        fig, axes = plt.subplots(1, 3, figsize=(18, 5))
        
        plot_configs = [
            ('Comparisons', 'log'),
            ('Swaps', 'symlog'),
            ('Mean Runtime', 'log')
        ]
        
        for i, (metric, scale) in enumerate(plot_configs):
            sns.lineplot(data=df, x='Size', y=metric, hue='Algorithm', marker='o', ax=axes[i])
            axes[i].set_title(f'{metric} vs Size')
            axes[i].set_yscale(scale)
            axes[i].set_xscale('log')
        
        plt.tight_layout()
        plt.show()
        
    except Exception as e:
        print(f"Error: {e}")

if __name__ == "__main__":
    analyze_sorting_data()
