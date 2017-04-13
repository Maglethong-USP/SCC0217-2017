package compiler;

import java.util.Scanner;

import compiler.lexic.LexicalAnalyzer;
import compiler.lexic.LexicalAnalyzer.LineAnalysis;
import compiler.lexic.Token;
import compiler.error.Error;



/**
 * Author: Maglethong Spirr
 */
public class App {
    
    public static void main( String[] args ) {
        Scanner scanner = new Scanner(System.in);
        LexicalAnalyzer analyser = new LexicalAnalyzer();
        
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            LineAnalysis analysis = analyser.getElements(line);
            
            for (Token t : analysis.getTokens()) {
                System.out.println(t.toString());
            }
            for (Error e : analysis.getErrors()) {
                System.out.println(e.toString());
            }
            
            if (line.startsWith("\\q")) {
                break;
            }
        }
        
        scanner.close();
    }
}















