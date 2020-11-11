
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

import java.util.Set;


/**
 * This interface is created to provide a general frame for 
 * the methods commonly used for all problems regardless of the problem type.
 * These methods will be needed to create and traverse the search tree.
*/


public interface State {

        
        // This function can be called for a particular problem state 
        // and returns all possible next states within a State Set. 
	public Set<State> next_states();
        
        // Checks if the state is the goal state for the problem. 
	public boolean goal_test();  
        // returns the value of the heuristic function
	public double heuristic();
        // This method is overridden to check state equality
        @Override
        public boolean equals(Object o); 
        // a print function created according to each problem's state members
        public void print_state();
        
        /// after stage 1
//        @Override
//        public int hashCode();
//        
}
