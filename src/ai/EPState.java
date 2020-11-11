/**
 *
 * @author Nihal Vatandas
 * 
 * language / compiler : java / javac 1.7.0_45
 * machine : Mac OS X 10.7.5 
 * date : April 7, 2015 7:53 PM
 *
 * 
 */
package ai;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;


/**
 * This class is created for the states of the eight puzzle problem 
 * Each state keeps a 2Dim integer array as the board of the game
 * and the position of the empty cell
 * Empty cell is denoted by 0 on the board
 */


public class EPState implements State {
    
    private final int[][] board = new int[3][3];
    private int empty_row, empty_col ;
    
    
    
     public EPState(){}
    
    /**
     * This constructor takes the board configuration as the input.
     * @param init_values: board configuration
     */
    public EPState(int[][] init_values){ 
        
        // put zero into the empty cell
        for(int i =0; i<3; i++){
            System.arraycopy(init_values[i], 0, this.board[i], 0, 3);
        }
        
        // determine the position of the empty cell (marked by 0)
        for(int i=0; i<3; i++ ){
            for(int j = 0; j<3; j++){
                if (init_values[i][j] == 0){
                    this.empty_row = i;
                    this.empty_col = j;
                }
            }
        }       
    }
    
    
    /**
     * This constructor creates a next state of a state.
     * The four integer input show the position of the tile to be moved
     * and its final position 
     * @param prev: parent state
     * @param source_row: row number of the tile to be moved in the parent state
     * @param source_col: column number of the tile to be moved in the parent state
     * @param dest_row: new row destination of the tile in the created state
     * @param dest_col: new column of the tile in the created state
     */
    // assuming  the command (coordinates) are valid
    public EPState(EPState prev, int source_row, int source_col,
                              int dest_row, int dest_col){
        
        
        // copy the parent's state to the new one
        for(int i=0; i<3; i++ ){
            System.arraycopy(prev.board[i], 0, this.board[i], 0, 3);
        }
        
        // do the changes, move the tile, set the new empty cell coordinates
        this.board[dest_row][dest_col] = prev.board[source_row][source_col];
        this.board[source_row][source_col] = 0;
        this.empty_row = source_row;
        this.empty_col = source_col;   
    
    }
    
    /**
     * Returns the set of all possible next states of a state.
     */
    @Override
    public Set<State> next_states() {
    
        Set<State> moves = new HashSet<State>();

        // if empty cell is not on the leftmost column, move it to the left
        if (this.empty_row -1 >= 0 ){
            moves.add( new EPState(this, this.empty_row-1,this.empty_col, 
                                   this.empty_row, this.empty_col));}
        
        
        // if empty cell is not on the rightmost column, move it to the right
        if (this.empty_row +1 < 3 ){
            moves.add( new EPState(this, this.empty_row+1,this.empty_col, 
                                   this.empty_row, this.empty_col));}

        
        // if empty cell is not on the upmost row, move it to up        
        if (this.empty_col -1 >= 0 ){
            moves.add( new EPState(this, this.empty_row, this.empty_col-1, 
                                   this.empty_row, this.empty_col));}
        
        
        // if empty cell is not on the bottom row, move it to down        
        if (this.empty_col +1 < 3 ){
            moves.add( new EPState(this, this.empty_row,this.empty_col+1, 
                                   this.empty_row, this.empty_col));}

        return moves;
    }

    /**
     * The goal is to have the empty cell at top left corner
     * and the numbers in increasing order (row-wise)
     */
    @Override
    public boolean goal_test() {
        int[][] goal = {{0,1,2},{3,4,5},{6,7,8}};
        return Arrays.deepEquals(this.board, goal);    
    }
    
    
    
    /**
     * Returns the heuristic function's values
     * Manhattan distance will be used as the heuristic
     */
    @Override
    public double heuristic() {
        return this.manhattan_dist_sum();
    }

    
    
    /**
     * Computes the manhattan distance of each tile to their position 
     * in the goal state. 
     * Returns the sum of distances of all tiles.
     */
    public double manhattan_dist_sum(){
    
    double sum = 0;
    for(int i=0; i<3; i++ ){
            for(int j = 0; j<3; j++){
                switch(this.board[i][j]){
                    case 1: sum += (Math.abs(i-0) + Math.abs(j-1)); break;
                    case 2: sum += (Math.abs(i-0) + Math.abs(j-2)); break;
                    case 3: sum += (Math.abs(i-1) + Math.abs(j-0)); break;
                    case 4: sum += (Math.abs(i-1) + Math.abs(j-1)); break;
                    case 5: sum += (Math.abs(i-1) + Math.abs(j-2)); break;
                    case 6: sum += (Math.abs(i-2) + Math.abs(j-0)); break;
                    case 7: sum += (Math.abs(i-2) + Math.abs(j-1)); break;
                    case 8: sum += (Math.abs(i-2) + Math.abs(j-2)); break;
                    default: 
                }
            }
        }
    
    
    return sum;
    }
    
