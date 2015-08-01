public class PercolationStats 
{	
    private int N;
    private int T;
    private int nsq;
    
    private static double[] thresholds;
    private static double mn;
    private static double sd;
    
    public PercolationStats(int a, int b)
    {
        if (a <= 0 || b <= 0) 
        {
            throw new IllegalArgumentException("Arguments should be greater than 0!");
        } 
        else 
        {
            N = a;
            T = b;
            nsq = N * N;
            thresholds = new double[T];
        }
    }

    public static void main(String[] args)
    {
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        
        PercolationStats ps = new PercolationStats(a, b);
        
        int i = 0;
        
        while (i < b)
        {
          thresholds[i] = ps.findThreshold();
          i++;
        }
        
        mn = ps.mean();
        sd = ps.stddev();
        
        System.out.println("mean                    = " + mn);
        System.out.println("stddev                  = " + sd);
        System.out.println("95% confidence interval = " + ps.confidenceLo() + ", " + ps.confidenceHi());
    }

    public double mean()
    {
        return StdStats.mean(thresholds);
    }

    public double stddev()
    {
        return StdStats.stddev(thresholds);
    }

    public double confidenceLo()
    {
        return mn - ((1.96 * sd)/Math.sqrt(T));
    }

    public double confidenceHi()
    {
        return mn + ((1.96 * sd)/Math.sqrt(T));
    }

    private double findThreshold()
    {
      Percolation perc = new Percolation(N);
      boolean[][] testedSites = new boolean[N+1][N+1];
      int i = 0;
      while (i < nsq)
      {
        int row = StdRandom.uniform(1, N+1);
        int col = StdRandom.uniform(1, N+1);
        if (testedSites[row][col])
        {
          i--;
          continue;
        }
        else
        {
          testedSites[row][col] = true;
          perc.open(row, col);
            if (perc.percolates())
            {
              double tries = i;
              double tot = nsq;
              double threshold = tries / tot;
              return threshold;
            }
            i++;
        }
      }
      return 1;
    }
}
