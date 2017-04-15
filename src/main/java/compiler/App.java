package compiler;

import java.util.Scanner;

import compiler.lexic.LexicalAnalyzer;



/**
 * Author: Maglethong Spirr
 */
public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        LexicalAnalyzer analyser = new LexicalAnalyzer(scanner);

        while (analyser.hasNext()) {
            System.out.println(analyser.next().toString());
            if (scanner.hasNext("\\\\q")) {
                break;
            }
        }

        scanner.close();
    }
}


