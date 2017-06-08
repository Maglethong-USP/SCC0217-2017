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
    // TODO -> Follow
    PROGRAM(Collections.singletonList(RESERVED_PROGRAM), null),
    DC_C(Arrays.asList(RESERVED_CONST, null), null),
    DC_V(Arrays.asList(RESERVED_VAR, null), null),
    DC_P(Arrays.asList(RESERVED_PROCEDURE, null), null),
    DC(concat(DC_C.first, DC_V.first, DC_P.first), null),
    BODY(concat(DC.first, RESERVED_BEGIN), null),
    TYPE_VAR(Arrays.asList(REAL, INTEGER), null),
    VARIABLE(Collections.singletonList(ID), null),
    MORE_VARIABLES(Arrays.asList(COMMA, null), null),
    // TODO -> First & Follow
    PARAMETER(Arrays.asList(), null),
    LIST_PARAM(Arrays.asList(), null),
    MORE_PARAM(Arrays.asList(), null),
    BODY_P(Arrays.asList(), null),
    DC_LOC(Arrays.asList(), null),
    LIST_ARG(Arrays.asList(), null),
    PFALSE(Arrays.asList(), null),
    COMMANDS(Arrays.asList(), null),
    CMD(Arrays.asList(), null),
    CONDITION(Arrays.asList(), null),
    RELATION(Arrays.asList(), null),
    EXPRESSION(Arrays.asList(), null),
    OP_UN(Arrays.asList(), null),
    OTHER_TERMS(Arrays.asList(), null),
    OP_ADD(Arrays.asList(), null),
    TERM(Arrays.asList(), null),
    MORE_FACTORS(Arrays.asList(), null),
    OP_MUL(Arrays.asList(), null),
    FACTOR(Arrays.asList(), null),
    NUMBER(Arrays.asList(), null);



    private final Set<TokenType> first;
    private final Set<TokenType> follow;

    RuleType(Collection<TokenType> first, Collection<TokenType> follow) {
        this.first = new HashSet<>(first);
        this.follow = null; // new HashSet<>(follow);
    }

    public Set<TokenType> getFirst() {
        return Collections.unmodifiableSet(first);
    }
}
