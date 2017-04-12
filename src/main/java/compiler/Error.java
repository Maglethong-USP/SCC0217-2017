package compiler;



/**
* Author: Maglethong Spirr
*/
public class Error {
    private final int line;
    private final int column;
    //private final ErrorType type; // TODO -> Will be used once we do syntax
    private final String content;

    
    public Error(int line, int column, /*TokenType type, */String content) {
        this.line = line;
        this.column = column;
        //this.type = type;
        this.content = content;
    }
    
    public @Override String toString() {
        return "Error: " + content + " at line " + line + " column " + column + "";
    }
}
