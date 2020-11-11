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

import java.util.PriorityQueue;
import java.util.Stack;

/**
 *
 * @author nihalvatandas
 */
public class IDAStar extends DepthFirstSearch {
    
    private static int count_visited_nodes;
    private static int max_of_max_open_list_size;
    long ida_start_time;// = System.nanoTime();
    long ida_end_time;// = System.nanoTime();
    private static int max_of_max_depth;

    
    public IDAStar (){ 
        count_visited_nodes = 0;
        max_of_max_open_list_size = 0;
        max_of_max_depth = 0;
    }
        
    
    @Override
    public Node start_search(Node root_node){
        ida_start_time = System.nanoTime();
        Node result;
        int limit = (int)root_node.get_f();

        while(true) {
            DepthFirstSearch dfs = new DepthFirstSearch (limit);
            result = dfs.start_search(root_node);
            limit = dfs.get_next_cost(limit);

            count_visited_nodes += dfs.n_visited_nodes();
            max_of_max_open_list_size = Math.max(DepthFirstSearch.max_open_list_size , 
                    max_of_max_open_list_size);
            max_of_max_depth = Math.max(max_of_max_depth, dfs.max_depth());
            
            if (result != null){
                ida_end_time = System.nanoTime();
                return result; 
            }
            if ( limit < 0){ 
                ida_end_time = System.nanoTime();
                return null; 
            }     
            
        } 
            
    }
    
    @Override
    public int n_visited_nodes(){ 
        return count_visited_nodes;
    }

    @Override
    public int max_open_list_size() {
        return max_of_max_open_list_size;
    }
    
    @Override
    public double elapsed_time(){
        return (double)(ida_end_time - ida_start_time)/1000000;
    }
    
    @Override
    public int max_depth(){
        return IDAStar.max_of_max_depth;
    }
    
}
