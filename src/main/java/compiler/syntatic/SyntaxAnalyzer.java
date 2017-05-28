package compiler.syntatic;


import compiler.error.Error;
import compiler.lexic.LexicalAnalyzer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SyntaxAnalyzer {

    private LexicalAnalyzer lexic;
    private List<Error> erros = new ArrayList<>();

    public SyntaxAnalyzer(LexicalAnalyzer lexic) {
        this.lexic = lexic;
    }

    public void Analyze() {
        RuleAnalyzer.initial(lexic, erros);

        erros.sort((e1, e2) -> e1.getLine() - e2.getLine() != 0 ? e1.getLine() - e2.getLine() : e1.getColumn() - e2.getColumn());
    }

    public Collection<Error> getErrors() {
        return Collections.unmodifiableCollection(erros);
    }
}
