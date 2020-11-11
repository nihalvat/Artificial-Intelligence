
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
import java.util.Set;
import java.util.HashSet;
import java.util.*;


/**
 * This class is created for the states of the water jug problem,
 * assuming water source is available
 * There are two jugs with capacities 3- and 5- gallons
 * Each state keeps the amount of water that small and big jugs have
 */

public class WJState implements State {


	private int small = 0;
	private int big = 0;   
               
        public WJState(){}   
        
        
        /**
         * Constructor with initialization values other than zero.
         * @param small: amount of water in 3-gallon jug
         * @param big: amount of water in 5-gallon jug
         */
        public WJState(int small, int big) {
		this.small = small;
		this.big = big;
	}


        
        /**
         * Returns the set of all possible next states of a state.
         */
        
        @Override
	public Set<State> next_states() {
            
            Set<State> moves = new HashSet<State>();
            
            // fill the big jug to its full capacity
            moves.add(new WJState(3,big));
            
            // fill the small jug to its full capacity
            moves.add(new WJState(small,5));
            
            // empty the small jug
            moves.add(new WJState(0,big));
            
            // empty the big jug
            moves.add(new WJState(small,0));
            
            // How much of the water in the small jug 
            // can be added to the big one ?
            int diff = Math.min(small, 5-big);
            
            // if possible, transfer the diff amount from small to big one
            if (diff>0){
                    moves.add( new WJState( small-diff, big+diff ));
            }
            
            // How much of the water in the big jug 
            // can be added to the small one ?
            diff = Math.min(3-small, big);

            // if possible, transfer the diff amount from big to small one            
            if (diff>0){
                    moves.add(new WJState(small+diff, big-diff));
            }

            return moves;
	}

        /**
         * The goal is to have 1 gallon water in the small jug
         */
      @Override
	public boolean goal_test() {
		return small==1;
	}
	

	
         /**
	 * Returns true if two states are equal.
	 */
        @Override
	public boolean equals(Object o) {
            
        // two states are equal if the amount of water 
        //within the jugs are equal    
            if (o==null || !(o instanceof WJState))
                return false;
          
            WJState wjs = (WJState)o;
            return ((this.small == wjs.small) && (this.big == wjs.big));
	}
	

        /**
         * Prints the information about the state 
         */
        @Override
        public void print_state(){
            	System.out.print( "(" + small + ", " + big + ")\n"); 
        }

        
    /**
     * Prior states to the goal (1,#) are: (0,1) (2,4) (3,3)
     * Heuristic is the minimum of the current state's distance to each of them
     */    
    @Override
    public double heuristic() {
        
        if (this.small == 1)
            return 0;
        else
            return Math.min((Math.abs(this.big-3) + Math.abs(this.small-3) +1),  // 5
            (Math.min((Math.abs(this.big-4) + Math.abs(this.small-2)+1),        // 12
            (Math.abs(this.big-1) + this.small +1))));                           // 30
                                                                                // 13    
//            return Math.abs(this.big-3) + Math.abs(this.small-3);
    }

    
    

    /**
     * This method is created to run within the main method.
     * Gives info about the problem and prints the next 
     * possible states for the initial state of the problem.
     * 
     * Asks the user to enter an initial state and 
     * prints the next possible states of that state.
     */
    
    public void go() throws FileNotFoundException {
        
        int s = 0, b = 0;
        Scanner in = new Scanner(System.in);
        //PrintStream out = new PrintStream(new FileOutputStream("output.txt"));
        //System.setOut(out);

        
        System.out.print("Water Jug Problem with 3- and 5-gallon jugs.\n"
                + "States are shown by (s, b) where s and b indicate the amount of"
                + " water in the small and big jugs, respectively.\n"
                + "Enter an initial value to see the possible next states\n"
                + "\tfor the small jug (an integer within [0,3]): ");
        s = in.nextInt();
        if ((s<0)||(s>3)){
            System.err.println("Invalid input.");
            return;             
        }
        System.out.println("You entered: "+ s);
        System.out.print("\tfor the big jug (an integer within [0,5]): ");
        b = in.nextInt();
        if ((b<0)||(b>5)){
            System.err.println("Invalid input.");
            return;
        }
        System.out.println("You entered: "+ b);
        
        this.small = s;
        this.big = b;
        
        Node root = new Node(this, null);

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

    @Override
    public int hashCode(){
        return (this.big*10)+this.small;
    }
    
}