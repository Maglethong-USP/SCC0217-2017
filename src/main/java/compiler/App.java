package compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * Author: Maglethong Spirr
 */
public class App {
    
    public static void main( String[] args ) {
        
        // Processing 
        String line = "program lalg;      {teste}     var a: integer; begin readd(a, @, 1); end.";

        List<Token> tokens = new ArrayList<Token>();
        List<Error> errors = new ArrayList<Error>();
        
        Pattern spacePattern = Pattern.compile("^(\\s)*");
        
        int lineNum = 0;
        int columnNum = 0;
        while (line.length() > 0) {
            
            boolean found = false;
            
            // Remove whitespaces
            Matcher m = spacePattern.matcher(line);
            if (m.find()) {
                line = line.substring(m.group().length());
                columnNum += m.group().length();
            }
            
            // Try matching all possible tokens
            for (TokenType type : TokenType.values()) {
                m = type.getPattern().matcher(line);
                if (m.find()) {
                    tokens.add(new Token(lineNum, columnNum + m.start() + m.group().length() - m.group().length(), type, m.group())); // TODO -> check for reserved words
                    line = line.substring(m.end());
                    columnNum += m.end();
                    found = true;
                    break;
                }
            }
            
            // No token found -> error
            if (!found) {
                errors.add(new Error(lineNum, columnNum++, /*type,*/ "unexpected character '" + line.charAt(0) + "'"));
                line = line.substring(1);
            }
        }
        
        // Printing results
        for (Token token : tokens) {
            System.out.println(token.toString());
        }
        for (Error error : errors) {
            System.out.println(error.toString());
        }
    }
}















