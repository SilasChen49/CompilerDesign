package syntactic.dataSet;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

public class Grammar {

    private HashMap<String, ArrayList<String>> grammar;

    private HashMap<String, ArrayList<String>> semanticGrammar;

    public Grammar() {
        grammar = new HashMap<>();
        readGrammarFromFile(grammar, false);
        semanticGrammar = new HashMap<>();
        readGrammarFromFile(semanticGrammar, true);
    }

    private void readGrammarFromFile(HashMap<String, ArrayList<String>> grammarSet, boolean semantic) {
        Properties properties = new Properties();
        String propertiesFile = "properties";
        try {
            properties.load(new FileInputStream(new File(propertiesFile)));
        } catch (IOException e) {
            System.err.println(e.getStackTrace());
        }

        String grammarFile;
        if (!semantic)
            grammarFile = properties.getProperty("grammarFile");
        else
            grammarFile = properties.getProperty("semanticGrammarFile");

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(grammarFile)));){
            String string;
            while ((string=bufferedReader.readLine())!=null){
                if (string.isEmpty())
                    continue;
                String[] singleGrammr = string.split("\\s+");
                ArrayList<String> extension = new ArrayList<>();
                for (int i=2; i<singleGrammr.length; i++) {
                    extension.add(singleGrammr[i]);
                }
                grammarSet.put(singleGrammr[0], extension);
            }
        } catch (IOException e) {
            System.err.println(e.getStackTrace());
            return;
        }
    }

    public void printGrammar(){
        for(HashMap.Entry<String,ArrayList<String>> entry : grammar.entrySet()){
            System.out.print(entry.getKey()+" -> ");
            for (String s : entry.getValue())
                System.out.print(s+" ");
            System.out.println();
        }
    }

    public HashMap<String, ArrayList<String>> getGrammar() {
        return grammar;
    }

    public HashMap<String, ArrayList<String>> getSemanticGrammar() {return semanticGrammar;}
}
