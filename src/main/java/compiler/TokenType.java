package compiler;

import java.util.regex.Pattern;



/**
 * Author: Maglethong Spirr
 */
public enum TokenType {
    // To test expressions use: https://regex101.com/
    ID ("^(\\s*)([a-zA-Z])([a-zA-Z0-9_]*)"),
    SEMICOLOM ("^\\s;");
    // TODO -> Other rules

    private final Pattern pattern;

    private TokenType(String regex) {
        this.pattern = Pattern.compile(regex);
    }

    public Pattern getPattern() {
        return pattern;
    }
}
