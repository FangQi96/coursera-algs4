import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {  
   private boolean[] openstatus;
   private int N;
   private WeightedQuickUnionUF backwash;
   private WeightedQuickUnionUF no_backwash;
   private int opened_number=0;  //use class name to call static value (static way)
   public Percolation(int n){
       if(n<1){
           throw new IllegalArgumentException();
       }
       N = n;
       backwash = new WeightedQuickUnionUF((N+1)*N+N+1);     //node[0] is not used in both situation
       no_backwash = new WeightedQuickUnionUF((N+1)*N+1);
       openstatus = new boolean[(N+1)*N+N+1];
       for(int i=1;i<=n;i++){
           backwash.union(1, i);
           backwash.union((N+1)*N+1, (N+1)*N+i);
           no_backwash.union(1, i);
           openstatus[i]=true;
           openstatus[(N+1)*N+i]=true;
       }
   }
   public boolean isOpen(int i,int j){
       if(i<1||j<1||i>N||j>N){
           throw new IndexOutOfBoundsException();
       }
       return openstatus[i*N+j];
   }
   public void open(int i,int j){
       if(i<1||j<1||i>N||j>N){
           throw new IndexOutOfBoundsException();
       }
       if(isOpen(i,j)){
           return;
       }
       openstatus[i*N+j]=true;
       this.opened_number++;
       int self = N*i+j;
       int up = self-N;
       int down = self+N;
       int left = self-1;
       int right = self+1;
       if(openstatus[(i-1)*N+j]){       //DO NOT use isOpen() in up and down situation(out of boundary)
           backwash.union(self, up);
           no_backwash.union(self, up);
       }
       if(openstatus[(i+1)*N+j]){
           backwash.union(self, down);
           if(i!=N){
               no_backwash.union(self, down);
           }
       }
       if(j!=1&&isOpen(i,j-1)){
           backwash.union(self, left);
           no_backwash.union(self, left);
       }
       if(j!=N&&isOpen(i,j+1)){
           backwash.union(self, right);
           no_backwash.union(self, right);
       }
   }
   public int numberOfOpenSites(){
       return this.opened_number;
   }
   public boolean isFull(int i,int j){
       if(i<1||j<1||i>N||j>N){
           throw new IndexOutOfBoundsException();
       }
       return isOpen(i,j)&&no_backwash.connected(N*i+j, 1);
   }
   public boolean percolates(){
       return backwash.connected(1, (N+1)*N+1);
   }
}  