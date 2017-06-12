package compiler.lexic;


/**
 * Author: Maglethong Spirr
 * 
 * This class is just a token container while we only have a lexical analysis.
 * It will be removed in future versions.
 */
public final class Token {

    private final int line;
    private final int column;
    private final TokenType type;
    private final String content;

    public Token(int line, int column, TokenType type, String content) {
        this.line = line;
        this.column = column;
        this.type = type == TokenType.ID ? TokenType.getReservedWordOrID(content) : type;
        this.content = content;
    }



    public int getLine() { return line; }
    public int getColumn() { return column; }
    public TokenType getType() { return type; }
    public String getContent() { return content; }
    
    public String toString() { return "<" + type.toString() + ", " + content + ">"; }
}
