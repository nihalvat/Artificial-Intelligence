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

import java.util.HashSet;
import java.util.Set;

/**
 * 
 * 
 */
public abstract class Search {
    
    
    protected Set<Node> closed_list = new HashSet<>();
    protected long start_time;
    protected long end_time;
    protected static int max_depth ;

    
    public Node start_search(Node root_node){
    
        start_time = System.nanoTime();
        max_depth = 0;
        open_list_clear();         
        closed_list.clear();
        
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
                if (!closed_list.contains(c)){
                        open_list_add(c);
                }
            }       
        } 
        end_time = System.nanoTime();
        return null;
    }
    
    

    
    public void print_path(Node solution){
        if (solution == null){
            System.out.println("No solution found.");
        }
        else{
            Node current = solution;
            System.out.println("The solution path is (in reverse order): ");
            while (current != null){
                current.state.print_state();
                current = current.get_parent();
            }
        System.out.println("\nelapsed time: " + this.elapsed_time() + " ms");
        System.out.println("solution found at depth " + solution.get_depth());}
        System.out.println("max depth searched: " + this.max_depth());
        System.out.println("visited nodes: " + this.n_visited_nodes());
        System.out.println("max open list size: " + this.max_open_list_size());
    }
    
    
    
    
    public int n_visited_nodes(){ return this.closed_list.size();}
    public double elapsed_time(){ return (double)(end_time - start_time)/1000000;}
    public int max_depth(){ return Search.max_depth;}
    
    public abstract int max_open_list_size();
    public abstract void open_list_clear();
    public abstract void open_list_add(Node n);
    public abstract boolean open_list_not_empty();
    public abstract Node next_from_open_list();
    public abstract void open_list_print();
    public abstract int open_list_size();
    
}
