package compiler.lexic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import compiler.base.IElement;
import compiler.error.Error;
import compiler.error.ErrorType;



/**
 * Author: Maglethong Spirr
 */
public final class LexicalAnalyzer {
    private static final Pattern spacePattern = Pattern.compile("\\A" + "((\\s)(\\{.*?\\})?)*");
    
    private int lineNum = -1;
    private int columnNum = 0;
    
    private final Scanner scanner;
    
    private List<IElement> buffer = new ArrayList<IElement>();
    
    public LexicalAnalyzer(Scanner scanner) {
	this.scanner = scanner;
    }

    public boolean hasNext() {
	while (buffer.size() == 0 && scanner.hasNextLine()) {
	    lineNum++;
	    columnNum = 0;
	    buffer = getLineElements(scanner.nextLine());
	}

	return buffer.size() > 0;
    }
    
    public IElement next() {
	if (!hasNext()) {
	    return null;
	}
	
	return buffer.remove(0);
    }
    
    private List<IElement> getLineElements(String line) {

        List<IElement> elements = new ArrayList<IElement>();
        
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
                    elements.add(new Token(lineNum, columnNum + m.start() + m.group().length() - m.group().length(), type, m.group()));
                    line = line.substring(m.end());
                    columnNum += m.end();
                    found = true;
                    break;
                }
            }
            
            // No token found -> error
            if (!found) {
        	elements.add(new Error(lineNum, columnNum++, ErrorType.LEXIC, "unexpected character '" + line.charAt(0) + "'"));
                line = line.substring(1);
            }
        }
        
        return elements;
    }
}