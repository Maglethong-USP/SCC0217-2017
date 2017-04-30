package compiler.error;

import compiler.base.IElement;



/**
 * Author: Maglethong Spirr
 */
public final class Error implements IElement {
    private final int line;
    private final int column;
    private final ErrorType type;
    private final String content;

    
    public Error(int line, int column, ErrorType type, String content) {
        this.line = line;
        this.column = column;
        this.type = type;
        this.content = content;
    }

    public int getLine() { return line; }
    public int getColumn() { return column; }
    public ErrorType getType() { return type; }
    public String getContent() { return content; }
    
    public String toString() {
        return "Error: " + content + " at line " + line + " column " + column + "";
    }
}
