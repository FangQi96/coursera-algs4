import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
	private final int leastMoves;
	private Stack<Board> solution = new Stack<Board>();
	private boolean initGoaled=false;
	private searchNode finalNode;
	private static class searchNode implements Comparable<searchNode>{
		private int move;
		private final Board board;
		private searchNode prev;
		public searchNode(Board initBoard){
			board = initBoard;
			prev = null;
			move = 0;
		}
		public searchNode(Board initBoard,searchNode prev,int move){
			board = initBoard;
			this.prev = prev;
			this.move = move;
		}
		public int compareTo(searchNode other){
			int priority = this.board.manhattan()+this.move;
			int otherPriority = other.board.manhattan()+other.move;
			if(priority>otherPriority)
				return 1;
			else if(priority==otherPriority)
				return 0;
			else
				return -1;
		}
	}
	public Solver(Board initial){
		MinPQ<searchNode> minpq = new MinPQ<searchNode>();
		MinPQ<searchNode> twinpq = new MinPQ<searchNode>();
		searchNode current = new searchNode(initial);
		searchNode twinCurrent = new searchNode(initial.twin());
		minpq.insert(current);
		twinpq.insert(twinCurrent);
		while(true){
			current = minpq.delMin();
			twinCurrent = twinpq.delMin();
			for(Board board:current.board.neighbors()){
				if(current.prev==null||!current.prev.board.equals(board)){
					searchNode neighborNode = new searchNode(board,current,current.move+1);
					minpq.insert(neighborNode);
				}
			}
			for(Board board:twinCurrent.board.neighbors()){
				if(twinCurrent.prev==null||!twinCurrent.prev.board.equals(board)){
					searchNode neighborNode = new searchNode(board,twinCurrent,twinCurrent.move+1);
					twinpq.insert(neighborNode);
				}
			}
			if(current.board.isGoal()){
				initGoaled = true;
				leastMoves = current.move;
				finalNode = new searchNode(current.board,current.prev,current.move);
				break;
			}
			if(twinCurrent.board.isGoal()){
				leastMoves = -1;
				break;
			}	
		}
	}
	
	public int moves(){
		return leastMoves;
	}
	public boolean isSolvable(){
		if(initGoaled)
			return true;
		else
			return false;
	}
	public Iterable<Board> solution(){
		if(!initGoaled)
			return null;
		else{
			while(finalNode!=null){
				solution.push(finalNode.board);
				finalNode=finalNode.prev;
			}
			return solution;
		}
	}
	public static void main(String args[]){
	    // create initial board from file
	    In in = new In(args[0]);
	    int n = in.readInt();
	    int[][] blocks = new int[n][n];
	    for (int i = 0; i < n; i++)
	        for (int j = 0; j < n; j++)
	            blocks[i][j] = in.readInt();
	    Board initial = new Board(blocks);
	    System.out.println(initial.manhattan());

	    // solve the puzzle
	    Solver solver = new Solver(initial);

	    // print solution to standard output
	    if (!solver.isSolvable())
	        StdOut.println("No solution possible");
	    else {
	        StdOut.println("Minimum number of moves = " + solver.moves());
	        for (Board board : solver.solution())
	            StdOut.println(board);
	    }
	}
}
