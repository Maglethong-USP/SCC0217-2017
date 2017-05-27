package compiler;

import compiler.lexic.LexicalAnalyzer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;


/**
 * Author: Maglethong Spirr
 */
@RunWith(Parameterized.class)
public class LexicalTest {

    @Parameter(0)
    public String source;
    @Parameter(1)
    public String[] expectedElements;


    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                // Test sequence 1: Recognizing Reserved words
                {"program", new String[]{"<RESERVED_PROGRAM, program>"}},
                {"begin", new String[]{"<RESERVED_BEGIN, begin>"}},
                {"end", new String[]{"<RESERVED_END, end>"}},
                {"const", new String[]{"<RESERVED_CONST, const>"}},
                {"var", new String[]{"<RESERVED_VAR, var>"}}, {"real", new String[]{"<RESERVED_REAL, real>"}},
                {"integer", new String[]{"<RESERVED_INTEGER, integer>"}},
                {"procedure", new String[]{"<RESERVED_PROCEDURE, procedure>"}},
                {"if", new String[]{"<RESERVED_IF, if>"}}, {"then", new String[]{"<RESERVED_THEN, then>"}},
                {"else", new String[]{"<RESERVED_ELSE, else>"}},
                {"read", new String[]{"<RESERVED_READ, read>"}},
                {"write", new String[]{"<RESERVED_WRITE, write>"}},
                {"while", new String[]{"<RESERVED_WHILE, while>"}}, {"do", new String[]{"<RESERVED_DO, do>"}},
                {"for", new String[]{"<RESERVED_FOR, for>"}}, {"to", new String[]{"<RESERVED_TO, to>"}},
                // Test sequence 2: Recognizing Identifiers and Numbers
                {"a", new String[]{"<ID, a>"}}, {"asd123", new String[]{"<ID, asd123>"}},
                {"a_as_123a", new String[]{"<ID, a_as_123a>"}},
                {"_a", new String[]{"<ID, a>", "Error: unexpected character '_' at line 0 column 0"}},
                {"123", new String[]{"<INTEGER, 123>"}}, {"+234", new String[]{"<INTEGER, +234>"}},
                {"-345", new String[]{"<INTEGER, -345>"}},
                {"456a", new String[]{"<INTEGER, 456>", "<ID, a>"}}, {"123.3", new String[]{"<REAL, 123.3>"}},
                {"+123.3", new String[]{"<REAL, +123.3>"}}, {"-123.3", new String[]{"<REAL, -123.3>"}},
                {"123.", new String[]{"<INTEGER, 123>", "<PERIOD, .>"}},
                {".3", new String[]{"<PERIOD, .>", "<INTEGER, 3>"}},
                // Test sequence 3: Other symbols
                {";", new String[]{"<SEMICOLOM, ;>"}}, {":", new String[]{"<COLOM, :>"}},
                {",", new String[]{"<COMMA, ,>"}}, {":=", new String[]{"<ATTRIBUTION, :=>"}},
                {"=", new String[]{"<EQUAL, =>"}}, {"<>", new String[]{"<DIFFERENT, <>>"}},
                {">=", new String[]{"<GRATER_EQUAL, >=>"}}, {"<=", new String[]{"<LESS_EQUAL, <=>"}},
                {">", new String[]{"<GRATER, >>"}}, {"<", new String[]{"<LESS, <>"}},
                {"+", new String[]{"<ADD, +>"}}, {"-", new String[]{"<SUBTRACT, ->"}},
                {"(", new String[]{"<PARENTHESES_OPEN, (>"}}, {")", new String[]{"<PARENTHESES_CLOSE, )>"}},
                // Test sequence 4: Whitespace swallow
                {"\t;  ", new String[]{"<SEMICOLOM, ;>"}}, {"\n  \t ;  ", new String[]{"<SEMICOLOM, ;>"}},
                {"  ; \n\t ; ", new String[]{"<SEMICOLOM, ;>", "<SEMICOLOM, ;>"}},
                // Test 5: Comments
                {"  {}     ", new String[]{}}, {"  {}   {} \t  {}   {}   ", new String[]{}},
                // Test 6: For loop
                {
                        // Program Line
                        "for i := 1 to 10 do write(i);",
                        // Expected Tokens
                        new String[]{
                                "<RESERVED_FOR, for>",
                                "<ID, i>",
                                "<ATTRIBUTION, :=>",
                                "<INTEGER, 1>",
                                "<RESERVED_TO, to>",
                                "<INTEGER, 10>",
                                "<RESERVED_DO, do>",
                                "<RESERVED_WRITE, write>",
                                "<PARENTHESES_OPEN, (>",
                                "<ID, i>",
                                "<PARENTHESES_CLOSE, )>",
                                "<SEMICOLOM, ;>"
                        }
                },
                // Test 7: Simple program with error
                {
                        // Program Line
                        "program lalg;      {teste}     var a: integer; begin read(a, @, 1); end.",
                        // Expected Tokens
                        new String[]{
                                "<RESERVED_PROGRAM, program>",
                                "<ID, lalg>",
                                "<SEMICOLOM, ;>",
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
                                "Error: unexpected character '@' at line 0 column 61"
                        }
                },
                // Test 8: Line and Column in errors
                {
                        // Program Line
                        "   {}  #   \n  {}     @  \n  {}  { }   % ",
                        // Expected Tokens
                        new String[]{
                                "Error: unexpected character '#' at line 0 column 7",
                                "Error: unexpected character '@' at line 1 column 9",
                                "Error: unexpected character '%' at line 2 column 12"
                        }
                }
        };
        return Arrays.asList(data);
    }


    @Test(timeout = 1500)
    public void parsingProgramShouldReturnItsTokensAndErrors() {

        LexicalAnalyzer analyser = new LexicalAnalyzer(new Scanner(source));

        List<String> got = new ArrayList<String>();
        while (analyser.hasNext()) {
            got.add(analyser.next().toString());
        }
        while (analyser.hasError()) {
            got.add(analyser.nextError().toString());
        }

        for (int i = 0; i < Math.max(got.size(), expectedElements.length); i++) {
            assertEquals("Token/Error " + i + ": ", getOrElse(expectedElements, i, ""), getOrElse(got, i, ""));
        }
    }

    private String getOrElse(String[] arr, int index, String alternative) {
        return arr.length > index ? arr[index] : alternative;
    }

    private String getOrElse(List<String> list, int index, String alternative) {
        return list.size() > index ? list.get(index) : alternative;
    }
}
