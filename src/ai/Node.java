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
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Node is created to build the search tree nodes.
 * Each node keeps a state of the problem and several tree node attributes.
 */


public class Node {
    public final State state;             // the state that created the node
    public Node parent;             // parent of the node 
    public Set<Node> children;      // the set of child nodes 
    public int depth;               // depth of the node
    
    private double f,g,h;              // g: cost  h: heuristic
    public Node came_from;
    public int min_f_of_children = 0;
    public boolean modified = false;
    
    public Node(){
        this.state = null;
        this.parent = null;
        this.children = null;
        this.depth = 0;
        this.g = 0;   // ***********
        this.h = this.state.heuristic();
        this.f = this.g + this.h;
        this.came_from = null;
        
        //this.cost_so_far = 0;
        //this.dist_to_goal = 0;
    
    }
    
    /** 
     * Constructs a child node for the input parent_node
     * 
     * @param self_state: The state of the node to be created
     * @param parent_node: The parent of the node to be created
     * 
     */
    public Node(State self_state, Node parent_node){
        this.state = self_state;
        if (parent_node == null)
            this.depth = 0;
        else
            this.depth = parent_node.depth + 1;
        
        this.parent = parent_node;
        this.g = this.depth;   // ***********
        this.h = this.state.heuristic();
        this.f = this.g + this.h;
        
    }
    
      /**
       * First creates the next possible states for a node,
       * Then creates a new node for each child
       */
    public void set_child_nodes(){
        Set<State> states = this.state.next_states();
        this.children = new HashSet<Node>();
                
        for (State s : states){
            this.children.add( new Node(s,this) ); 
        }
    }
    
    
   
    
    /**
     * Prints the state information of the child nodes
     */
    public void print_children(){
        
        if (this.children == null){
            System.out.println("no child!");
            return;
        }
        
        for (Node n : this.children){
            n.state.print_state();        
        }
    }
    
    
    
    
    /**
     * 'Get' methods to return the values of private class members
     */
     public int get_depth(){ return this.depth;}
     public Node get_parent(){ return this.parent;}
     //public double get_cost_so_far(){ return this.cost_so_far;}
     //public double get_dist_to_goal(){ return this.dist_to_goal;}
     
     
     
     
     
     
     ///////////// after stage 1
     
     @Override
    public boolean equals(Object o) {
        
        // two nodes are equal if they have the same states
        
        if ( o==null || !(o instanceof Node ))
            return false;
	Node eps = (Node)o;
        
        return this.state.equals(eps.state);
        
    } 
     
    
    
    // after stage 1
    
    @Override
    public int hashCode(){
        return this.state.hashCode();
}    
    
    public void remove_child_node(Node c){
        boolean remove = this.children.remove(c);
        if (!remove) System.out.println("in remove_child_node: "
                + "cannot be removed!");
    }
 
    
    
    class NodeCompare implements Comparator<Node> {

        @Override
        public int compare(Node o1, Node o2) {
            if ( o1==null || o2==null )
                try {   
                    throw new Exception("cannot compare two nodes\n");
            } catch (Exception ex) {
                Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
            }   
            
//            int val1 = o1.cost_so_far + o1.dist_to_goal;
//            int val2 = o2.cost_so_far + o2.dist_to_goal;
//                        
//            return (int) Math.signum(val1-val2);      
  
            
            return Double.compare(o1.f, o2.f);
        } 
    }
    
    
   public double get_f(){ return this.f; } 
   public double get_g(){ return this.g; } 
   public double get_h(){ return this.h; } 
   public void set_f(double f){this.f = f; }

   public void set_g(double g){this.g = g; }
       
    
}
