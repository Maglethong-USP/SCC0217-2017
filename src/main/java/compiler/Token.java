package compiler;
    


/**
 * Author: Maglethong Spirr
 */
public class Token {
    private final int line;
    private final int column;
    private final TokenType type;
    private final String content;
    
    
    
    // Create base for Token and Error
    //   Create factory to Generate Token/Error base from the text
    
    public Token(int line, int column, TokenType type, String content) {
        this.line = line;
        this.column = column;
        this.type = type;
        this.content = content;
    }

    public int getLine() { return line; }
    public int getColumn() { return column; }
    public TokenType getType() { return type; }
    public String getContent() { return content; }
    
    public @Override String toString() {
        return "<" + type.toString() + ", " + content + ">";
    }
}