    /**
     * Returns true if two states are equal.
     */
    @Override
    public boolean equals(Object o) {
        
        // two states are equal if they have
        // the same board configuration
        
        if (o==null || !(o instanceof EPState))
            return false;
	EPState eps = (EPState)o;
		
        for(int i=0; i<3; i++ ){
            for(int j = 0; j<3; j++){
                if (this.board[i][j] != eps.board[i][j] ){
                    return false;
                }
            }
        }        
        return true;
    }
	
    /**
     * Prints the information about the state (the board configuration)
     */
    @Override
    public void print_state() {
        System.out.println("   ---------");
        for(int i=0; i<3; i++ ){
                System.out.print("   " + this.board[i][0] + 
                                 "   " + this.board[i][1] + 
                                 "   " + this.board[i][2] + "\n" );            
        }  
        System.out.println("   ---------");

    }
    
    
    
    /**
     * This method is created to run within the main method.
     * Gives info about the problem and prints the next 
     * possible states for the initial state of the problem.
     * 
     * Asks the user to enter an initial state and 
     * prints the next possible states of that state.
     */
    
    public void go() throws FileNotFoundException{
        Scanner in = new Scanner(System.in);
        //PrintStream out = new PrintStream(new FileOutputStream("ep_output.txt"));
        //System.setOut(out);

        
        System.out.print("Eight Puzzle Problem\n"
                + "Numbers 0,1,2,..,7,8 are used where 0 denotes the empty cell.\n"
                + "Sample input: 3,2,1,5,6,7,8,4,0 (shown below)\n");
        
        EPState sample = new EPState (new int [][]{{3,2,1},{5,6,7},{8,4,0}});
        EPState sample1 = new EPState (new int [][]{{3,2,0},{6,1,4},{7,8,5}});
        EPState sample2 = new EPState (new int [][]{{3,0,2},{4,1,8},{6,5,7}});
        EPState sample3 = new EPState (new int [][]{{3,1,2},{6,8,4},{7,0,5}});
        EPState sample4 = new EPState (new int [][]{{1,4,2},{3,7,5},{6,8,0}});
        EPState sample5 = new EPState (new int [][]{{1,4,2},{5,0,8},{3,6,7}});
        
        sample.print_state();
        int s;

              
        System.out.println("Choose one of the initial states below:\n"
                + "Press 1 for {{3,2,0},{6,1,4},{7,8,5}}\n"
                + "Press 2 for {{3,0,2},{4,1,8},{6,5,7}}\n"
                + "Press 3 for {{3,1,2},{6,8,4},{7,0,5}}\n"
                + "Press 4 for {{1,4,2},{3,7,5},{6,8,0}}\n"
                + "Press 5 for {{1,4,2},{5,0,8},{3,6,7}}\n"
                + "\t");
        
        
                s = in.nextInt();
                System.out.println("You entered: "+ s);
                
                Node root;
                
                switch(s){
                
                    case 1:
                        root = new Node(sample1, null);
                        break;
                    case 2:
                        root = new Node(sample2, null);
                        break;
                    case 3:
                        root = new Node(sample3, null);
                        break;
                    case 4:
                        root = new Node(sample4, null);
                        break;
                    case 5:
                        root = new Node(sample5, null);
                        break;
                    default:  
                        System.err.println("Invalid input.");
                        return;  
                
                }
   
        Search search1 = new BreadthFirstSearch();   
        Node result_node = search1.start_search(root);
        System.out.println("\n Breadth First Search:");
        search1.print_path(result_node);

        Search search2 = new DepthFirstSearch();   
        result_node = search2.start_search(root);
        System.out.println("\n Depth First Search:");
        search2.print_path(result_node);
        
        Search search3 = new BestFirstSearch();   
        result_node = search3.start_search(root);
        System.out.println("\n Best First Search:");
        search3.print_path(result_node);
        
        Search search4 = new AStar();   
        result_node = search4.start_search(root);
        System.out.println("\n A* Search:");
        search4.print_path(result_node);
        
        
        Search search5 = new IDAStar();   
        result_node = search5.start_search(root);
        System.out.println("\n IDA* Search:");
        search5.print_path(result_node);
        
        
    }
    

    
    
    // after stage 1
    @Override
    public int hashCode(){
        int a =0;       
        a += 10000000 * this.board[0][0];
        a += 1000000 * this.board[0][1];
        a += 100000 * this.board[0][2];
        a += 10000 * this.board[1][0];
        a += 1000 * this.board[1][1];
        a += 100 * this.board[1][2];
        a += 10 * this.board[2][0];
        a += this.board[2][1];
        
     return a;   
    }
    
}
