public class Percolation
{
  
    // variables
    private int N;
    private int nsq;
    private boolean[][] openSites;
    private boolean[][] fullSites;
    private int site;
    private WeightedQuickUnionUF wuf;

    // constructor
    public Percolation(int a) 
    {
        if (a <= 0)
        {
            throw new IllegalArgumentException("Input is invalid: " + a);
        }
        N = a + 1;
        nsq = N * N;
        openSites = new boolean[N][N];
        fullSites = new boolean[N][N];
        wuf = new WeightedQuickUnionUF(nsq);
    }

    // main
//     private static void main(int[] args) 
//     {
//         // TODO Auto-generated method stub
//         System.out.println(args);
//     }
  
    public boolean isOpen(int row, int col) 
    {
    	if (!validate(row, col))
        {
            throw new IndexOutOfBoundsException("Invalid input: [" + row + ", " + col + "]");
        }
        return validate(row, col) && openSites[row][col];
    }
    
    public boolean isFull(int row, int col) 
    {
    	if (!validate(row, col))
        {
            throw new IndexOutOfBoundsException("Invalid input: [" + row + ", " + col + "]");
        }
        return validate(row, col) && fullSites[row][col];
    }
  
    public void open(int row, int col) 
    {
        if (validate(row, col)) 
        {
            site = rowColTo1D(row, col);
            openSites[row][col] = true;
            
            if (row == 1)
            {
               fullSites[row][col] = true;
            }
            connect(row, col);
        } 
        else 
        {
            throw new IndexOutOfBoundsException("Invalid input: [" + row + ", " + col + "]");
        }
    }
  
    public boolean percolates() 
    {
        //if a point from the top row is connected with one in the bottom row, it percolates
        for (int i = 1; i < N; i++) 
        {
            for (int j = 1; j < N; j++) 
            {
                int t = rowColTo1D(1, i);
                int b = rowColTo1D(N-1, j);
                int top = wuf.find(t);
                int bottom = wuf.find(b);
                
                if (wuf.connected(top, bottom) && N-1 == 1)
                {
                    return true;
                }
                
                if (wuf.connected(top, bottom) && t != b) 
                {
                    return true;
                }
            }
        }
        return false;
    }

    private int rowColTo1D(int row, int col) 
    {
        return (N * (row - 1)) + col;
    }

    private boolean validate(int row, int col) 
    {
        int high = N + 1;
        return row > 0 && row < high && col > 0 && col < high;
    }
    
    private boolean topConnected(int s) 
    {
        //if a point from the top row is connected with one in the bottom row, it percolates
        for (int i = 1; i < N; i++) 
        {
            int t = rowColTo1D(1, i);
            int top = wuf.find(t);
            
            if (wuf.connected(top, s)) 
            {
                return true;
            }
        }
        return false;
    }
    
    private void testOpens()
    {
        //for each top site, recurse through everything
        for (int i = 1; i < N; i++)
        {
        for (int j = 1; j < N; j++)
        {
          if (openSites[i][j] && !fullSites[i][j])
          {
            int u = rowColTo1D(i, j);
            int test = wuf.find(u);
              for (int k = 1; k < N; k++) 
              {
                int t = rowColTo1D(1, k);
                int top = wuf.find(t);
              
              if (wuf.connected(top, test))
              {
                fullSites[i][j] = true;
              }
              }
          }
        }
      }
    }
    
    private void connect(int row, int col) 
    {
        int[][] check = {{row + 1, col}, {row - 1, col}, {row, col + 1}, {row, col - 1}};
        // for each of the neighbors let's connect or not
        for (int i = 0; i < check.length; i++) 
        {
            int m = check[i][0];
            int n = check[i][1];
            
            if (m != N && n != N) 
            {
                if (openSites[m][n]) 
                {
                   //then get the integer for each
                  int ck = rowColTo1D(m, n);
                    int a = wuf.find(ck);
                    int b = wuf.find(site);
                  
                    // if any surrounding site is also full, add site and itself to connected
                    if (fullSites[m][n]) 
                    {
                      fullSites[row][col] = true;
                    } 
                    else
                    {
                      // if it's connected to anything in the first row, then mark it as full and connected
                      for (int j = 1; j < N; j++)
                      {
                        int firstRowCheck = rowColTo1D(1, j);
                        if (wuf.connected(firstRowCheck, a))
                        {
                          fullSites[row][col] = true;             
                        }
                      }
                    }
                    
                    //then check connected
                    if (!wuf.connected(a, b)) 
                    {
                        //then, if not union
                        wuf.union(a, b);
                        if (topConnected(a))
                        {
                            fullSites[m][n] = true;
                        }
                        testOpens();
                    }
                }
            }
        }
    }
}
