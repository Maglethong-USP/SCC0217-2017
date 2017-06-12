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

    private RuleAnalyzer() {
    }

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
        Debug.log("[DEBUG] Entering rule #" + 1);

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
        Debug.log("[DEBUG] Entering rule #" + 2);

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
        Debug.log("[DEBUG] Entering rule #" + 3);

        dc_c(lexic, errors, concat(syncTokens, DC_V.getFirst()));
        dc_v(lexic, errors, concat(syncTokens, DC_P.getFirst()));
        dc_p(lexic, errors, syncTokens);
    }

    private static void dc_c(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 4);

        if (!isNextSymbol(RESERVED_CONST, lexic)) {
            return;
        }
        if (!isNextSymbol(ID, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, EQUAL);
            errors.add(buildError(lexic, ID, sync));
        }
        if (!isNextSymbol(EQUAL, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, NUMBER.getFirst());
            errors.add(buildError(lexic, EQUAL, sync));
        }
        numero(lexic, errors, concat(syncTokens, SEMICOLON));
        if (!isNextSymbol(SEMICOLON, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, DC_C.getFirst());
            errors.add(buildError(lexic, SEMICOLON, sync));
        }
        dc_c(lexic, errors, syncTokens);
    }

    private static void dc_v(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 5);

        if (!isNextSymbol(RESERVED_VAR, lexic)) {
            return;
        }
        variaveis(lexic, errors, concat(syncTokens, COLON));
        if (!isNextSymbol(COLON, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, TYPE_VAR.getFirst());
            errors.add(buildError(lexic, COLON, sync));
        }
        tipo_var(lexic, errors, concat(syncTokens, SEMICOLON));
        if (!isNextSymbol(SEMICOLON, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, DC_V.getFirst());
            errors.add(buildError(lexic, SEMICOLON, sync));
        }
        dc_v(lexic, errors, syncTokens);
    }

    private static void tipo_var(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 6);

        if (!isNextSymbol(RESERVED_INTEGER, lexic) && !isNextSymbol(RESERVED_REAL, lexic)) {
            errors.add(buildError(lexic, RESERVED_REAL, syncTokens));
        }
    }

    private static void variaveis(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 7);

        if (!isNextSymbol(ID, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, MORE_VARIABLES.getFirst());
            errors.add(buildError(lexic, ID, sync));
        }
        mais_var(lexic, errors, syncTokens);
    }

    private static void mais_var(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 8);

        if (!isNextSymbol(COMMA, lexic)) {
            return;
        }
        variaveis(lexic, errors, syncTokens);
    }

    private static void dc_p(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 9);

        if (!isNextSymbol(RESERVED_PROCEDURE, lexic)) {
            return;
        }
        if (!isNextSymbol(ID, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, PARAMETER.getFirst());
            errors.add(buildError(lexic, ID, sync));
        }
        parametros(lexic, errors, concat(syncTokens, SEMICOLON));
        if (!isNextSymbol(SEMICOLON, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, BODY_P.getFirst());
            errors.add(buildError(lexic, SEMICOLON, sync));
        }
        corpo_p(lexic, errors, concat(syncTokens, DC_P.getFirst()));
        dc_p(lexic, errors, syncTokens);
    }

    private static void parametros(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 10);

        if (!isNextSymbol(PARENTHESES_OPEN, lexic)) {
            return;
        }
        lista_par(lexic, errors, concat(syncTokens, PARENTHESES_CLOSE));
        if (!isNextSymbol(PARENTHESES_CLOSE, lexic)) {
            errors.add(buildError(lexic, PARENTHESES_CLOSE, syncTokens));
        }
    }

    private static void lista_par(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 11);

        variaveis(lexic, errors, concat(syncTokens, COLON));
        if (!isNextSymbol(COLON, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, TYPE_VAR.getFirst());
            errors.add(buildError(lexic, COLON, sync));
        }
        tipo_var(lexic, errors, concat(syncTokens, MORE_VARIABLES.getFirst()));
        mais_par(lexic, errors, syncTokens);
    }

    private static void mais_par(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 12);

        if (!isNextSymbol(COMMA, lexic)) {
            return;
        }
        lista_par(lexic, errors, syncTokens);
    }

    private static void corpo_p(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 13);

        dc_loc(lexic, errors, concat(syncTokens, RESERVED_BEGIN));
        if (!isNextSymbol(RESERVED_BEGIN, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, COMMANDS.getFirst());
            errors.add(buildError(lexic, RESERVED_BEGIN, sync));
        }
        comandos(lexic, errors, concat(syncTokens, RESERVED_END));
        if (!isNextSymbol(RESERVED_END, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, SEMICOLON);
            errors.add(buildError(lexic, RESERVED_END, sync));
        }
        if (!isNextSymbol(SEMICOLON, lexic)) {
            errors.add(buildError(lexic, SEMICOLON, syncTokens));
        }
    }

    private static void dc_loc(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 14);

        dc_v(lexic, errors, syncTokens);
    }

    private static void lista_arg(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 15);

        if (!isNextSymbol(PARENTHESES_OPEN, lexic)) {
            return;
        }
        argumentos(lexic, errors, concat(syncTokens, PARENTHESES_CLOSE));
        if (!isNextSymbol(PARENTHESES_CLOSE, lexic)) {
            errors.add(buildError(lexic, PARENTHESES_CLOSE, syncTokens));
        }
    }

    private static void argumentos(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 16);

        if (!isNextSymbol(ID, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, MORE_PARAM.getFirst());
            errors.add(buildError(lexic, ID, sync));
        }
        mais_ident(lexic, errors, syncTokens);
    }

    private static void mais_ident(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 17);

        if (!isNextSymbol(SEMICOLON, lexic)) {
            return;
        }
        argumentos(lexic, errors, syncTokens);
    }

    private static void pfalsa(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 18);

        if (!isNextSymbol(RESERVED_ELSE, lexic)) {
            return;
        }
        cmd(lexic, errors, syncTokens);
    }

    private static void comandos(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 19);

        if (!lexic.hasNext() || !CMD.getFirst().contains(lexic.peek().getType())) {
            return;
        }
        cmd(lexic, errors, concat(syncTokens, SEMICOLON));
        if (!isNextSymbol(SEMICOLON, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, COMMANDS.getFirst());
            errors.add(buildError(lexic, SEMICOLON, sync));
        }
        comandos(lexic, errors, syncTokens);
    }

    private static void cmd(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 20);

        if (!lexic.hasNext()) {
            errors.add(buildError(lexic, ID, syncTokens));
        }

        switch (lexic.peek().getType()) {
            case RESERVED_READ:
            case RESERVED_WRITE:
                Debug.log("[DEBUG] Consuming " + lexic.peek());
                lexic.next();
                if (!isNextSymbol(PARENTHESES_OPEN, lexic)) {
                    Collection<TokenType> sync = concat(syncTokens, VARIABLES.getFirst());
                    errors.add(buildError(lexic, PARENTHESES_OPEN, sync));
                }
                variaveis(lexic, errors, concat(syncTokens, PARENTHESES_CLOSE));
                if (!isNextSymbol(PARENTHESES_CLOSE, lexic)) {
                    errors.add(buildError(lexic, PARENTHESES_CLOSE, syncTokens));
                }
                break;

            case RESERVED_WHILE:
                Debug.log("[DEBUG] Consuming " + lexic.peek());
                lexic.next();
                if (!isNextSymbol(PARENTHESES_OPEN, lexic)) {
                    Collection<TokenType> sync = concat(syncTokens, VARIABLES.getFirst());
                    errors.add(buildError(lexic, PARENTHESES_OPEN, sync));
                }
                condicao(lexic, errors, concat(syncTokens, PARENTHESES_CLOSE));
                if (!isNextSymbol(PARENTHESES_CLOSE, lexic)) {
                    Collection<TokenType> sync = concat(syncTokens, RESERVED_DO);
                    errors.add(buildError(lexic, PARENTHESES_CLOSE, sync));
                }
                if (!isNextSymbol(RESERVED_DO, lexic)) {
                    Collection<TokenType> sync = concat(syncTokens, CMD.getFirst());
                    errors.add(buildError(lexic, RESERVED_DO, sync));
                }
                cmd(lexic, errors, syncTokens);
                break;

            case RESERVED_IF:
                Debug.log("[DEBUG] Consuming " + lexic.peek());
                lexic.next();
                condicao(lexic, errors, concat(syncTokens, RESERVED_THEN));
                if (!isNextSymbol(RESERVED_THEN, lexic)) {
                    Collection<TokenType> sync = concat(syncTokens, CMD.getFirst());
                    errors.add(buildError(lexic, RESERVED_THEN, sync));
                }
                cmd(lexic, errors, concat(syncTokens, PFALSE.getFirst()));
                pfalsa(lexic, errors, syncTokens);
                break;

            case ID:
                Debug.log("[DEBUG] Consuming " + lexic.peek());
                lexic.next();
                if (isNextSymbol(ATTRIBUTION, lexic)) {
                    expressao(lexic, errors, syncTokens);
                } else {
                    lista_arg(lexic, errors, syncTokens);
                }
                break;

            case RESERVED_BEGIN:
                Debug.log("[DEBUG] Consuming " + lexic.peek());
                lexic.next();
                comandos(lexic, errors, concat(syncTokens, RESERVED_END));
                if (!isNextSymbol(RESERVED_END, lexic)) {
                    errors.add(buildError(lexic, RESERVED_END, syncTokens));
                }
                break;

            default:
                errors.add(buildError(lexic, ID, syncTokens));
                break;
        }
    }

    private static void condicao(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 21);

        expressao(lexic, errors, concat(syncTokens, RELATION.getFirst()));
        relacao(lexic, errors, concat(syncTokens, EXPRESSION.getFirst()));
        expressao(lexic, errors, syncTokens);
    }

    private static void relacao(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 22);

        if (!isNextSymbol(EQUAL, lexic) && !isNextSymbol(DIFFERENT, lexic)
                && !isNextSymbol(GRATER_EQUAL, lexic) && !isNextSymbol(GRATER, lexic)
                && !isNextSymbol(LESS, lexic) && !isNextSymbol(LESS_EQUAL, lexic)) {
            errors.add(buildError(lexic, EQUAL, syncTokens));
        }
    }

    private static void expressao(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 23);

        termo(lexic, errors, concat(syncTokens, OTHER_TERMS.getFirst()));
        outros_termos(lexic, errors, syncTokens);
    }

    private static void op_un(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 24);

        boolean b = isNextSymbol(ADD, lexic) || isNextSymbol(SUBTRACT, lexic);
    }

    private static void outros_termos(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 25);

        if (!lexic.hasNext() || !OP_ADD.getFirst().contains(lexic.peek().getType())) {
            return;
        }

        op_ad(lexic, errors, concat(syncTokens, TERM.getFirst()));
        termo(lexic, errors, concat(syncTokens, OTHER_TERMS.getFirst()));
        outros_termos(lexic, errors, syncTokens);
    }

    private static void op_ad(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 26);

        if (!isNextSymbol(ADD, lexic) && !isNextSymbol(SUBTRACT, lexic)) {
            errors.add(buildError(lexic, ADD, syncTokens));
        }
    }

    private static void termo(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 27);

        op_un(lexic, errors, concat(syncTokens, FACTOR.getFirst()));
        fator(lexic, errors, concat(syncTokens, MORE_FACTORS.getFirst()));
        mais_fatores(lexic, errors, syncTokens);
    }

    private static void mais_fatores(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 28);

        if (!lexic.hasNext() || !OP_MUL.getFirst().contains(lexic.peek().getType())) {
            return;
        }

        op_mul(lexic, errors, concat(syncTokens, FACTOR.getFirst()));
        fator(lexic, errors, concat(syncTokens, MORE_FACTORS.getFirst()));
        mais_fatores(lexic, errors, syncTokens);
    }

    private static void op_mul(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 29);

        if (!isNextSymbol(MULTIPLY, lexic) && !isNextSymbol(DIVIDE, lexic)) {
            errors.add(buildError(lexic, MULTIPLY, syncTokens));
        }
    }

    private static void fator(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 30);

        if (lexic.hasNext() && (NUMBER.getFirst().contains(lexic.peek().getType()) || lexic.peek().getType() == ID))  {
            Debug.log("[DEBUG] Consuming " + lexic.peek());
            lexic.next();
            return;
        }

        if (!isNextSymbol(PARENTHESES_OPEN, lexic)) {
            Collection<TokenType> sync = concat(syncTokens, EXPRESSION.getFirst());
            errors.add(buildError(lexic, PARENTHESES_OPEN, sync));
        }
        expressao(lexic, errors, concat(syncTokens, PARENTHESES_CLOSE));
        if (!isNextSymbol(PARENTHESES_CLOSE, lexic)) {
            errors.add(buildError(lexic, PARENTHESES_CLOSE, syncTokens));
        }
    }

    private static void numero(LexicalAnalyzer lexic, Collection<Error> errors, Collection<TokenType> syncTokens) {
        Debug.log("[DEBUG] Entering rule #" + 31);

        if (!isNextSymbol(INTEGER, lexic) && !isNextSymbol(REAL, lexic)) {
            errors.add(buildError(lexic, REAL, syncTokens));
        }
    }
}
