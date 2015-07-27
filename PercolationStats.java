public class PercolationStats 
{
    private int N;
    private int T;
    private int nsq;
    private double[] randoms;

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
            randoms = createRandomArray();
        }
    }

    public static void main(String[] args)
    {
        int a = Integer.parseInt(args[0]);
        int b = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(a, b);
        System.out.println("mean                    = " + ps.mean());
        System.out.println("stddev                  = " + ps.stddev());
        System.out.println("95% confidence interval = " + ps.confidenceLo() + ", " + ps.confidenceHi());
    }

    public double mean()
    {
        return StdStats.mean(randoms);
    }

    public double stddev()
    {
        return StdStats.stddev(randoms);
    }

    public double confidenceLo()
    {
        double m = mean();
        double s = stddev();
        return m - ((1.96 * s)/Math.sqrt(T));
    }

    public double confidenceHi()
    {
        double m = mean();
        double s = stddev();
        return m + ((1.96 * s)/Math.sqrt(T));
    }

    private double[] createRandomArray()
    {
        double[] rands = new double[T];
        for (int i = 0; i < T; i++)
        {
            double randNum = StdRandom.uniform(nsq);
            rands[i] = randNum / nsq;
        }
        return rands;
    }
}
