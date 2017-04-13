package compiler;

import java.util.Scanner;



/**
 * Author: Maglethong Spirr
 */
public class App {
    
    public static void main( String[] args ) {
        Scanner scanner = new Scanner(System.in);
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            
            System.out.println(line);
            
            if (line.startsWith("\\q")) {
                break;
            }
        }
        
        scanner.close();
    }
}















