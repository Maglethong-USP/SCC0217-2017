package compiler.lexic;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import compiler.error.Error;
import compiler.error.ErrorType;



/**
 * Author: Maglethong Spirr
 */
public final class LexicalAnalyzer {
    
    public final class LineAnalysis {
        private final List<Token> tokens;
        private final List<Error> errors;
        
        public LineAnalysis(List<Token> tokens, List<Error> errors) {
            this.tokens = tokens;
            this.errors = errors;
        }

        public List<Token> getTokens() { return tokens; }
        public List<Error> getErrors() { return errors; }
    }
    
    
    
    private static final Pattern spacePattern = Pattern.compile("\\A" + "((\\s)(\\{.*?\\})?)*");
    
    private int lineNum = 0;
    private int columnNum = 0;
    
    
    
    public LineAnalysis getElements(String line) {

        List<Token> tokens = new ArrayList<Token>();
        List<Error> errors = new ArrayList<Error>();
        
        while (line.length() > 0) {
            
            boolean found = false;
            
            // Remove whitespaces
            Matcher m = spacePattern.matcher(line);
            if (m.find()) {
                line = line.substring(m.group().length());
                columnNum += m.group().length();
                if (line.isEmpty())
                    break;
            }
            
            // Try matching all possible tokens
            for (TokenType type : TokenType.patternValues()) {
                m = type.getPattern().matcher(line);
                if (m.find()) {
                    tokens.add(new Token(lineNum, columnNum + m.start() + m.group().length() - m.group().length(), type, m.group()));
                    line = line.substring(m.end());
                    columnNum += m.end();
                    found = true;
                    break;
                }
            }
            
            // No token found -> error
            if (!found) {
                errors.add(new Error(lineNum, columnNum++, ErrorType.LEXIC, "unexpected character '" + line.charAt(0) + "'"));
                line = line.substring(1);
            }
        }
        
        return new LineAnalysis(tokens, errors);
    }
}