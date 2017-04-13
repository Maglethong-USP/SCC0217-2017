package compiler.lexic;

import java.util.Arrays;
import java.util.regex.Pattern;



/**
 * Author: Maglethong Spirr
 */
public enum TokenType {
    // To test expressions use: https://regex101.com/
    ID ("\\A([a-zA-Z])([a-zA-Z0-9_]*)"),
    REAL ("\\A(\\+|-)?[0-9]+\\.[0-9]+"), 
    INTEGER ("\\A(\\+|-)?[1-9][0-9]*"), 
    SEMICOLOM ("\\A;"),
    BRAQUETS_OPEN ("\\A\\{"),
    BRAQUETS_CLOSE ("\\A\\}"),
    PARENTHESES_OPEN ("\\A\\("),
    PARENTHESES_CLOSE ("\\A\\)"),
    COLOM ("\\A:"),
    PERIOD ("\\A\\."),
    COMMA ("\\A,"),
    // TODO -> Other rules
    RESERVED_END (null),
    RESERVED_READ (null),
    RESERVED_PROGRAM (null),
    RESERVED_VAR (null),
    RESERVED_INTEGER (null),
    RESERVED_BEGIN (null);
    // TODO -> Other Reserved


    
    static {
        patternValues = Arrays.stream(TokenType.values()).filter(t -> t.pattern != null).toArray(TokenType[]::new);
        reservedValues = Arrays.stream(TokenType.values()).filter(t -> t.pattern == null).toArray(TokenType[]::new);
    }

    private static final TokenType[] patternValues;
    private static final TokenType[] reservedValues;
    
    public static TokenType[] patternValues() { return patternValues; }
    public static TokenType[] reservedValues() { return reservedValues; }
    
    
    private final Pattern pattern;

    private TokenType(String regex) {
        this.pattern = regex == null ? null : Pattern.compile(regex);
    }

    public Pattern getPattern() {
        return pattern;
    }
}
