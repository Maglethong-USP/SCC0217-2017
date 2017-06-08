package compiler.syntatic;


import compiler.error.ErrorType;
import compiler.error.ReachedEndOfFileException;
import compiler.lexic.LexicalAnalyzer;
import compiler.lexic.TokenType;

import static compiler.lexic.TokenType.*;

import compiler.error.Error;
import compiler.util.Debug;

import java.util.*;

import static compiler.util.CollectionUtils.concat;
import static compiler.syntatic.RuleType.*;

/**
 * This class holds all LALG rules. For more information check LALG_grammar.pdf at projects root
 */
final class RuleAnalyzer {

    private RuleAnalyzer() {}

    static void initial(LexicalAnalyzer lexic, Collection<Error> errors) {
        try {
            programa(lexic, errors, new ArrayList<>());
        } catch (ReachedEndOfFileException e) {
            errors.add(e.getError());
        }

        if (lexic.hasNext()) {
            Error err = new Error(lexic.getLine(), lexic.getColumn(), ErrorType.SYNTAX,
                    "File did not end when expected, found token " + lexic.peek().getType());
            errors.add(err);
            Debug.log("[ERROR] " + err);
        }
    }

    /**
     * Builds a syntax error.
     * <p>
     * <p/> Compares the next token in buffer with an expected token type and builds an error reporting the line,
     * column, expected type and obtained type
     *
     * @param lexic      Lexical Analyzer providing tokens
     * @param expected   Expected token
     * @param syncTokens Synchronization tokens. Consumes tokens until a sync token is found
     * @return Error object with line, column and nature of the error
     */
    private static Error buildError(LexicalAnalyzer lexic, TokenType expected, Collection<TokenType> syncTokens) {
        Error err;
        if (lexic.hasNext()) {
            err = new Error(lexic.peek().getLine(), lexic.peek().getColumn(), ErrorType.SYNTAX,
                    "Unexpected token " + lexic.peek().getType()
                            + " was expecting " + expected);

            while (lexic.hasNext() && !syncTokens.contains(lexic.peek().getType())) {
                Debug.log("[DEBUG] Skipping " + lexic.peek());
                lexic.next();
            }
            Debug.log("[ERROR] " + err);
            return err;
        }

        err = new Error(lexic.getLine(), lexic.getColumn(), ErrorType.SYNTAX,
                "File ended unexpectedly, was expecting " + expected);

        Debug.log("[ERROR] " + err);

        throw new ReachedEndOfFileException(err);
    }

    /**
     * Checks if next symbol is of desired type and consumes it
     *
     * @param type  Desired type
     * @param lexic Lexical Analyzer providing tokens
     * @return True if the symbol was found and consumed
     */
    private static boolean isNextSymbol(TokenType type, LexicalAnalyzer lexic) {
        if (lexic.hasNext() && lexic.peek().getType() == type) {
            Debug.log("[DEBUG] Consuming " + lexic.peek());
            lexic.next();
            return true;
        }
        return false;
    }

    private static void programa(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {

        if (!isNextSymbol(RESERVED_PROGRAM, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, ID);
            errors.add(buildError(lexic, RESERVED_PROGRAM, sync));
        }
        if (!isNextSymbol(ID, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, SEMICOLON);
            errors.add(buildError(lexic, ID, sync));
        }
        if (!isNextSymbol(SEMICOLON, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, BODY.getFirst());
            errors.add(buildError(lexic, SEMICOLON, sync));
        }

        corpo(lexic, errors, syncTokens);
    }

    private static void corpo(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        dc(lexic, errors, concat(syncTokens, RESERVED_BEGIN));
        if (!isNextSymbol(RESERVED_BEGIN, lexic)) {
            Collection<TokenType> sync = concat(concat(syncTokens, COMMANDS.getFirst()), RESERVED_END);
            errors.add(buildError(lexic, RESERVED_BEGIN, sync));
        }
        comandos(lexic, errors, concat(syncTokens, RESERVED_END));
        if (!isNextSymbol(RESERVED_END, lexic)) {
            errors.add(buildError(lexic, RESERVED_END, syncTokens));
        }
    }

    private static void dc(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {

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

    private static void comandos(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {

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
