package compiler.lexic;

import java.util.HashMap;



/**
 * Author: Maglethong Spirr
 */
public final class Token {
    
    private static final HashMap<String, TokenType> reservedWords = new HashMap<String, TokenType>();
    
    static {
        for (TokenType t : TokenType.reservedValues()) {
            reservedWords.put(t.name().replace("RESERVED_", "").toLowerCase(), t);
        }
    }
    
    private final int line;
    private final int column;
    private final TokenType type;
    private final String content;

    
    
    public Token(int line, int column, TokenType type, String content) {
        this.line = line;
        this.column = column;
        this.type = type == TokenType.ID ? reservedWords.getOrDefault(content, TokenType.ID) : type;
        this.content = content;
    }

    public int getLine() { return line; }
    public int getColumn() { return column; }
    public TokenType getType() { return type; }
    public String getContent() { return content; }
    
    public String toString() {
        return "<" + type.toString() + ", " + content + ">";
    }
}

