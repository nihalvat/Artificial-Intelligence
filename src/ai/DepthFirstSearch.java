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
public class DepthFirstSearch extends Search{

    private Stack<Node> open_list = new Stack<Node>(); 
    protected PriorityQueue<Integer> ida_cost_list = new PriorityQueue<>();
    private int cutoff_depth = 15;
    protected static int max_open_list_size;

    
    public DepthFirstSearch (){
        DepthFirstSearch.max_open_list_size = 0;
    }
    
    public DepthFirstSearch (int depth_limit){
        this.cutoff_depth = depth_limit;
        DepthFirstSearch.max_open_list_size = 0;
        
    }
    
    @Override
    public void open_list_clear() {
        open_list.clear();
    }

    @Override
    public void open_list_add(Node n) {
        int f;
        if ( !open_list.contains(n) && (n.get_depth()<= cutoff_depth)){
            open_list.push(n);
            f = (int) n.get_f();
            if ( ! ida_cost_list.contains(f)){
                ida_cost_list.add(f);
            }
            
        }
        max_open_list_size = Math.max(max_open_list_size, open_list.size());

    }

    @Override
    public boolean open_list_not_empty() {
        return ( !open_list.empty() );
    }

    @Override
    public Node next_from_open_list() {
        return open_list.pop();
    }
    
    protected int get_next_cost(int prev_cost){
        for(Integer n: ida_cost_list){
            if (n > prev_cost){
                return n;
            }   
        }
        return -1;    
    }
    

    @Override
    public void open_list_print() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int open_list_size() {
        return open_list.size();
    }

    @Override
    public int max_open_list_size() {
        return DepthFirstSearch.max_open_list_size;
    }
    
}
