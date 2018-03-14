package syntactic.dataSet;

import javax.xml.ws.Endpoint;
import java.util.*;

public class TokenSet {

    public HashSet<String> terminals;

    public HashSet<String> nonTerminals;

    public ArrayList<ArrayList<String>> productions;

    public HashMap<String, HashSet<String>> firstSet;

    public HashMap<String, HashSet<String>> followSet;

    public HashMap<String, HashMap<String, Integer>> parseTable;

    public ArrayList<ArrayList<String>> semanticProductions;

    public String[] matchValue = new String[1000];

    public HashSet<String> emptySet = new HashSet<>();

    Grammar grammar;

    public TokenSet(Grammar grammar) {
        terminals = new HashSet<>();

        String[] terminalStrings = {"id", "intNum", "floatNum",
                "program", "class", "if", "then", "else", "for", "get", "put", "return", "int", "float",
                "=", "==", "<>", "<", ">", "<=", ">=", "+", "-", "*", "/", "not", "and", "or", "::",
                ":", ",", ".", ";", "[", "]", "{", "}", "(", ")",
                "EPSILON","$"};

        for (String terminal : terminalStrings)
            terminals.add(terminal);

        nonTerminals = new HashSet<>();

        for (String nonTerminal : grammar.getGrammar().keySet()) {
            nonTerminals.add(nonTerminal);
        }

        this.grammar = grammar;

        productions = new ArrayList<>();

        semanticProductions = new ArrayList<>();

    }

    public void generateSets(){

        generateProductions(grammar.getGrammar(), productions);

        generateSemanticProductions(grammar.getSemanticGrammar(), semanticProductions);

        generateFirstSet();

        generateFollowSet();

        generateParseTable();

        parseTable.get("preIdnest1p").put("[", 46);

        parseTable.get("variable1p").put(".", 61);

        parseTable.get("variable1p").put("[", 61);
    }

    public void generateParseTable(){
        int i=0;
        parseTable = new HashMap<>();
        for (ArrayList<String> production : productions){
            String key = production.get(0);
            HashMap<String, HashSet<String>> hashSetHashMap = new HashMap<>();
            hashSetHashMap.put(key, new HashSet<>());
            int j = getFirst(hashSetHashMap, production, 1, key);
            if (j==production.size()-1 && firstSet.containsKey(production.get(j-1)) && firstSet.get(production.get(j-1)).contains("EPSILON"))
                hashSetHashMap.get(key).add("EPSILON");
            if (production.get(1).equals("EPSILON"))
                hashSetHashMap.get(key).add("EPSILON");
            if (hashSetHashMap.get(key).contains("EPSILON"))
            {
                for (String s :followSet.get(key)){
                    if (parseTable.containsKey(key)){
                        parseTable.get(key).put(s,i);
                    }
                    else {
                        HashMap<String, Integer> hashMap = new HashMap<>();
                        hashMap.put(s, i);
//                        System.out.println(key + "-------" + s + "----" + i);
                        parseTable.put(key, hashMap);
                    }
                }
            }
            else {
                for (String s:hashSetHashMap.get(key)){
                    if (parseTable.containsKey(key)){
                        parseTable.get(key).put(s,i);
                    }
                    else {
                        HashMap<String, Integer> hashMap = new HashMap<>();
                        hashMap.put(s, i);
//                        System.out.println(key + "-------" + s + "----" + i);
                        parseTable.put(key, hashMap);
                    }
                }
            }
            i++;
        }
    }

    public void printParseTable(){
        for (Map.Entry<String, HashMap<String, Integer>> entry : parseTable.entrySet()){
            System.out.print(entry.getKey()+" : ");
            for (Map.Entry<String, Integer> entry1 : entry.getValue().entrySet())
                System.out.print(entry1.getKey()+", "+entry1.getValue()+", ");
            System.out.println();
        }
    }
    public void generateSemanticProductions(HashMap<String, ArrayList<String>> grammar, ArrayList<ArrayList<String>> productions){
        int j=100;
        for (Map.Entry<String, ArrayList<String>> entry : grammar.entrySet()) {
            ArrayList<String> production = new ArrayList<>();
            production.add(entry.getKey());
//            System.out.println();
            for (String token : entry.getValue()) {
//                System.out.print(token);
                if (token.equals("|")) {
//                    for (String s : production)
//                        System.out.print(s+" ");
//                    System.out.println();
                    matchValue[j] = production.get(0);
                    productions.add(production);

//                    production.add("SA"+j);
//                    semanticProductions.add(production);
//                    if (production.get(1).equals("EPSILON"))
//                        emptySet.add("SA"+j);

                    j++;
                    production = new ArrayList<>();
                    production.add(entry.getKey());
                } else {
                    production.add(token);
                }
            }
            matchValue[j] = production.get(0);
            productions.add(production);

//            production.add("SA"+j);
//            if (production.get(1).equals("EPSILON"))
//                emptySet.add("SA"+j);

            j++;
//            semanticProductions.add(production);
        }
    }

