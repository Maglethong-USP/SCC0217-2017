package compiler.syntatic;


import compiler.error.ErrorType;
import compiler.lexic.LexicalAnalyzer;
import compiler.lexic.TokenType;
import compiler.error.Error;

import java.util.Collection;

/**
 * This class holds all LALG rules. For more information check LALG_grammar.pdf at projects root
 */
final class RuleAnalyzer {


    static void initial(LexicalAnalyzer lexic, Collection<Error> errors) {
        programa(lexic, errors);

        if (lexic.hasNext()) {
            errors.add(new Error(lexic.getLine(), lexic.getColumn(), ErrorType.SYNTAX,
                    "File did not end when expected, found token " + lexic.peak().getType()));
        }
    }

    /**
     * Builds a syntax error.
     *
     * <p/> Compares the next token in buffer with an expected token type and builds an error reporting the line,
     * column, expected type and obtained type
     *
     * @param lexic Lexical Analyzer providing tokens
     * @param expected Expected token
     * @return Error object with line, column and nature of the error
     */
    private static Error buildError(LexicalAnalyzer lexic, TokenType expected) {
        if (lexic.hasNext()) {
            return new Error(lexic.peak().getLine(), lexic.peak().getColumn(), ErrorType.SYNTAX,
                    "Unexpected token " + lexic.peak().getType()
                            + " was expecting " + expected);
        }
        return new Error(lexic.getLine(), lexic.getColumn(), ErrorType.SYNTAX,
                "File ended unexpectedly, was expecting " + expected);
    }

    /**
     * Checks if next symbol is of desired type and consumes it
     *
     * @param type Desired type
     * @param lexic Lexical Analyzer providing tokens
     * @return True if the symbol was found and consumed
     */
    private static boolean isNextSimbol(TokenType type, LexicalAnalyzer lexic) {
        if (lexic.hasNext() && lexic.peak().getType() == type) {
            lexic.next();
            return true;
        }
        return false;
    }

    private static void programa(LexicalAnalyzer lexic, Collection<Error> errors) {

        if (isNextSimbol(TokenType.RESERVED_PROGRAM, lexic)) {
            if (isNextSimbol(TokenType.ID, lexic)) {
                if (isNextSimbol(TokenType.SEMICOLOM, lexic)) {
                    corpo(lexic, errors);
                } else {
                    errors.add(buildError(lexic, TokenType.SEMICOLOM));
                }
            } else {
                errors.add(buildError(lexic, TokenType.ID));
            }
        } else {
            errors.add(buildError(lexic, TokenType.RESERVED_PROGRAM));
        }
    }

    private static void corpo(LexicalAnalyzer lexic, Collection<Error> errors) {
    }

    private static void dc() {

    }

    private static void dc_c() {
    }

    private static void dc_v() {
    }

    private static void tipo_var() {
    }

    private static void variaveis() {
    }

    private static void mais_var() {
    }

    private static void dc_p() {
    }

    private static void parametros() {
    }

    private static void lista_par() {
    }

    private static void mais_par() {
    }

    private static void corpo_p() {
    }

    private static void dc_loc() {
    }

    private static void lista_arg() {
    }

    private static void argumentos() {
    }

    private static void mais_ident() {
    }

    private static void pfalsa() {
    }

    private static void comandos() {
    }

    private static void cmd() {
    }

    private static void condicao() {
    }

    private static void relacao() {
    }

    private static void expressao() {
    }

    private static void op_un() {
    }

    private static void outros_termos() {
    }

    private static void op_ad() {
    }

    private static void termo() {
    }

    private static void mais_fatores() {
    }

    private static void op_mul() {
    }

    private static void fator() {
    }

    private static void numero() {
    }
}
