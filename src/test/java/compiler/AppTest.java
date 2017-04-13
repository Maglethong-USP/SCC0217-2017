package compiler;

import compiler.lexic.LexisAnalizer;
import compiler.lexic.LexisAnalizer.LineAnalysis;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;



/**
 * Author: Maglethong Spirr
 */
@RunWith(Parameterized.class)
public class AppTest {

    @Parameter(0)
    public String line;
    @Parameter(1)
    public String[] expectedElements;
    

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
            // Test sequence 1: Recognizing Reserved words
            { "program", new String[] { "<RESERVED_PROGRAM, program>" } },
            { "begin", new String[] { "<RESERVED_BEGIN, begin>" } },
            { "end", new String[] { "<RESERVED_END, end>" } },
            { "const", new String[] { "<RESERVED_CONST, const>" } },
            { "var", new String[] { "<RESERVED_VAR, var>" } },
            { "real", new String[] { "<RESERVED_REAL, real>" } },
            { "integer", new String[] { "<RESERVED_INTEGER, integer>" } },
            { "procedure", new String[] { "<RESERVED_PROCEDURE, procedure>" } },
            { "if", new String[] { "<RESERVED_IF, if>" } },
            { "then", new String[] { "<RESERVED_THEN, then>" } },
            { "else", new String[] { "<RESERVED_ELSE, else>" } },
            { "read", new String[] { "<RESERVED_READ, read>" } },
            { "write", new String[] { "<RESERVED_WRITE, write>" } },
            { "while", new String[] { "<RESERVED_WHILE, while>" } },
            { "do", new String[] { "<RESERVED_DO, do>" } },
            // Test sequence 2: Recognizing Identifiers and Numbers
            { "a", new String[] { "<ID, a>" } },
            { "asd123", new String[] { "<ID, asd123>" } },
            { "a_as_123a", new String[] { "<ID, a_as_123a>" } },
            { "_a", new String[] { "<ID, a>", "Error: unexpected character '_' at line 0 column 0" } },
            { "123", new String[] { "<INTEGER, 123>" } },
            { "+123", new String[] { "<INTEGER, +123>" } },
            { "-123", new String[] { "<INTEGER, -123>" } },
            { "123a", new String[] { "<INTEGER, -123>", "<ID, a>" } },
            { "123.3", new String[] { "<REAL, 123.3>" } },
            { "+123.3", new String[] { "<REAL, +123.3>" } },
            { "-123.3", new String[] { "<REAL, -123.3>" } },
            { "123.", new String[] { "<INTEGER, 123>", "<PERIOD, .>" } },
            { ".3", new String[] { "<PERIOD, .>", "<INTEGER, 3>" } },
            // Test sequence 3: Other symbols
            { ";", new String[] { "<SEMICOLOM, ;>" } },
            { ":", new String[] { "<COLOM, :>" } },
            { ",", new String[] { "<COMMA, ,>" } },
            { ":=", new String[] { "<ATRIBUTION, :=>" } },
            { "=", new String[] { "<EQUAL, =>" } },
            { "<>", new String[] { "<DIFFERENT, <>>" } },
            { ">=", new String[] { "<GRATER_EQUAL, >=>" } },
            { "<=", new String[] { "<LESS_EQUAL, <=>" } },
            { ">", new String[] { "<GRATER, >>" } },
            { "<", new String[] { "<LESS, <>" } },
            { "+", new String[] { "<ADD, +>" } },
            { "-", new String[] { "<SUBTRACT, ->" } },
            { "*", new String[] { "<MULTIPLY, *>" } },
            { "/", new String[] { "<DEVIDE, />" } },
            // Test sequence 4: Whitespace swallow
            { "\t;  ", new String[] { "<SEMICOLOM, ;>" } },
            { "\n  \t ;  ", new String[] { "<SEMICOLOM, ;>" } },
            { "  ; \n\t ; ", new String[] { "<SEMICOLOM, ;>", "<SEMICOLOM, ;>" } },
            // Test 5: Simple program with error
            { 
                // Program Line
                "program lalg;      {teste}     var a: integer; begin read(a, @, 1); end.",
                // Expected Tokens
                new String[] {
                       "<RESERVED_PROGRAM, program>",
                       "<ID, lalg>",
                       "<SEMICOLOM, ;>",
                       "<BRAQUETS_OPEN, {>",
                       "<ID, teste>",
                       "<BRAQUETS_CLOSE, }>",
                       "<RESERVED_VAR, var>",
                       "<ID, a>",
                       "<COLOM, :>",
                       "<RESERVED_INTEGER, integer>",
                       "<SEMICOLOM, ;>",
                       "<RESERVED_BEGIN, begin>",
                       "<RESERVED_READ, read>",
                       "<PARENTHESES_OPEN, (>",
                       "<ID, a>",
                       "<COMMA, ,>",
                       "<COMMA, ,>",
                       "<INTEGER, 1>",
                       "<PARENTHESES_CLOSE, )>",
                       "<SEMICOLOM, ;>",
                       "<RESERVED_END, end>",
                       "<PERIOD, .>", 
                       // Expected Errors
                       "Error: unexpected character '@' at line 0 column 61"
                }, 
            }
          };
        return Arrays.asList(data);
    }
    
    @Test
    public void parsingProgramShouldReturnItsTokensAndErrors() {
        
        LexisAnalizer analyser = new LexisAnalizer();
        
        LineAnalysis analysis = analyser.getElements(line);

        int j = 0;
        for (int i = 0; i < analysis.getTokens().size() && i < expectedElements.length; i++, j++) {
            assertEquals("Token " + i + ": ", expectedElements[i], analysis.getTokens().get(i).toString());
        }
        for (int i = 0; i < analysis.getTokens().size() && i +j < expectedElements.length; i++, j++) {
            assertEquals("Error " + i + ": ", expectedElements[i], analysis.getTokens().get(i).toString());
        }
        assertEquals("Number of Elements Found: ", expectedElements.length, analysis.getTokens().size() + analysis.getErrors().size());
    }
}