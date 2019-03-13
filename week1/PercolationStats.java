import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

  private final double mean;
  private final double stddev;
  private final double confidenceLo;
  private final double confidenceHi;

  // perform trials independent experiments on an n-by-n grid
  public PercolationStats(int n, int trials) {
    if (n <= 0 || trials <= 0) {
      throw new IllegalArgumentException();
    }
    final double sites = n * n;
    final double confidenceFactor = 1.96;
    final double[] fractions = new double[trials];


    for (int i = 0; i < trials; i++) {
      Percolation percolation = new Percolation(n);

      while (!percolation.percolates()) {
        int randomRow = StdRandom.uniform(n) + 1;
        int randomCol = StdRandom.uniform(n) + 1;
        percolation.open(randomRow, randomCol);
      }

      fractions[i] = percolation.numberOfOpenSites() / sites;
    }

    this.mean = StdStats.mean(fractions);
    this.stddev = StdStats.stddev(fractions);
    double confidenceRatio = (confidenceFactor * this.stddev) / Math.sqrt(trials);
    this.confidenceLo = this.mean - confidenceRatio;
    this.confidenceHi = this.mean + confidenceRatio;
  }

  // sample mean of percolation threshold
  public double mean() {
    return this.mean;
  }

  // sample standard deviation of percolation threshold
  public double stddev() {
    return this.stddev;
  }

  // low  endpoint of 95% confidence interval
  public double confidenceLo() {
    return this.confidenceLo;
  }

  // high endpoint of 95% confidence interval
  public double confidenceHi() {
    return this.confidenceHi;
  }

  // test client (described below)
  public static void main(String[] args) {
    if (args.length != 2) {
      throw new IllegalArgumentException();
    }

    int n = Integer.parseInt(args[0]);
    int trials = Integer.parseInt(args[1]);

    PercolationStats stats = new PercolationStats(n, trials);

    System.out.println(String.format("mean = %f", stats.mean()));
    System.out.println(String.format("stddev = %f", stats.stddev()));
    System.out.println(String.format("95%% confidence interval = [%f, %f]", stats.confidenceLo(), stats.confidenceHi()));
  }
}