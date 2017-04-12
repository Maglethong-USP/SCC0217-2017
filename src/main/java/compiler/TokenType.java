package compiler;

import java.util.regex.Pattern;



/**
 * Author: Maglethong Spirr
 */
public enum TokenType {
    // To test expressions use: https://regex101.com/
    ID ("^([a-zA-Z])([a-zA-Z0-9_]*)"),
    REAL ("^(\\+|-)?[0-9]+\\.[0-9]+"), 
    INTEGER ("^(\\+|-)?[1-9][0-9]*"), 
    SEMICOLOM ("^;"),
    BRAQUETS_OPEN ("^\\{"),
    BRAQUETS_CLOSE ("^\\}"),
    PARENTHESES_OPEN ("^\\("),
    PARENTHESES_CLOSE ("^\\)"),
    COLOM ("^:"),
    PERIOD ("^\\."),
    COMMA ("^,");
    // TODO -> Other rules
    // TODO -> Reserved words?

    private final Pattern pattern;

    private TokenType(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public Pattern getPattern() {
        return pattern;
    }
}
