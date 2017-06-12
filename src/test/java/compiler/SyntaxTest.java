package compiler;

import compiler.lexic.LexicalAnalyzer;
import compiler.syntatic.SyntaxAnalyzer;
import compiler.error.Error;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * SyntaxTest class
 *
 * @author Maglethong Spirr
 */
@RunWith(Parameterized.class)
public class SyntaxTest {

    @Parameter(0)
    public String source;
    @Parameter(1)
    public String[] expectedErrors;

    @Parameters
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][]{
                // Test #0
                {"program p; begin end", new String[]{}},
                {"program p; begin ", new String[]{"Error: File ended unexpectedly, was expecting RESERVED_END at line 0 column 17"}},
                {"program ; begin end", new String[]{"Error: Unexpected token SEMICOLON was expecting ID at line 0 column 8"}},
                {"p; begin end", new String[]{"Error: Unexpected token ID was expecting RESERVED_PROGRAM at line 0 column 0"}},
                {"p; end", new String[]{
                        "Error: Unexpected token ID was expecting RESERVED_PROGRAM at line 0 column 0",
                        "Error: Unexpected token RESERVED_END was expecting RESERVED_BEGIN at line 0 column 3"
                }},
                // Test #5
                {"p; begin end", new String[]{"Error: Unexpected token ID was expecting RESERVED_PROGRAM at line 0 column 0"}},
                {"program p; const a = 0.3; begin end", new String[]{}},
                {"program p; const a = 0.3; const b = 1; begin end", new String[]{}},
                {"program p; const a = 1.; const b = 1; begin end", new String[]{"Error: Unexpected token PERIOD was expecting SEMICOLON at line 0 column 22"}},
                {"program p; const a = 0.3; var b, c, d : real; begin end", new String[]{}},
                // Test #10
                {"program p; procedure proc1; begin end; begin end", new String[]{}},
                {"program p; procedure proc1(a : integer, b : real); begin end; begin end", new String[]{}},
                {"program p; begin read(a); write(b); while(a <> b) do begin a := b; end; end", new String[]{}},
                {"", new String[]{"Error: File ended unexpectedly, was expecting RESERVED_PROGRAM at line -1 column 0"}}

        };
        return Arrays.asList(data);
    }

    @BeforeClass
    public static void initialize() {
        System.setProperty("debug", "true");
    }

    @Test(timeout = 1500)
    public void parsingProgramShouldReturnItsTokensAndErrors() {

        LexicalAnalyzer analyser = new LexicalAnalyzer(new Scanner(source));
        SyntaxAnalyzer syntax = new SyntaxAnalyzer(analyser);

        syntax.Analyze();

        List<String> got = syntax.getErrors().stream()
                .map(Error::toString)
                .collect(Collectors.toList());

        for (int i = 0; i < Math.max(got.size(), expectedErrors.length); i++) {
            assertEquals("Token/Error " + i + ": ", getOrElse(expectedErrors, i, ""),
                    getOrElse(got, i, ""));
        }
    }

    private String getOrElse(String[] arr, int index, String alternative) {
        return arr.length > index ? arr[index] : alternative;
    }

    private String getOrElse(List<String> list, int index, String alternative) {
        return list.size() > index ? list.get(index) : alternative;
    }
}
