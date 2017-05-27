package compiler.lexic;

import compiler.error.Error;
import compiler.error.ErrorType;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Author: Maglethong Spirr
 */
public final class LexicalAnalyzer {
    private static final Pattern spacePattern = Pattern.compile("\\A" + "((\\s)(\\{.*?\\})?)*");

    private int lineNum = -1;
    private int columnNum = 0;

    private final Scanner scanner;

    private List<Token> tokenBuffer = new ArrayList<>();
    private List<Error> errorBuffer = new ArrayList<>();

    /**
     * Constructor
     *
     * @param scanner An input scanner to read instructions from
     */
    public LexicalAnalyzer(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Has next token to read
     *
     * @return true if there are still tokens to consume
     */
    public boolean hasNext() {
        while (tokenBuffer.size() == 0 && scanner.hasNextLine()) {
            lineNum++;
            columnNum = 0;
            analyzeLine(scanner.nextLine());
        }

        return tokenBuffer.size() > 0;
    }

    /**
     * Retrieve next Token
     *
     * @return The next token from input
     */
    public Token next() {
        if (!hasNext()) {
            throw new NullPointerException("Trying to read next token from end of file");
        }

        return tokenBuffer.remove(0);
    }

    /**
     * Does the code being analyzed contain a lexical error until the line being analyzed?
     *
     * @return true if the line contain an error
     */
    public boolean hasError() {
        while (errorBuffer.size() == 0 && tokenBuffer.size() == 0 && scanner.hasNextLine()) {
            lineNum++;
            columnNum = 0;
            analyzeLine(scanner.nextLine());
        }

        return errorBuffer.size() > 0;
    }

    /**
     * Retrieve next lexical error
     *
     * @return The next error
     */
    public Error nextError() {
        if (!hasError()) {
            throw new NullPointerException("Trying to read next token from end of file");
        }

        return errorBuffer.remove(0);
    }

    private void analyzeLine(String line) {

        while (line.length() > 0) {

            boolean found = false;

            // Remove whitespaces
            Matcher m = spacePattern.matcher(line);
            if (m.find()) {
                line = line.substring(m.group().length());
                columnNum += m.group().length();
                if (line.isEmpty())
                    break;
            }

            // Try matching all possible tokens
            for (TokenType type : TokenType.patternValues()) {
                m = type.getPattern().matcher(line);
                if (m.find()) {
                    tokenBuffer.add(new Token(lineNum,
                            columnNum + m.start() + m.group().length() - m.group().length(),
                            type, m.group()));
                    line = line.substring(m.end());
                    columnNum += m.end();
                    found = true;
                    break;
                }
            }

            // No token found -> error
            if (!found) {
                errorBuffer.add(new Error(lineNum, columnNum++, ErrorType.LEXIC,
                        "unexpected character '" + line.charAt(0) + "'"));
                line = line.substring(1);
            }
        }
    }
}