    public void generateProductions(HashMap<String, ArrayList<String>> grammar, ArrayList<ArrayList<String>> productions) {
//        productions = new ArrayList<>();
//        semanticProductions = new ArrayList<>();
        int j=100;
        for (Map.Entry<String, ArrayList<String>> entry : grammar.entrySet()) {
            ArrayList<String> production = new ArrayList<>();
            production.add(entry.getKey());
//            System.out.println();
            for (String token : entry.getValue()) {
//                System.out.print(token);
                if (token.equals("|")) {
//                    for (String s : production)
//                        System.out.print(s+" ");
//                    System.out.println();
                    matchValue[j] = production.get(0);
                    productions.add(production);

                    production.add("SA"+j);
//                    semanticProductions.add(production);
//                    if (production.get(1).equals("EPSILON"))
//                        emptySet.add("SA"+j);

                    j++;
                    production = new ArrayList<>();
                    production.add(entry.getKey());
                } else {
                    production.add(token);
                }
            }
            matchValue[j] = production.get(0);
            productions.add(production);

            production.add("SA"+j);
//            if (production.get(1).equals("EPSILON"))
//                emptySet.add("SA"+j);

            j++;
//            semanticProductions.add(production);
        }
    }

    public void generateFollowSet(){
        followSet = new HashMap<>();

        //initialization
        HashSet<String> progSet = new HashSet<>();
        progSet.add("$");

//        followSet.put("prog", progSet); //this is true
        followSet.put("prog", progSet);
        int size = productions.size();

        for (int i=0; i<size; i++){
            for (ArrayList<String> production : productions){
                for (int j=1; j<production.size(); j++)
                {
                    String s = production.get(j);
                    if (nonTerminals.contains(s)){
                        if (!followSet.containsKey(s))
                            followSet.put(s,new HashSet<>());
                        int k = getFirst(followSet, production, j+1, s);
                        if (followSet.containsKey(production.get(0))) {
                            if (k == production.size()-1 && firstSet.containsKey(production.get(k - 1)) && firstSet.get(production.get(k - 1)).contains("EPSILON")) {
                                followSet.get(s).addAll(followSet.get(production.get(0)));
                            }
//                            if (production.get(j).equals("arraySize"))
//                                System.out.println(k+"  "+production.size()+"---------");
                            if (j==(production.size()-2))
                            followSet.get(s).addAll(followSet.get(production.get(0)));
                        }
//                        if (j==produnction.size()-1)
                    }
                }
            }
        }

    }

    public void generateFirstSet() {
        firstSet = new HashMap<>();

        for (String terminal : terminals) {
            HashSet<String> set = new HashSet<>();
            set.add(terminal);
            firstSet.put(terminal, set);
        }

        int size = nonTerminals.size();
        for (int i = 0; i < size; i++) {
            for (ArrayList<String> production : productions) {
                String key = production.get(0);
                if (!firstSet.containsKey(key))
                    firstSet.put(key, new HashSet<>());

                int j = getFirst(firstSet, production, 1, key);
                if (j==production.size()-1 && firstSet.containsKey(production.get(j-1)) && firstSet.get(production.get(j-1)).contains("EPSILON"))
                    firstSet.get(key).add("EPSILON");
//                if (production.get(0).equals("variable1p")){
//                    System.out.println("-------"+j+" "+production.size());
//                }
            }
        }
    }

    public int getFirst(HashMap<String, HashSet<String>> set, ArrayList<String> production, int start, String key){
//        if (key.equals("funcBody1p")){
//            System.out.print("---------"+production.get(0));
//        }
        if (start>=production.size())
            return start;
        String token = production.get(start);
        if (firstSet.containsKey(token)){
            for (String s : firstSet.get(token)){
                if (!s.equals("EPSILON"))
                    set.get(key).add(s);
            }
            if (token.equals("EPSILON"))
                firstSet.get(key).add("EPSILON");
        }
        int j = start+1;
        while (firstSet.containsKey(production.get(j-1)) && (j<production.size()) && (firstSet.containsKey(production.get(j))) && (firstSet.get(production.get(j-1)).contains("EPSILON"))) {
            for (String s : firstSet.get(production.get(j))) {
                if (!s.equals("EPSILON"))
                    set.get(key).add(s);
            }

            j++;
        }
        return j;
    }

    public void printSet(HashMap<String, HashSet<String>> set) {
        List<HashMap.Entry<String, HashSet<String>>> infoIfs = new ArrayList<HashMap.Entry<String, HashSet<String>>>(set.entrySet());
        Collections.sort(infoIfs, new Comparator<Map.Entry<String, HashSet<String>>>() {
            @Override
            public int compare(Map.Entry<String, HashSet<String>> o1, Map.Entry<String, HashSet<String>> o2) {
                return (o1.getKey().toString().compareTo(o2.getKey().toString()));
            }
        });
        for (int i=0; i<infoIfs.size(); i++){
            String id = infoIfs.get(i).toString();
            HashSet<String> hashSet = set.get(id);
//            for (String s : hashSet)
//                System.out.print(s+" ");
            System.out.println(id);
        }
//        for (Map.Entry<String, HashSet<String>> entry : set.entrySet()) {
//            if (!terminals.contains(entry.getKey())) {
//                System.out.print(entry.getKey() + " : ");
//                for (String s : entry.getValue())
//                    System.out.print(s + " ");
//                System.out.println();
//            }
//        }
    }

    public void printProduction(ArrayList<ArrayList<String>> productions) {
        for (ArrayList<String> production : productions) {
            System.out.print(production.get(0) + " -> ");
            for (int i=1; i<production.size(); i++) {
                System.out.print(production.get(i) + " ");
            }
            System.out.println();
        }
    }

    public void printNonTerminals() {
        int i = 0;
        for (String s : nonTerminals) {
            System.out.print(s + " ");
            i++;
        }
        System.out.println(i);
    }
}
