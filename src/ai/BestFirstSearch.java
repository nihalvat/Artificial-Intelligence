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

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 *
 * @author nihalvatandas
 */
public class BestFirstSearch extends Search {

    private PriorityQueue<Node> open_list ;
    private static int max_open_list_size;

    public BestFirstSearch() { 
        open_list = new PriorityQueue<Node>(1,new Comparator<Node>(){
            @Override
            public int compare(Node o1, Node o2) {	
		return Double.compare( o1.get_h(), o2.get_h());
            }
        });
        max_open_list_size = 0;

    }
    
    
    
    @Override
    public void open_list_clear() {
         open_list.clear();
    }

    @Override
    public void open_list_add(Node n) {
        if (! open_list.contains(n)){
            open_list.offer(n);
        }
        max_open_list_size = Math.max(max_open_list_size, open_list.size());
    }

    @Override
    public boolean open_list_not_empty() {
        return !open_list.isEmpty();
    }

    @Override
    public Node next_from_open_list() {
        return open_list.remove();
    }

    @Override
    public void open_list_print() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public int open_list_size(){ return open_list.size();}

    @Override
    public int max_open_list_size() {
        return max_open_list_size;
    }
    
    
    
    
}
