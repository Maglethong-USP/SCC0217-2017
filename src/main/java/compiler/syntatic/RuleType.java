package compiler.syntatic;

import compiler.lexic.TokenType;

import java.util.*;

import static compiler.lexic.TokenType.*;
import static compiler.util.CollectionUtils.concat;

/**
 * RuleType class
 *
 * @author Maglethong Spirr
 */
public enum RuleType {
    NUMBER(Arrays.asList(INTEGER, REAL)),
    FACTOR(concat(NUMBER.first, ID, PARENTHESES_OPEN)),
    OP_MUL(Arrays.asList(MULTIPLY, DIVIDE)),
    MORE_FACTORS(concat(OP_MUL.first, (TokenType) null)),
    OP_ADD(Arrays.asList(ADD, SUBTRACT)),
    OTHER_TERMS(concat(OP_ADD.first, (TokenType) null)),
    OP_UN(Arrays.asList(ADD, SUBTRACT, null)),
    TERM(OP_UN.first),
    EXPRESSION(TERM.first),
    RELATION(Arrays.asList(EQUAL, DIFFERENT, GRATER, GRATER_EQUAL, LESS, LESS_EQUAL)),
    CONDITION(EXPRESSION.first),
    CMD(Arrays.asList(RESERVED_READ, RESERVED_WRITE, RESERVED_WHILE, RESERVED_IF, ID, RESERVED_BEGIN)),
    COMMANDS(concat(CMD.first, (TokenType) null)),
    PROGRAM(Collections.singletonList(RESERVED_PROGRAM)),
    DC_C(Arrays.asList(RESERVED_CONST, null)),
    DC_V(Arrays.asList(RESERVED_VAR, null)),
    DC_P(Arrays.asList(RESERVED_PROCEDURE, null)),
    DC(concat(DC_C.first, DC_V.first, DC_P.first)),
    BODY(concat(DC.first, RESERVED_BEGIN)),
    TYPE_VAR(Arrays.asList(REAL, INTEGER)),
    VARIABLES(Collections.singletonList(ID)),
    MORE_VARIABLES(Arrays.asList(COMMA, null)),
    PARAMETER(Arrays.asList(PARENTHESES_OPEN, null)),
    LIST_PARAM(VARIABLES.first),
    MORE_PARAM(Arrays.asList(SEMICOLON, null)),
    DC_LOC(DC_V.first),
    BODY_P(DC_LOC.first),
    LIST_ARG(Arrays.asList(PARENTHESES_OPEN, null)),
    PFALSE(Arrays.asList(RESERVED_ELSE, null));

    private final Set<TokenType> first;

    RuleType(Collection<TokenType> first) {
        this.first = new HashSet<>(first);
    }

    public Set<TokenType> getFirst() {
        return Collections.unmodifiableSet(first);
    }
}
