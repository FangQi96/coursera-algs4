import edu.princeton.cs.algs4.Queue;

public class Board {
	private final int dimension;
	private final int[][] components;
	private final int hamming;
	private final int manhattan;
	public Board(int[][] blocks) {		//consider block0 when initial the board
		dimension = blocks[0].length;
		components = new int[dimension][dimension];
		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++)
				components[i][j] = blocks[i][j];
		int hamming = 0;
		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++)
				if (components[i][j] != 0 && components[i][j] != (i * dimension + j + 1))	//ignore block 0 when calculate hamming
					hamming++;
		this.hamming = hamming;
		int manhattan = 0;
		for (int i = 0; i < dimension; i++)
			for (int j = 0; j < dimension; j++) {	//ignore block 0 when calculate manhattan
					manhattan = manhattan + manDistance(components[i][j], i, j);
			}
		this.manhattan = manhattan;
		
	}

	public int dimension() {
		return dimension;
	}

	public int hamming() {
		return hamming;
	}

	private int manDistance(int value, int i, int j) {
		if (value == (i * dimension + j + 1)||value==0)
			return 0;
		int x, y;
		x = y = 0;
		while ((value - dimension) > 0) {
			x++;
			value -= dimension;
		}
		y = value - 1;
		return Math.abs(x-i)+Math.abs(y-j);
	}

	public int manhattan() {
		return manhattan;
	}
	
	public boolean isGoal()	{
		int count=0;
		for(int i=0;i<dimension;i++)
			for(int j=0;j<dimension&&count<dimension*dimension-1;j++){
				count++;
				if(components[i][j]!=(i*dimension+j+1))
					return false;
			}
		return true;
	}
	
	public Board twin(){
		int[][] initArray = new int[dimension][dimension];
		for(int i=0;i<dimension;i++)
			for(int j=0;j<dimension;j++)
				initArray[i][j]=components[i][j];
		if(components[0][0]!=0&&components[0][1]!=0){
			int temp;
			temp = initArray[0][0];
			initArray[0][0] = initArray[0][1];
			initArray[0][1] = temp;
			return new Board(initArray);
		}
		else{
			int temp;
			temp = initArray[1][0];
			initArray[1][0] = initArray[1][1];
			initArray[1][1] = temp;
			return new Board(initArray);
		}
	}
	
	public boolean equals(Object y){
		if(y == null||this == null)
			return false;
		if(this.getClass()!=y.getClass())		//null and different class need to be handled in advance
			return false;
		Board other = (Board)y;
		if(this.dimension!=other.dimension)
			return false;
		for(int i=0;i<dimension;i++)
			for(int j=0;j<dimension;j++)
				if(this.components[i][j]!=other.components[i][j])
					return false;
		return true;
	}
	
	public Iterable<Board> neighbors(){
		Queue<Board> neighbours = new Queue<Board>(); 
        int row = 0;
        int col = 0;                
        getPosition:
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (components[i][j] == 0) { 
                    row = i; 
                    col = j; 
                    break getPosition;
                }                     
            }
        }
        int[] dx = {-1, 0, 0, 1};
        int[] dy = {0, -1, 1, 0};        
        int[][] tmpBlocks = new int[dimension][dimension];
        int posX, posY;
        for (int i = 0; i < 4; i++) {
            posX = row + dx[i];		//array dx and dy represents upward downward leftward and rightward
            posY = col + dy[i];
            if (posX >= 0 && posX < dimension 				//copy the components if (posX,posY) exist
                    && posY >= 0 && posY < dimension) {                
                for (int j = 0; j < dimension; j++) {
                    for (int k = 0; k < dimension; k++) 
                        tmpBlocks[j][k] = components[j][k];
                }                    
                int tmpNum = tmpBlocks[row][col];			//exchange (posX,posY) and (row,col) of tmpBlocks
                tmpBlocks[row][col] = tmpBlocks[posX][posY];
                tmpBlocks[posX][posY] = tmpNum;                    
                Board nbr = new Board(tmpBlocks);
                neighbours.enqueue(nbr);                    
            }       
        }    
        return neighbours;
	}
	
	public String toString(){
		StringBuilder s = new StringBuilder();
		s.append(dimension+"\n");
		for(int i =0;i<dimension;i++){
			for(int j=0;j<dimension;j++){
				s.append(String.format("%2d"+" ", components[i][j]));	//a " " is needed between two number
			}
			s.append("\n");
		}
		return s.toString();
	}
}
