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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author nihalvatandas
 */
public class AStar extends Search {

    private PriorityQueue<Node> open_list ;
    private static int max_open_list_size;
    private static ArrayList<Node> came_from = new ArrayList<>();

    
    /**
     * Constructor.
     * Open list returns the nodes in the order of decreasing f = g+h values
     */
    public AStar() { 
        open_list = new PriorityQueue<Node>(1,new Comparator<Node>(){
            @Override
            public int compare(Node o1, Node o2) {	
		return Double.compare( o1.get_f(), o2.get_f());
            }
        });
        max_open_list_size = 0;

    }
    
    
   
    
    @Override
    public void open_list_clear() {
        open_list.clear();
    }

    // adds node n to the open list
    @Override
    public void open_list_add(Node n) {
        if (! open_list.contains(n)){
            open_list.offer(n);
        }
        max_open_list_size = Math.max(max_open_list_size, open_list.size());
    }

    // returns true if open list is not empty
    @Override
    public boolean open_list_not_empty() {
        return !open_list.isEmpty();
    }

    // returns the next node from the open list
    @Override
    public Node next_from_open_list() {
        return open_list.remove();
    }

    @Override
    public void open_list_print() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    // returns the size of the open list
    @Override
    public int open_list_size() {
        return open_list.size();
    }

    // returns the maximum size of the open list 
    @Override
    public int max_open_list_size() {
        return max_open_list_size;
    }
    
    
    // A* pseudocode is taken from 
    // http://en.wikipedia.org/wiki/A*\_search\_algorithm
    @Override
    public Node start_search(Node root_node){
    
        int tentative_g;
        start_time = System.nanoTime();
        max_depth = 0;
        open_list_clear();         
        closed_list.clear();
        came_from.clear();
        
        open_list_add(root_node);
        
        while(open_list_not_empty()){
            Node n = next_from_open_list();
            max_depth = Math.max(max_depth, n.get_depth());
            if (n.state.goal_test() ){
                end_time = System.nanoTime();
                return n;               // returns the goal node
            }

            closed_list.add(n);
            n.set_child_nodes();
      
            for (Node c : n.children){                
                if (closed_list.contains(c)){
                    continue;
                }
                
                // computes a temporary g to see which path is of less cost
                tentative_g = (int)n.get_g() + 1;
                
                if((!open_list.contains(c)) || (tentative_g < c.get_g())){
                    c.came_from = n;
                    c.set_g((double)tentative_g);
                    c.set_f((double)(c.get_g()+c.get_h()));
                    this.open_list_add(c);
                }
            }       
        } 
        end_time = System.nanoTime();
        return null;
    
    
    
    
    }

    
    /**
     * Given the outcome of the search method, it informs about the result.
     * @param solution:  
     */
    @Override
    public void print_path(Node solution){
        if (solution == null){
            System.out.println("No solution found.");
        }
        else{
            Node current = solution;
            System.out.println("The solution path is (in reverse order): ");
            while (current != null){
                current.state.print_state();
                current = current.came_from;
            }
        System.out.println("\nelapsed time: " + this.elapsed_time() + " ms");
        System.out.println("solution found at depth " + solution.get_depth());}
        System.out.println("max depth searched: " + this.max_depth());
        System.out.println("visited nodes: " + this.n_visited_nodes());
        System.out.println("max open list size: " + this.max_open_list_size());
    }
    
}
