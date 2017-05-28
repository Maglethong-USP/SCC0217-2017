package compiler;

import java.util.Scanner;

import compiler.lexic.LexicalAnalyzer;
import compiler.syntatic.SyntaxAnalyzer;


/**
 * Author: Maglethong Spirr
 */
public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SyntaxAnalyzer syntax = new SyntaxAnalyzer(new LexicalAnalyzer(scanner));

        syntax.Analyze();

        syntax.getErrors().forEach((e) -> System.out.println(e));

        scanner.close();
    }
}


