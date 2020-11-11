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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * This class is created for the kenken puzzle. 
 * Each state keeps 
 *       a puzzle grid: a 1D array is used 
 *                     (performing the operations as if it's a 2D)
 * 
 *       size: size of the grid (e.g. size=5 if a 5x5 grid is used)
 *       
 *       cages: list of cages (this info defines the puzzle) 
 *              since cage info is dynamically changing, 
 *              a list is added to each state
 * 
 */



public class KenkenState implements State{

    public int size;           // size of the board
    private int[] grid;         // game board
    public ArrayList<Cage> cages = new ArrayList<>();  // puzzle definition
    
    
    
    
    public KenkenState(){}
    
    
    /**
     * A copy constructor
     */
    public KenkenState( KenkenState ken ){
    
        this.size = ken.size;
        this.grid = new int[this.size*this.size];
        
        System.arraycopy(ken.grid, 0, this.grid, 0, ken.grid.length);
        for (Cage c : ken.cages) {
            this.cages.add(new Cage (c));
        }
    }
    
    /**
     * Puzzle definitions are read from a .txt file
     * This method reads data (given in a certain format),
     * in the .txt file to define the puzzle
     * 
     * Puzzle samples are available in the input folder
     */
    public void init(String filename) throws FileNotFoundException {
    
        ArrayList<String> content = new ArrayList<>();
        Scanner s = null, s2= null;
        int val;
        char op;
    
        
        // read the file into a string, use tokens to split it
        try {
            s = new Scanner(new BufferedReader(new FileReader(filename)));
            s.useDelimiter("size");
            while (s.hasNext()) { 
                content.add(s.next());
            }
        } finally { if (s != null) s.close();}
        
        s.reset();  
        s = new Scanner(content.get(0));
        
        // set the size
        this.size = s.nextInt();
        
        // read cage info: each line has info of a separate cage
        this.grid = new int[this.size * this.size];
        s.reset();  
        s = new Scanner(content.get(1)); 
        s.useDelimiter("\n");
        content.clear();
        
        while (s.hasNext()){ 
                content.add(s.next());
        } 
        
        
        
        // now read the cell indices into the list using tokens
        ArrayList<Integer> pos = new ArrayList<>();
        
        for (int i=0; i<content.size(); i++){
            s.reset(); s = new Scanner (content.get(i));
            s.useDelimiter(";");        
            
            val =  s.nextInt();
            op = s.next().charAt(0);
            s2 = new Scanner(s.next());
            s2.useDelimiter(",");
            pos.clear();
            while (s2.hasNext()){
                pos.add(s2.nextInt());
            }
            // create the new cage and add it to the list
            this.cages.add(new Cage (op,val,pos));     
        }
     
    }
    
    
    
    /**
     * Returns the set of all possible next states of a state. 
     * 
     * New states are created in increasing index order :
     * (pictorially from top left corner to bottom right corner of the grid)
     * 
     *      Find the first empty cell (marked by 0)
     *      Try all values from 1 to size
     *      - If it is a possible value according to the cage operator
     *      - And if no repeating number appears in the same row and column
     *       --> add the new state to the list
     * 
     */
    @Override
    public Set<State> next_states() {

        Set<State> moves = new HashSet<State>();
        int cage_num;
        
        for (int i = 0; i<this.size*this.size; i++){
            if (this.grid[i] == 0){  // find the first empty cell in grid
                for(int j = 1; j <= this.size; j++){
                    
                    KenkenState newstate = new KenkenState (this); // implement
                    newstate.grid[i] = j;
                    cage_num = this.index_of_cage_with_cell(i);
                    newstate.cages.get(cage_num).n_empty_cells--;
                    try {
                        if ( newstate.is_valid(i) ){
                         moves.add(newstate);                         
                        }
                    } catch (Exception ex) {
                        Logger.getLogger(KenkenState.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        return moves;
    }
    @Override
    public boolean goal_test() {
        return (this.heuristic() == 0);
                    
    }
    @Override
    public double heuristic() {
         return ( this.get_n_remaining_cols() 
                + this.get_n_remaining_rows()
                + this.get_n_remaining_cages());
                 
    }
    
    
    /**
     * Prints the information about the state (grid configuration)
     */
    @Override
    public void print_state() {
        
        System.out.println("----------------");
        
        for (int i =0; i<this.size*this.size; i++){
            System.out.print(this.grid[i]+ "  ");
            if((i+1)%this.size == 0){System.out.println("");}     
        }
        System.out.println("----------------");

    
    }
  
    
    
    
    
    /**
     * Returns true if two states are equal.
     */
    @Override
    public boolean equals (Object o){
        
        // two states are equal if their grid content are the same.
        // this implies the equality of the cage content, too.
        
        if (o==null || !(o instanceof KenkenState))
            return false;
        else{
            KenkenState ken = (KenkenState) o;
            return Arrays.equals(this.grid, ken.grid);
        }

    }
    
    
    /**
     * checks all columns if any duplicate value appears
     * @return true if there is no duplicate
     * otherwise false
     */
    public boolean no_repeat_in_cols(){
        
        int[] checker = new int[this.size+1];
        int index;
    
        for (int i=0; i<this.size; i++){        // order of column: 0, 1,..,n-1
            Arrays.fill(checker,0);
            for(int j=0; j<this.size; j++){     // 0, .. , size-1
                index = (this.size * j) + i; 
                checker[grid[index]]++;         
                if ( (grid[index] != 0) &&  (checker[grid[index]]>1) )
                    return false;
            }
        }
        return true;
    } 
    
   
    /**
     * checks all rows if any duplicate value appears
     * @return true if there is no duplicate
     * otherwise false
     */
    public boolean no_repeat_in_rows(){
        
        int[] checker = new int[this.size+1];
        int index;
    
        for (int i=0; i<this.size; i++){        // order of row: 0, 1,..,n-1
            Arrays.fill(checker,0);
            for(int j=0; j<this.size; j++){     // 0, .. , size-1
                index = (this.size * i) + j; 
                System.out.print(index + " ");
                checker[grid[index]]++;         
                if ( (grid[index] != 0) &&  (checker[grid[index]]>1) )
                    return false;
            }
            System.out.println("");
        }
        return true;
    } 
    
    
    
    /**
     * Returns true if there is no repeating number on the specified column
     * @param col_num: index of the column to be checked
     */
    public boolean no_repeat_in_col(int col_num){
        
        // an array created for all possible values in a column
        // including zero
        int[] checker = new int[this.size+1];
        int index;
    
        
        // traverse the column to see each cell's value
      
        for(int j=0; j<this.size; j++){     // 0, .. , size-1
            index = (this.size * j) + col_num; 
            checker[grid[index]]++;     
            
            // if a non-zero value appears more than once, return false
            if ( (grid[index] != 0) &&  (checker[grid[index]]>1) )
                return false;
        }
        
        return true;
    } 
    
    /**
     * Returns true if there is no repeating number on the specified row
     * @param row_num: index of the column to be checked
     */
    public boolean no_repeat_in_row(int row_num){
        
        // an array created for all possible values in a column
        // including zero
        
        int[] checker = new int[this.size+1];
        int index;
    
        // traverse the column to see each cell's value
        
        for(int j=0; j<this.size; j++){     // 0, .. , size-1
            index = (this.size * row_num) + j; 
            checker[grid[index]]++;     
            
            // if a non-zero value appears more than once, return false
            if ( (grid[index] != 0) &&  (checker[grid[index]]>1) )
                return false;
        }        
        return true;
    }
      
    
    
    
    /**
     * If a single-cell cage appears, it is directly initialized
     * to its value, no search needed.
     * After its value is set, it is removed from the cage list.
     */    
    public void fill_single_cell_cages(){
        Cage c;    
        Iterator iter = this.cages.iterator();

        while (iter.hasNext()) {
            c = (Cage) iter.next();
            if( c.op == '=' ){
                this.grid [c.pos.get(0)] = c.value;
                iter.remove();
            }
        }
    }    
 
    
    
    
    
    /**
     * Given the index of a cell, 
     * returns the index of the cage that owns the cell
     * @param grid_index: index of the cell on the grid
     */
    public int index_of_cage_with_cell(int grid_index){
    
        Cage c;    
        Iterator iter = this.cages.iterator();
        
        while (iter.hasNext()) {
            c = (Cage) iter.next();
            if( c.pos.contains(grid_index) ){
                return this.cages.indexOf(c);
            }
        }
        
        throw new IllegalArgumentException("A cell number must be < " 
                  + this.size*this.size + ".\n "
                + "No repeating cell numbers allowed.\n"
                + "Input file might not be well-formatted." );
     }

    
    
    
    
    
    
    
    
    
    
    /**
     * Checks the specified cage if the values in it are valid.
     * Assumes that the cage has at least one non-empty cell.
     * (this assumption is checked before the function call)
     * Number of empty cells of the cage might be more than 0.
     * @param cage_index: index of the cage to be checked.
     */
    public boolean plus_check(int cage_index) {
        int sum = 0;
        int i;
        Cage c = this.cages.get(cage_index);
        
        // compute the sum of all values in the cage
        for (Iterator<Integer> it = c.pos.iterator(); it.hasNext();) {
            i = it.next();    
            sum += this.grid[i];
        }        
        
        // values must not be grater than the result
        // their sum must not be greater than the result
        // if there is an empty cell, sum of the values must be less than the result
        return ( (c.value - sum <= c.n_empty_cells * this.size) &&
                ((sum == c.value) && (c.n_empty_cells == 0)  ||  
                (sum < c.value) && (c.n_empty_cells > 0)));                       
    }
 
    
    
    
    
    
    
    
    
    /**
     * Checks the specified cage if the values in it are valid.
     * Assumes that the cage has at least one non-empty cell. 
     * (this assumption is checked before the function call)
     * Number of empty cells of the cage might be more than 0.
     * @param cage_index: index of the cage to be checked.
     */
    public boolean minus_check(int cage_index) throws Exception {
        
        // a cage with a minus operator must have exactly two cells
        
        Cage c = this.cages.get(cage_index);
        if (c.pos.size() != 2){
            throw new Exception ("Two operands needed for subtraction cage\n");
        }
               
        // subtract the values in the cage
        // if there is only one value, it computes the that value
        // since empty cells are zero
        int result = Math.abs( this.grid[c.pos.get(0)] - this.grid[c.pos.get(1)]);
             
        
        if(c.n_empty_cells == 1){
            // if one cell is empty, the value must satisfy these conditions:
            return (result - c.value > 0) || (result + c.value <= this.size);              
        } else {       
            // if there is no empty cell
            return (result == c.value);
        }   
    }

    
    
    
    
    /**
     * Checks the specified cage if the values in it are valid.
     * Assumes that the cage has at least one non-empty cell.
     * (this assumption is checked before the function call)
     * Number of empty cells of the cage might be more than 0.
     * @param cage_index: index of the cage to be checked.
     */
    public boolean product_check(int cage_index) {
        
        Cage c = this.cages.get(cage_index);
        int i;
        int product = 1;
        
        // compute the product of all values in the cage
        for (Iterator<Integer> it = c.pos.iterator(); it.hasNext();) {
            i = this.grid[it.next()];
            if (i!=0) { product *= i; } 
        }

        // each value must divide the result
        // if there is an empty cell, 
        // the product of the values must divide the result
        
        return ((( c.n_empty_cells>0 ) && ( c.value % product == 0))  ||
                (( c.n_empty_cells == 0) && ( c.value == product )));
        
    }
    
    
    
    /**
     * Checks the specified cage if the values in it are valid.
     * Assumes that the cage has at least one non-empty cell.
     * (this assumption is checked before the function call)
     * Number of empty cells of the cage might be more than 0.
     * @param cage_index: index of the cage to be checked.
     */
    public boolean division_check(int cage_index) throws Exception {
    
        // a cage with a division operator must have exactly two cells

        Cage c = this.cages.get(cage_index);
        if (c.pos.size() != 2){
            throw new Exception ("Two operands needed for subtraction cage.\n");
        }
        
        // find the min and max values in the cage
        
        int max_num,min_num;
        
        max_num = Math.max(this.grid[c.pos.get(0)], this.grid[c.pos.get(1)]);
        min_num = Math.min(this.grid[c.pos.get(0)], this.grid[c.pos.get(1)]);
       
        
        // if one cell is empty, min = 0
        if(c.n_empty_cells == 1){
            // the other value must satisfy:
            return (max_num % c.value == 0) || (max_num*c.value <= this.size);           
        }else {
            // if no empty cells, we expect the result
            return ((double)max_num / (double) min_num == (double)c.value );
        }
    }

    
    
    
    
    
    /**
     * When computing all possible values for a particular cell, 
     * this method tells if a value is possible for that cell.
     * Returns true if possible.
     * 
     * Validity is decided according to 3 conditions:
     *      - the value satisfies operator constraints
     *      - the column of the cell has no repeating value
     *      - the row of the cell has no repeating value
     * 
     * @param grid_index: index of the cell being modified
     * @throws java.lang.Exception
     */
    public boolean is_valid(int grid_index) throws Exception{
        
        // compute rown and column bunber of the cell
        
        int row_num = grid_index / this.size;    // row numbers: 0, .. , size-1
        int col_num = grid_index % this.size;    // col numbers: 0, .. , size-1
        
        // find the cage which the cell belongs to
        int cage_index = this.index_of_cage_with_cell(grid_index);
        Cage c = this.cages.get(cage_index);
        
        
        boolean flag;        
        
        if (c.n_empty_cells < c.pos.size()){ // at least one cell is modified

                switch(c.op){
                case '+': flag = this.plus_check(cage_index); break;
                case '-': flag = this.minus_check(cage_index); break;
                case '*': flag = this.product_check(cage_index); break;
                case '/': flag = this.division_check(cage_index); break;
                default: throw new Exception("in cage_check function: "
                                              + "wrong operator");
                    // we removed the one-cell-cage during init
                }
                
                
                if (flag){
                    return (this.no_repeat_in_col(col_num) && 
                            this.no_repeat_in_row(row_num) );
                
                }else {return false;}
         }else {throw new Exception("checking unmodified cage\n");}     
    }    
     
    
    /**
     * Returns true if the grid is completely done and the configuration is valid.
     * Number of empty cells must be 0 in all cages for returning a true.
     * @throws Exception 
     */
    
    public boolean is_valid2() throws Exception{
    
        Cage c;
        boolean flag = true;
        for (int i=0; i<this.cages.size(); i++){
            c = this.cages.get(i);
            switch(c.op){
                case '+': 
                    
                    flag = flag && this.final_plus_check(i);
                    //System.out.println(i + " + flag " + flag);
                    break;
                    
                case '-':  
                    flag = flag && this.final_minus_check(i);
                    //System.out.println(i + " - flag " + flag);
                    break;
                
                case '*':  
                    flag = flag && this.final_product_check(i);
                    //System.out.println(i + " * flag " + flag);
                    break;
                
                case '/':  
                    flag = flag && this.final_division_check(i);
                    //System.out.println(i + " / flag " + flag);
                    break;
                
                case '=':
                    flag = flag &&  true; 
                    //System.out.println(i + " = flag " + flag);
                    break;
                default: 
                    flag = flag &&  true;
                    // we removed the one-cell-cage during init
                }
        
        }
        return flag;
    
    }
    
    
    /**
     * When a cell is assigned a value, this function checks if the value
     * satisfies all constraints.
     * Empty cells in the grid are allowed. 
     * @param grid_index: cell number of the variable
     * @return true if all constraints are satisfied. otherwise false.
     * @throws Exception 
     */
    
     public boolean is_valid3(int grid_index) throws Exception{
        
        // compute rown and column bunber of the cell
        
        int row_num = grid_index / this.size;    // row numbers: 0, .. , size-1
        int col_num = grid_index % this.size;    // col numbers: 0, .. , size-1
        
        // find the cage which the cell belongs to
        int cage_index = this.index_of_cage_with_cell(grid_index);
        Cage c = this.cages.get(cage_index);
        
        
        if (c.n_empty_cells == 0){

            boolean flag;        

                    switch(c.op){
                    case '+': flag = this.plus_check(cage_index); break;
                    case '-': flag = this.minus_check(cage_index); break;
                    case '*': flag = this.product_check(cage_index); break;
                    case '/': flag = this.division_check(cage_index); break;
                    case '=': flag = true; break;    
                    default: throw new Exception("in cage_check function: "
                                                  + "wrong operator");
                        // we removed the one-cell-cage during init
                    }
            return flag;

        }
        return true;
                
              
    }    
     
    
    
    
    /**
     * Returns true if there is no empty cell left in the cage and 
     * all values are valid.
     * @param cage_index: Cage number
     */
    public boolean final_plus_check(int cage_index){
        int sum = 0;
        int i;
        Cage c = this.cages.get(cage_index);
        
        // compute the sum of all values in the cage
        for (Iterator<Integer> it = c.pos.iterator(); it.hasNext();) {
            i = it.next(); 
            if (this.grid[i] == 0){
                return false;
            }
            sum += this.grid[i];
        }        
        
        return (sum == c.value);  
    
    }
    
    /**
     * Returns true if there is no empty cell left in the cage and 
     * all values are valid.
     * @param cage_index: Cage number
     */
    public boolean final_minus_check(int cage_index){
        
        Cage c = this.cages.get(cage_index); 
        if (this.grid[c.pos.get(0)] != 0  &&  this.grid[c.pos.get(1)]!= 0){
            int result = Math.abs( this.grid[c.pos.get(0)] - this.grid[c.pos.get(1)]);
            return (result == c.value);
        }
        else return false;
    }
   
    /**
     * Returns true if there is no empty cell left in the cage and 
     * all values are valid.
     * @param cage_index: Cage number
     */
    public boolean final_division_check(int cage_index){
        
        Cage c = this.cages.get(cage_index);
        int max_num,min_num;
        max_num = Math.max(this.grid[c.pos.get(0)], this.grid[c.pos.get(1)]);
        min_num = Math.min(this.grid[c.pos.get(0)], this.grid[c.pos.get(1)]);
        
        //System.out.println("max: " + max_num + "min: "+ min_num);
        if (max_num!=0 && min_num!= 0){
            return ((double)max_num / (double) min_num == (double)c.value );
        }
        else return false;
    }
    
    
    /**
     * Returns true if there is no empty cell left in the cage and 
     * all values are valid.
     * @param cage_index: Cage number
     */
    public boolean final_product_check(int cage_index){
        
        Cage c = this.cages.get(cage_index);
        int i;
        int product = 1;
        for (Iterator<Integer> it = c.pos.iterator(); it.hasNext();) {
            i = this.grid[it.next()];
            product *= i; 
        } 
        return  (c.value == product) ;
    }
    
    
    
   /**
    * This method is created to run within the main method.
    * Gives info about the problem.
    * 
    * Asks the user to choose among the available puzzles and
    * prints the next possible states for the initial state of the puzzle.
    * 
    * @throws java.io.FileNotFoundException
    */ 
    
    
   public void go() throws FileNotFoundException, Exception{
       
       Scanner in = new Scanner(System.in);
       //PrintStream out = new PrintStream(new FileOutputStream("ken_output.txt"));
       //System.setOut(out);

       System.out.print("KenKen Puzzle (Challenging Problem)\n"
                + "Below puzzle samples are available in the input folder:\n"
               + "\nIF YOU CHOOSE 3x3, BFS,DFS,Best FS, A*, IDA* WILL RUN. "
               + "FOR THE OTHER SIZES CSP WILL RUN.\n"
               + "\n(This can be changed in KenkenState.java/go() method in Line 757.)\n"
                + "Press 1 for 3x3 easy\n"
               + "Press 2 for 4x4 easy\n"
               + "Press 3 for 4x4 hard\n"
               + "Press 4 for 5x5 easy\n"
               + "Press 5 for 5x5 hard\n"
               + "Press 6 for 6x6 easy\n"
               + "Press 7 for 6x6 hard\n"
               + "Press 8 for 7x7 easiest\n"
               + "Press 9 for 7x7 easy\n"
               + "Press 10 for 7x7 medium\n"
               + "Press 11 for 7x7 hard\n"
               + "Press 12 for 9x9 hard\n"
               
                + "\n\t");
       
       int s = in.nextInt();
       System.out.println("\nYou entered: "+s);
      
       switch(s){
           case 1: this.init("kenken3_easy.txt"); break;
           case 2: this.init("kenken4_easy.txt"); break;
           case 3: this.init("kenken4_hard.txt"); break;
           case 4: this.init("kenken5_easy.txt"); break;
           case 5: this.init("kenken5_hard.txt"); break;
           case 6: this.init("kenken6_easy.txt"); break;
           case 7: this.init("kenken6_hard.txt"); break;
           case 8: this.init("kenken7_easiest.txt"); break;
           case 9: this.init("kenken7_easy.txt"); break;
           case 10: this.init("kenken7_medium.txt"); break;
           case 11: this.init("kenken7_hard.txt"); break;
           case 12: this.init("kenken9_hard.txt"); break;
           default:    
                System.err.println("Invalid input.");
                return;
       }
   
       

             
       
       if (s==1){
       
            this.fill_single_cell_cages();
            Node root = new Node(this, null);  
           
            Search search1 = new BreadthFirstSearch();   
            Node result_node = search1.start_search(root);
            System.out.println("\n Breadth First Search:");
            search1.print_path(result_node);

            Search search2 = new DepthFirstSearch();   
            result_node = search2.start_search(root);
            System.out.println("\n Depth First Search:");
            search2.print_path(result_node);

            Search search3 = new BestFirstSearch();   
            result_node = search3.start_search(root);
            System.out.println("\n Best First Search:");
            search3.print_path(result_node);

            Search search4 = new AStar();   
            result_node = search4.start_search(root);
            System.out.println("\n A* Search:");
            search4.print_path(result_node);


            Search search5 = new IDAStar();   
            result_node = search5.start_search(root);
            System.out.println("\n IDA* Search:");
            search5.print_path(result_node);

           
       
       }
       else{  // CSP is running here
           
            CSPsolver csp = new CSPsolver(this);
            CSPnode  result = csp.search();
            csp.print_result(result);

       }
       
       
       
 
       
   }
   
   
   /**
    * Returns the number of cages which have empty cells
    */
   public int get_n_remaining_cages(){
        int count = 0;
        for (Cage c: this.cages){
            if (c.n_empty_cells == 0){
                count++;
            }
        }
      return  (this.cages.size() - count);
    }
       
   /**
    * Returns the number of rows which have empty cells
    */   
   public int get_n_remaining_rows(){
        
        int mult, count=0;
        
        for(int i=0; i<this.size; i++){  // row number
            mult = 1;
            for(int j=0; j<this.size; j++){   // col number
                mult *= this.grid[i*this.size + j];
            }
            if (mult != 0){
                count++;
            }
        }
       
        return (this.size - count);
    
    }

   /**
    * Returns the number of columns which have empty cells
    */  
   
   public int get_n_remaining_cols(){
        
        int mult, count=0;
        
        for(int i=0; i<this.size; i++){  // row number
            mult = 1;
            for(int j=0; j<this.size; j++){   // col number
                mult *= this.grid[i + this.size * j];
            }
            if (mult != 0){
                count++;
            }
        }
       
        return (this.size - count);
    
    }
   
   
   /**
    * Modify grid.
    * Assign value to the cell which is enumerated with grid_index.
    * @param grid_index
    * @param value 
    */
   
   public void set_grid(int grid_index, int value){
       this.grid[grid_index] = value;
       Cage c = this.cages.get( this.index_of_cage_with_cell(grid_index) );
       c.n_empty_cells--; 
   }
   
   
   // tells the value of a cell
   public int get_grid(int grid_index){
       return this.grid[grid_index] ;
       
   }
   
   
}  