package compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;



/**
 * Author: Maglethong Spirr
 */
public class App {
    
    public static void main( String[] args ) {
        
        // Processing 
        String line = "program lalg;      {teste}     var a: integer; begin readd(a, @, 1); end.";

        List<Token> tokens = new ArrayList<Token>();
        List<Error> errors = new ArrayList<Error>();
        
        int lineNum = 0;
        int columnNum = 0;
        while (line.length() > 0) {
            boolean found = false;
            
            for (TokenType type : TokenType.values()) {
                Matcher m = type.getPattern().matcher(line);
                if (m.find()) {
                    String content = m.group().replaceAll("\\s", "");
                    tokens.add(new Token(lineNum, columnNum + m.start() + m.group().length() - content.length(), type, content)); // TODO -> check for reserved words
                    line = line.substring(m.end(), line.length());
                    columnNum += m.end();
                    found = true;
                    break;
                }
            }
            
            if (!found) {
                errors.add(new Error(lineNum, columnNum++, /*type,*/ "unexpected character '" + line.charAt(0) + "'"));
                line = line.substring(1, line.length());
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















