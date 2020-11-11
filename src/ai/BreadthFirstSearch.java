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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Queue;

/**
 *
 * @author nihalvatandas
 */
public class BreadthFirstSearch extends Search {
    
    private Queue<Node> open_list = new LinkedList<Node>();
    private static int max_open_list_size;
    
    public BreadthFirstSearch (){
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
        return (! open_list.isEmpty());
    }

    @Override
    public Node next_from_open_list() {
        return open_list.remove();

    }
    
    
    @Override
    public void open_list_print(){
    
        System.out.println("open list size: " + open_list.size());
 
    }

    @Override
    public int open_list_size() {
        return open_list.size();
    }

    @Override
    public int max_open_list_size() {
        return max_open_list_size;
    }
    
    
}
