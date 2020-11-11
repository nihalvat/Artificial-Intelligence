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
import java.util.Stack;

/**
 * CSP solver implemented for KenKen puzzle
 * 
 */
public class CSPsolver {
 
    
    private static Stack<CSPnode> open_list = new Stack<>();
    // ordered_var keeps variables ordered according to increasing domain size
    private static PriorityQueue<Integer> ordered_var;    
    private static CSPnode root_node; 
    private static CSPnode current_node;
    
    // pre_depth : depth of the variable which is handled one before
    private static int prev_depth = -1; 
    private long start_time;
    private long end_time;
    private static long max_open_list_size = 0;
    // # of visited nodes: includes also the ones visited during 
    // the forward checking process
    private static long n_visited_nodes = 0;    
    
    
    
    /**
     * Constructor.
     * Takes the puzzle instance, creates the root node
     * Put variables in order
     * @throws Exception 
     */
    public CSPsolver(KenkenState ks) throws Exception{
        CSPsolver.root_node = new CSPnode (ks, 0);
        CSPsolver.current_node = CSPsolver.root_node;
        
        CSPsolver.ordered_var = new PriorityQueue<>(1,new Comparator<Integer>()
        {
            @Override
            public int compare(Integer o1, Integer o2) {
                return Integer.compare(domain_size((int)o1),
                                       domain_size((int)o2));
            }     
        });
        
        CSPsolver.ordered_var.clear();
        for (int i=0; i<root_node.n_of_var; i++){
            CSPsolver.ordered_var.add((Integer)i);
        }

    }
    
   
    /**
     * When a backtrack occurs, this function restores the variable list
     * @param n: first node after the backtrack
     */
    public void update_ordered_var(CSPnode n){
        
        CSPsolver.ordered_var.clear();
        CSPsolver.current_node = n;
        for (int i=0; i<n.n_of_var; i++){
           if (n.state.get_grid(i) != 0) {continue;} 
           if (n.domain.get(i).isEmpty()){continue;}
           if (i == n.grid_index)        {continue;}
           CSPsolver.ordered_var.add ((Integer)i);
        }
      
    }
  
    // returns the domain size of variable with cell number = grid_index 
    public int domain_size(int grid_index){
        return CSPsolver.current_node.domain.get(grid_index).size();
    }
    
    
    
    
    // if a dead end occur, dont add it to the open list
    // open list is designed in the depth first manner
    
    /**
     * Node expansion.
     * 
     * @param n: node to be expanded
     * @throws Exception 
     */
    
    public void expand_node(CSPnode n) throws Exception{
                
        CSPnode child;
        
        // if a backtrack happens, restore the ordered_var list
        if ( n.depth <= CSPsolver.prev_depth){    
            this.update_ordered_var(n);
        }
              
        // if there is no more variable to handle, no expansion. return.
        if(CSPsolver.ordered_var.isEmpty()){
            return;         
        }
        
        
        // var_index is the cell number of the next variable
        Integer var_index = CSPsolver.ordered_var.remove();
        // for all values in the domain of the variable
        for(Integer i: n.domain.get((int)var_index)){
            child = new CSPnode(n); // copy parent's state and domain to the child
            child.depth = n.depth + 1; // increase depth
            child.grid_index = (int)var_index;
            child.state.set_grid(child.grid_index, i); // modify the puzzle grid

            
            //this variable is handled so make its domain empty for the upcoming nodes
            child.domain.get(child.grid_index).clear();     
            CSPsolver.current_node = child;
            // if the newly assigned value satisfies all constraints
            if (child.state.is_valid3(child.grid_index)){
                // if the newly assigned value makes no other variable's domain empty
                // add it to the open list
                if (child.update_domains()){         
                    CSPsolver.open_list.add(child); 
                    CSPsolver.max_open_list_size = Math.max(CSPsolver.max_open_list_size, 
                                                   CSPsolver.open_list.size());
                }
            }     
        }
    }
    /**
     * CSP solver algorithm.
     * @return
     * @throws Exception 
     */
    public CSPnode search() throws Exception{
        
        start_time = System.nanoTime();
        open_list.clear();   
        open_list.add(CSPsolver.root_node);
        
        CSPnode cur_node;
        while (!open_list.isEmpty()){
            CSPsolver.n_visited_nodes++;
            cur_node = open_list.pop();

            if (cur_node.is_goal()){
                end_time = System.nanoTime();
                return cur_node;
            }
           
            this.expand_node(cur_node);
            CSPsolver.prev_depth = cur_node.depth; 
            // keep track of the depth to detect backtracks
        }
        
        end_time = System.nanoTime();
        return null;       
    }
    
    
    // returns running time of the CSP solver
    public double elapsed_time(){
        return (double)(end_time - start_time)/1000000;
    }
    
    
    
    // a helper function. not used during the search process     
    public void print_open_list(){
        
        Stack<CSPnode> temp = new Stack<>();
        temp.addAll(CSPsolver.open_list);
        System.out.print("   -   ");
        while (!temp.isEmpty()){
            System.out.print(" " + temp.pop().grid_index);
        } 
        System.out.println("");
        
    }
    
    
    // a helper function. not used during the search process 
    public void print_ordered_list(){
        PriorityQueue temp = new PriorityQueue(CSPsolver.ordered_var);
        System.out.print("   -   ");
        while (!temp.isEmpty()){
            System.out.print(" " + temp.poll());
        } 
        System.out.println("");
    }
    
    
    
    /**
     * Takes the output of the search() method and informs about the result
     * @param result : output of the search() method
     */
    public void print_result(CSPnode result){
        
        if (result == null){
            System.out.println("No solution found.");
        }      
        else{
            System.out.println("Solution found!");
            result.state.print_state();
        }        
        
        System.out.println("\nelapsed time: " + this.elapsed_time() + " ms");
        System.out.println("visited nodes: " + CSPsolver.n_visited_nodes);
        System.out.println("max open list size: " + CSPsolver.max_open_list_size);
    
    
    }
}
