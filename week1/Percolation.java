import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

  private final int n;
  private final int virtualTop;
  private final int virtualBottom;
  private final WeightedQuickUnionUF unionUF;
  private final WeightedQuickUnionUF unionUFTop;
  private boolean[][] sites;
  private int openSites;

  // create n-by-n grid, with all sites blocked
  public Percolation(int n) {
    if (n <= 0) {
      throw new IllegalArgumentException();
    }

    this.n = n;
    int siteNo = n * n;
    this.virtualTop = 0;
    this.virtualBottom = siteNo + 1;
    this.unionUF = new WeightedQuickUnionUF(siteNo + 2);
    this.unionUFTop = new WeightedQuickUnionUF(siteNo + 1);

    for (int i = 0; i < n; i++) {
      int topIndex = i + 1;
      int bottomIndex = siteNo - i;
      union(topIndex, virtualTop);
      unionUF.union(bottomIndex, virtualBottom);
    }

    this.sites = new boolean[n][n];
    this.openSites = 0;

  }

  // open site (row, col) if it is not open already
  public void open(int row, int col) {
    validateCell(row, col);

    if (!isOpen(row, col)) {
      int index = convertToIndex(row, col);
      int[] neighbours = getNeighbours(index);

      // open site
      openSite(row, col);

      // connect to neighbours if needed
      for (int i = 0; i < neighbours.length && neighbours[i] != 0; i++) {
        int neighbourIndex = neighbours[i];
        int[] neighbourCell = convertToCell(neighbourIndex);
        if (isOpen(neighbourCell[0], neighbourCell[1])) {
          union(index, neighbourIndex);
        }
      }
    }
  }

  public boolean isOpen(int row, int col) {
    validateCell(row, col);
    return sites[row - 1][col - 1];
  }

  // is sites (row, col) full?
  public boolean isFull(int row, int col) {
    validateCell(row, col);
    int index = convertToIndex(row, col);
    return isOpen(row, col) && unionUFTop.connected(index, virtualTop);
  }

  // number of open sites
  public int numberOfOpenSites() {
    return openSites;
  }

  // does the system percolate?
  public boolean percolates() {
    return numberOfOpenSites() > 0 && unionUF.connected(virtualTop, virtualBottom);
  }

  private void union(int p, int q) {
    unionUF.union(p, q);
    unionUFTop.union(p, q);
  }
  private void validateCell(int row, int col) {
    validateArgument(row);
    validateArgument(col);
  }

  private void validateArgument(int arg) {
    if (arg < 1 || arg > n) {
      throw new IllegalArgumentException();
    }
  }

  private void openSite(int row, int col) {
    sites[row - 1][col - 1] = true;
    openSites++;
  }

  private int convertToIndex(int row, int col) {
    return (row - 1) * n + col;
  }

  private int[] convertToCell(int index) {
    int row, col;
    int mod = index % n;

    // different logic for last column cells
    if (mod != 0) {
      row = (index / n) + 1;
      col = mod;
    }
    else {
      row = index / n;
      col = n;
    }

    int[] cell = { row, col };
    return cell;
  }

  private int[] getNeighbours(int index) {
    int[] neighbours = {0, 0, 0, 0};
    int neighboursNo = 0;

    // TOP
    if (index > n) {
      neighbours[neighboursNo] = index - n;
      neighboursNo++;
    }

    // BOTTOM
    if (index <= n * (n - 1)) {
      neighbours[neighboursNo] = index + n;
      neighboursNo++;
    }

    // LEFT
    if (index % n != 1) {
      neighbours[neighboursNo] = index - 1;
      neighboursNo++;
    }

    // RIGHT
    if (index % n != 0) {
      neighbours[neighboursNo] = index + 1;
      neighboursNo++;
    }

    return neighbours;
  }
}