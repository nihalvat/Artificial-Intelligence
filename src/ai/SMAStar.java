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
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 *
 * @author nihalvatandas
 */
public class SMAStar extends Search{
    
    int limit = 5;


    private PriorityQueue<Node> open_list;
    private static int max_open_list_size;
    private static ArrayList<Node> came_from = new ArrayList<>();

    public SMAStar() { 
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
    
    public Node deepest_least_f_from_open_list (){
        
        Iterator<Node> it = open_list.iterator();
        Node n;
        while(it.hasNext()){
            n = it.next();
            if (n.depth == max_depth){
                return n;
            }
        }
        return null;
    }

    // returns one of the unmodified child of Node n
    private Node next_unmodified(Node n){
        for(Node c: n.children){
            if (!c.modified)
                return c;
        }
        return null;
    }
    
    private boolean all_modified(Node n){
        for(Node c: n.children){
            if (!c.modified)
                return false;
        }
        return true;
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
        return max_open_list_size;
    }
    
    
    
    @Override
    public Node start_search(Node root_node){
    
        start_time = System.nanoTime();
        max_depth = 0;
        open_list_clear();         
        closed_list.clear();
        came_from.clear();
        Node succ;
        
        open_list_add(root_node);
        //closed_list.add(root_node);
        
        while(open_list_not_empty()){
            System.out.println("open list size " + open_list.size());
            Node n = deepest_least_f_from_open_list();
            max_depth = Math.max(max_depth, n.get_depth());
            if (n.state.goal_test() ){
                end_time = System.nanoTime();
                return n;               // returns the goal node
            }

            
            //closed_list.add(n);
            n.set_child_nodes();
          
            succ = next_unmodified(n);
            succ.set_f(Math.max(n.get_f(), succ.get_g() + succ.get_h()));
            succ.modified = true;
            
            if (succ.get_f() < n.min_f_of_children){
                n.min_f_of_children = (int)succ.get_f();
            }

            if (all_modified(n)){
                backup(n);
            }
            
            boolean all_in_memory = true;
            for (Node c: n.children){
                if (!open_list.contains(c)){
                    all_in_memory = false;
                }
            }
            
            if (all_in_memory){
                open_list.remove(n);
            }
            
                            

            
            if (open_list.size() > limit){
                this.remove_last();
            
            }
            open_list.offer(succ);            
                  
        } 
        end_time = System.nanoTime();
        return null;
    
    
    
    
    }

    private void backup (Node n){
        // if n is completed and has a parent
        if ((n.children.isEmpty()) && (n.parent != null)){
            n.set_f((double)n.min_f_of_children); 
            if (n.modified){
                backup(n.parent);
            }
        }
    }
    
    // removes the last element from the open_list
    private void remove_last(){
        Iterator<Node> it = open_list.iterator();
        Node n = null;        
        while(it.hasNext()){
            n = it.next();
        }
        open_list.remove(n);
        n.parent.children.remove(n);
    
    }
    
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
