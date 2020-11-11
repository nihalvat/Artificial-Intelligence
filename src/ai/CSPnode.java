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
import java.util.List;

/**
 *  This class is used together with CSPsolver class.
 *  CSPnode keeps the node-relevant information which are needed during CSP 
 *  search process
 *  It is implemented to work only on KenKen puzzles
 */
public class CSPnode{
    
    public KenkenState state;
    public int depth;              // depth of the node
    public int n_of_var;           // number of variables = size^2
    public int grid_index = -1;    // grid index starts from 0,...,(size^2 -1)
    public List<ArrayList<Integer>> domain = new ArrayList<>();   
                                   /* domains of the variables which are not 
                                      handled yet */ 

    
    
    // constructor 
    public CSPnode (KenkenState ks, int depth) throws Exception{
    
        this.state = new KenkenState(ks);    // copy KenkenState ks
        this.depth = depth;
        this.n_of_var = ks.size*ks.size;     // # of variables = # of cells
        this.set_domain();                   // initialize domains for this node
    
    }
    
    
    
    
    
    //copy constructor
    public CSPnode(CSPnode n){
        this.state = new KenkenState(n.state);  // copy the given state
        this.depth = n.depth;
        this.n_of_var = n.n_of_var;  

        for(int i=0; i<n_of_var; i++){          // copy the domains
            this.domain.add(i,new ArrayList<>(n.domain.get(i)));            
        }   
    }
    
    
    
    /**
     * This function initializes the domain list in the node constructor
     * @throws Exception 
     * 
     */
    
    public final void set_domain() throws Exception{
        
        ArrayList temp; 
        int cage_index;
        for (int i=0; i<this.n_of_var; i++ ){
            cage_index = this.state.index_of_cage_with_cell(i);
            temp = this.state.cages.get(cage_index).possible_values(this.state.size);
            this.domain.add(i, temp);
        }        
        
    }
    
    

    
    
   
    
    /**
     * When a variable assigned a value, this function removes that value 
     * from the domains of the variables which sit on the same row and column 
     * with the assigned one.
     * If the domain of a not-yet-handled variable becomes empty
     * @return false
     * otherwise return true
     */
    
    public boolean update_domains(){
        
        int index_row, index_col;
        int value_to_be_removed = this.state.get_grid(this.grid_index);  //[this.grid_index];
    
        // find row and column order of the modified cell
        int row_num = this.grid_index / this.state.size;
        int col_num = this.grid_index % this.state.size;
        
        for(int j=0; j<this.state.size; j++){     // 0, .. , size-1
            // find indexes of the cells which sits on the same row 
            // or column with the modified cell
            index_row = (this.state.size * row_num) + j; 
            index_col = (this.state.size * j) + col_num; 
                    
            // remove the newly added value from the domains of 
            // the cells in the same row or column 
            this.domain.get(index_row).remove((Integer)value_to_be_removed);
            this.domain.get(index_col).remove((Integer)value_to_be_removed);
        }    
        
        // if there exists a (yet unassigned) cell with empty domain, return false
        for (int j=0; j<this.n_of_var; j++){
            if (this.domain.get(j).isEmpty() && (this.state.get_grid(j) == 0)){
                return false;
            }
        }
        return true;       
    }
    
    
    // Returns true if the grid is completely done and the configuration is valid
    public boolean is_goal() throws Exception{
        return this.state.is_valid2();
    }
    
    
    
    // a helper function
    // not used during the search
    public void print_domain(){
        
        for (int i=0; i<this.domain.size(); i++){
            System.out.print("\ndomain[" + i + "] = ");
            for(int j=0; j<this.domain.get(i).size(); j++){
                System.out.print(this.domain.get(i).get(j) + " ");
            }
        }   

    
    }
    
}
