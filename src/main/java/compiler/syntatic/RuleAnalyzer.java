package compiler.syntatic;


import compiler.lexic.LexicalAnalyzer;
import compiler.lexic.TokenType;

// This class holds all LALG rules. For more information check LALG_grammer.pdf at projects root
public final class RuleAnalyzer {

    public static void programa(LexicalAnalyzer lexic) {
        // FIXME: return type form lexic.next shoud be TokenType
        if(lexic.next() == TokenType.RESERVED_PROGRAM){
            if(lexic.next() == TokenType.ID) {
                if (lexic.next() == TokenType.SEMICOLOM) {
                    corpo(lexic);
                } else {
                    throw new Error("Missing semicolon");
                }
            } else {
                throw new Error("Missing identifier");
            }
        } else {
            throw new Error("Missing reserved word 'program' at programs beginning");
        }


    }

    public static void corpo(LexicalAnalyzer lexic) {
    }

    public static void dc() {

    }

    public static void dc_c() {
    }

    public static void dc_v() {
    }

    public static void tipo_var() {
    }

    public static void variaveis() {
    }

    public static void mais_var() {
    }

    public static void dc_p() {
    }

    public static void parametros() {
    }

    public static void lista_par() {
    }

    public static void mais_par() {
    }

    public static void corpo_p() {
    }

    public static void dc_loc() {
    }

    public static void lista_arg() {
    }

    public static void argumentos() {
    }

    public static void mais_ident() {
    }

    public static void pfalsa() {
    }

    public static void comandos() {
    }

    public static void cmd() {
    }

    public static void condicao() {
    }

    public static void relacao() {
    }

    public static void expressao() {
    }

    public static void op_un() {
    }

    public static void outros_termos() {
    }

    public static void op_ad() {
    }

    public static void termo() {
    }

    public static void mais_fatores() {
    }

    public static void op_mul() {
    }

    public static void fator() {
    }

    public static void numero() {
    }
}
