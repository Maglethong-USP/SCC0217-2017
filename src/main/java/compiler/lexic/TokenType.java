package compiler.lexic;

import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;



/**
 * Author: Maglethong Spirr
 */
public enum TokenType {
    // To test expressions use: https://regex101.com/
    ID ("([a-zA-Z])([a-zA-Z0-9_]*)"),
    REAL ("(\\+|-)?[0-9]+\\.[0-9]+"), 
    INTEGER ("(\\+|-)?[1-9][0-9]*"), 
    // Symbols
    SEMICOLOM (";"),
    BRAQUETS_OPEN ("\\{"),
    BRAQUETS_CLOSE ("\\}"),
    PARENTHESES_OPEN ("\\("),
    PARENTHESES_CLOSE ("\\)"),
    ATRIBUTION (":="),
    COLOM (":"),
    PERIOD ("\\."),
    COMMA (","),
    EQUAL ("="),
    DIFFERENT ("<>"),
    GRATER_EQUAL (">="),
    LESS_EQUAL ("<="),
    GRATER (">"),
    LESS ("<"),
    ADD ("\\+"),
    SUBTRACT ("-"),
    MULTIPLY ("\\*"),
    DIVIDE ("/"),
    // Reserved Words
    RESERVED_END (null),
    RESERVED_READ (null),
    RESERVED_PROGRAM (null),
    RESERVED_VAR (null),
    RESERVED_INTEGER (null),
    RESERVED_BEGIN (null),
    RESERVED_CONST (null),
    RESERVED_REAL (null),
    RESERVED_PROCEDURE (null),
    RESERVED_IF (null),
    RESERVED_THEN (null),
    RESERVED_ELSE (null),
    RESERVED_WRITE (null),
    RESERVED_WHILE (null),
    RESERVED_DO (null);
    
    
    // Calculate pattern once per value
    private final Pattern pattern;

    private TokenType(String regex) {
        this.pattern = regex == null ? null : Pattern.compile("\\A" + regex);
    }

    public Pattern getPattern() {
        return pattern;
    }


    // Getters for Reserved/non-Reserved values
    private static final TokenType[] patternValues;
    private static final TokenType[] reservedValues;
    
    static {
        patternValues = Arrays.stream(TokenType.values()).filter(t -> t.pattern != null).toArray(TokenType[]::new);
        reservedValues = Arrays.stream(TokenType.values()).filter(t -> t.pattern == null).toArray(TokenType[]::new);
    }
    
    public static TokenType[] patternValues() { return patternValues; }
    public static TokenType[] reservedValues() { return reservedValues; }

    // Map for reserved words
    private static final HashMap<String, TokenType> reservedWords = new HashMap<String, TokenType>();
    
    static {
        for (TokenType t : TokenType.reservedValues()) {
            reservedWords.put(t.name().replace("RESERVED_", "").toLowerCase(), t);
        }
    }
    
    public static TokenType getReservedWordOrID(String word) {
	return reservedWords.getOrDefault(word, ID);
    }
}
