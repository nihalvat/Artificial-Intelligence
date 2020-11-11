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
import java.util.Arrays;


/**
 * This class is created for the states of the kenken puzzle.
 * If the puzzle grid is considered as the set of cells 
 * which is partitioned, each partition is called a cage.
 *  
 * Each cage has its own operator (+,-,*,/) and 
 * a resultant integer value, along with the group of cells.
 * 
 * 
 * It is possible to have a cage with one cell. 
 * This cell is directly initialized to its value in the puzzle.
 * In this case, the operator is defined as (=) for convention.
 * 
 * 
 */


public class Cage {
    public char op;             // operator of the cage
    public int value;           // result of the operation
    public int n_empty_cells;   // number of empty cells in the cage
    public ArrayList<Integer> pos;     
                         // keeps the indices of the cells that form the cage 
    
    
    
    public Cage(){}
    
    public Cage ( Cage c ){
    
        this.op = c.op;
        this.value = c.value;
        this.n_empty_cells = c.n_empty_cells;
        this.pos = new ArrayList<>(c.pos);
    }
    
    /**
     * Constructor with initial values.
     * @param o: operator
     * @param v: result of the operation
     * @param p: list of indices of the position of the cells 
     */
    public Cage(char o, int v, ArrayList<Integer> p){ 
        
        this.op = o;
        this.value = v;
        this.n_empty_cells = p.size();
        this.pos = new ArrayList<> (p);
    }
    
    
    
    
//    /**
//     * Returns true if the cage is equal to the cage c
//     */
//    public boolean equals (Cage c){
//                
//        // two cages are equal
//        
//        if ( (this.op == c.op) && (this.value == c.value) &&
//             (this.pos.size() == c.pos.size()) )
//            return this.pos.equals(c.pos);        
//        return false; 
//    }
    
    

    /**
     * Prints the information about the cage 
     */
    public void print (){
    
        System.out.print("val: "+ this.value +"\n");
        System.out.print("op: "+ this.op +"\n");
        System.out.print("empty: "+ this.n_empty_cells +"\n");
        System.out.print("pos: ");
        for(Integer k: this.pos){
            System.out.print(k + ", ");      
        }
        System.out.println("\n");

    }

    // size is the size of the puzzle 
    public ArrayList possible_values(int size) throws Exception{
        switch (this.op){
            case '+': return this.plus_values(size); 
            case '-': return this.minus_values(size);
            case '*': return this.product_values(size);
            case '/': return this.division_values(size);
            case '=': return new ArrayList<> (Arrays.asList(this.value));   
            default: throw new Exception("error in Cage/possible_values function\n");
        }
    }
   

    public ArrayList plus_values(int size){
        
//        System.out.println("\n\n in Cage/plus values");
        
        ArrayList<Integer> temp = new ArrayList<> ();
        
        // if this cage has exactly 2 cells
        if (this.pos.size() == 2){
             for (int i=1; i<=size; i++){
                 if ((i<this.value)&&(this.value-i <= size)){
                     temp.add(i);
                 }
             }
        }
        else {
            for (int i=1; i<=size; i++){
                if (i<this.value){
                    temp.add(i);
                }   
            }
        }
        
        
//        System.out.println("size of returned  list " + temp.size());
//        for(int k=0; k<temp.size(); k++){
//            System.out.print(" "+ temp.get(k));
//        }
        return temp;
    }
    
    public ArrayList minus_values(int size){
        
//        System.out.println("\n\n in Cage/minus values");
        
        ArrayList<Integer> temp = new ArrayList<> ();
        for (int i=1; i<=size; i++){
            if ( ((1<=i)&&(i<=size-this.value)) || 
                 ((1+this.value<=i)&&(i<=size)) ){
                temp.add(i);
            }   
        }
//        System.out.println("size of returned  list " + temp.size());
//        for(int k=0; k<temp.size(); k++){
//            System.out.print(" "+ temp.get(k));
//        }
        return temp;
    }
    
    
    public ArrayList product_values(int size){
        
//        System.out.println("\n\n in Cage/product values");

        
        ArrayList<Integer> temp = new ArrayList<> ();
        
        if (this.pos.size() == 2){
            for (int i=1; i<=size; i++){
                if (this.value%i == 0 && (this.value/i <= size) ){
                    temp.add(i);
                }   
            }        
        }
        else{
            for (int i=1; i<=size; i++){
                if (this.value%i == 0 ){
                    temp.add(i);
                }   
            }
        
        }
        
        

        
//        System.out.println("size of returned  list " + temp.size());
//        for(int k=0; k<temp.size(); k++){
//            System.out.print(" "+ temp.get(k));
//        }
        return temp;
    }
    
    
    public ArrayList division_values(int size){
        
//        System.out.println("\n\n in Cage/division values");
        
        ArrayList<Integer> temp = new ArrayList<> ();
        for (int i = 1; i<= size; i++){
            if ((i*this.value <= size) || (i%this.value==0)){
                temp.add(i);
            }  
        }
//        System.out.println("size of returned  list " + temp.size());
//        for(int k=0; k<temp.size(); k++){
//            System.out.print(" "+ temp.get(k));
//        }
        return temp;
    }
    
    
    
    
    
    
}
