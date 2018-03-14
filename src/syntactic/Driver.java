package syntactic;

import syntactic.dataSet.Grammar;
import syntactic.dataSet.TokenSet;

public class Driver {

    public static void main(String[] args) {
        Grammar grammar = new Grammar();
//        grammar.printGrammar();
        TokenSet tokenSet = new TokenSet(grammar);
        tokenSet.generateSets();
//        tokenSet.printNonTerminals();
//        tokenSet.printProduction(tokenSet.productions);
//        tokenSet.printProduction(tokenSet.semanticProductions);
//        System.out.println();
//        tokenSet.generateFirstSet();
//        tokenSet.generateFollowSet();

//        tokenSet.printSet(tokenSet.firstSet);
//        System.out.println();
//        tokenSet.printSet(tokenSet.followSet);
//        System.out.println();
//        tokenSet.generateParseTable();
//        tokenSet.printParseTable();
//
        Parser parser = new Parser();
        parser.setTokenSet(tokenSet);
        parser.init();
        for (int i = 0; i < 1; i++)
            parser.parse(i);

    }
}
