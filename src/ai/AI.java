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

import java.io.FileNotFoundException;
import java.util.Scanner;


public class AI {

    /**
     * @param args the command line arguments
     * @throws java.io.FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException, Exception {
        

        Scanner in = new Scanner(System.in);
        String input;
             
        do {       
            AI.opening_message();
          
            input = (in.nextLine());
            System.out.println("You entered: " + input);
            if (input.length() > 0) {
                
                switch (input) {
                    
                    case ("waterjug"):
                        WJState wj = new WJState();
                        wj.go(); break;
                    
                    case ("mission"):                        
                        MCState m = new MCState();
                        m.go(); break;
                    
                    case ("eight"):                        
                        EPState e = new EPState();
                        e.go(); break;
                    
                    case ("kenken"):                        
                        KenkenState k = new KenkenState();
                        k.go(); break;                    
                    
                    case("quit"): break;
                        
                    default:
                        System.err.println("Input is not valid.");                    
                }            
            } 
        } while (! input.equalsIgnoreCase("quit") ); 
        
      
    }
    
    
    
    public static void opening_message(){
        System.out.println("\n\nPlease enter one of the keywords below to continue with:\n"
                + "waterjug -> water jug problem\n"
                + "mission  -> missionaries and cannibals problem\n"
                + "eight    -> eight puzzle problem\n"
                + "kenken   -> kenken puzzle (challenging problem)\n"
                + "quit     -> to quit\n"); 
    }
    
    
    
}
