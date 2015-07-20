
public class Percolation 
{
	
	// variables
	public static int N;
	public static int Nsq;
	public static boolean[][] openSites;
	public static boolean[][] fullSites;
	public static int site;
	private static WeightedQuickUnionUF wuf;
	
	// constructor
	public Percolation(int a) 
	{
		N = a + 1;
		Nsq = N * N;
		openSites = new boolean[N][N];
		fullSites = new boolean[N][N];
		wuf = new WeightedQuickUnionUF(Nsq);
	}
	
	// main
	public static void main(int[] args) 
	{
		// TODO Auto-generated method stub
		System.out.println(args);
	}
	
	public boolean isOpen(int row, int col) 
	{
		return openSites[row][col];
	}
	
	public boolean isFull(int row, int col) 
	{
		return fullSites[row][col];
	}
	
	public void open(int row, int col) 
	{
		if (validate(row, col)) 
		{
			site = rowColTo1D(row, col);
			openSites[row][col] = true;
			connect(row, col);
		} 
		else 
		{
			throw new IndexOutOfBoundsException("Bad validation...");
		}
	}
	
	public boolean percolates() 
	{
		//if a point from the top row is connected with one in the bottom row, it percolates
		for (int i = 0; i < N; i++) 
		{
			for (int j = 0; j < N; j++) 
			{
				int t = rowColTo1D(1, i);
				int b = rowColTo1D(N-1, j);
				int top = wuf.find(t);
				int bottom = wuf.find(b);
				if (wuf.connected(top, bottom)) 
				{
					return true;
				}
			}
		}
		return false;
	}
	// should this be a multi-dimensional array?
	private static int rowColTo1D(int row, int col) 
	{
		return (N * (row - 1)) + col;
	}

	private static boolean validate(int row, int col) 
	{
		int high = N + 1;
		return row > 0 && row < high && col > 0 && col < high;
	}
	
	private static void connect(int row, int col) 
	{
		int[][] check = {{row + 1, col},{row - 1, col},{row, col + 1},{row, col - 1}};
		// for each of the neighbors let's connect or not
		for (int i = 0; i < check.length; i++) 
		{
			int m = check[i][0];
			int n = check[i][1];
			if (m == N || n == N) return;
			//then get the integer for each
			if ( openSites[m][n] ) 
			{
				int ck = rowColTo1D(m, n);
				int a = wuf.find(ck);
				int b = wuf.find(site);
				fullSites[m][n] = true;
				//then check connected
				if ( !wuf.connected(a, b) ) 
				{
				    //then, if not union
				    wuf.union(a, b);
				}
			}
		}
	}
	
}
