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


/**
 * This class is created for the states of the missionaries and cannibals problem 
 * Each state keeps the number of missionaries and cannibals 
 * on each side of the river and the location of the boat
 */

public class MCState implements State {

	private int left_mis = 3;   /* number of missionaries on the 
                                       left hand side (LHS) of the river */
	private int left_can = 3;   // number of cannibals on LHS of the river
	private int right_mis =0;  // number of missionaries on RHS of the river
	private int right_can =0;  // number of cannibals on RHS of the river
	private boolean boat_on_left = true;    // the boat is either on LHS or RHS

        
        
        
        public MCState() {}
	
        
        
        
	/**
         * Constructor to create a state from the previous state
	 * @param prev: previous (parent) state
         * @param  mis_on_boat: number of missionaries to get across the river
         * @param  can_on_boat: number of cannibals to get across the river
	 */
	private MCState(MCState prev, 
                int mis_on_boat, int can_on_boat) {
		
		this.boat_on_left = !prev.boat_on_left;
		if (this.boat_on_left) {
			mis_on_boat = - mis_on_boat;
			can_on_boat = - can_on_boat;
		}
		this.left_mis  = prev.left_mis  - mis_on_boat;
		this.right_mis = prev.right_mis + mis_on_boat;
		this.left_can  = prev.left_can  - can_on_boat;
		this.right_can = prev.right_can + can_on_boat;
	}

	/**
	 * Returns the set of all possible next states of a state. 
	 */
	public Set<State> next_states() {
		Set<State> moves = new HashSet<>();
                MCState newstate;
                
                newstate = new MCState(this, 1, 0);
                if (newstate.is_valid()){ moves.add(newstate);}
                    
                newstate = new MCState(this, 2, 0);
                if (newstate.is_valid()){ moves.add(newstate);}        
                
                newstate = new MCState(this, 0, 1);
                if (newstate.is_valid()){ moves.add(newstate);}  
                
                newstate = new MCState(this, 0, 2);
                if (newstate.is_valid()){ moves.add(newstate);}  
                
                newstate = new MCState(this, 1, 1);
                if (newstate.is_valid()){ moves.add(newstate);}  

                return moves;
              
		
	}
        
        /**
         * Returns if a 'candidate' next state is a valid state or not
         * Number of cannibals and missionaries on each side are compared
         * Number of mis.s must not exceed number of can.s
         */
        private boolean is_valid() {
		return ((left_mis <= left_can || left_can == 0) &&
			(right_mis <= right_can || right_can == 0) &&
			(left_mis >= 0 && left_can >= 0) &&
			(right_mis >= 0 && right_can >= 0));			
	}
        

	/**
	 * The goal state is to have all missionaries and cannibals
         * on RHS of the river
         * returns true if no one is left on LHS
	 */
        @Override
	public boolean goal_test() {
		return left_mis==0 && left_can==0;
	}
	
//	/**
//	 * Returns a heuristic approximation of the number of moves required
//	 * to solve this problem from this state.  This is implemented as
//	 * 2 times the number of people on the east side
//	 */
//        @Override
//	public double heuristic() {
//		return 2*(left_mis+left_can);
//	}
        
        
	/**
	 * Returns true if two states are equal.
	 */
        @Override
	public boolean equals(Object o) {
            
            // two states are equal if number of can. and mis. on each side
            // and the boat' location are the same
            
            if (o==null || !(o instanceof MCState))
                    return false;
            MCState ms = (MCState)o;
          
            return ((this.boat_on_left == ms.boat_on_left) &&
                    (this.left_mis == ms.left_mis) &&
                    (this.left_can == ms.left_can));
        }
	

        /**
         * Prints the information about the state 
         */
        @Override
        public void print_state(){
            System.out.print("("+left_mis+", "+left_can+")  ");
            if (boat_on_left){ System.out.print(" B |   ");}
            else{ System.out.print("   | B ");}
            System.out.println("("+right_mis+", "+right_can+")");                       
	}

        
        /**
         * This method is created to run within the main method.
         * Gives info about the problem and prints the next 
         * possible states for the initial state of the problem.
         */
        public void go() throws FileNotFoundException{
        
            //PrintStream out = new PrintStream(new FileOutputStream("mc_output.txt"));
            //System.setOut(out);

            System.out.print("Missionaries and Cannibals Problem with 3 missionaries and "
                    + "3 cannibals on the left side of the river.\n"
                    + "States are shown by (m_l, c_l) B |   (m_r, c_r) where\n"
                    + "\tm_l: missionaries on the left side\n"
                    + "\tc_l: cannibals on the left side\n"
                    + "\tm_r: missionaries on the right side\n"
                    + "\tc_r: cannibals on the right side\n"
                    + "\tB and | indicate the position of the boat and the river, respectively.\n"
                    + "\n Initial state is (3, 3) B |   (0, 0)\n\n");

        
            
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

        
    /**
     * This method will be implemented to return heuristic function's values
     */    
    @Override
    public double heuristic() {
        return 2*(left_mis + left_can);
    }
        
        
}
