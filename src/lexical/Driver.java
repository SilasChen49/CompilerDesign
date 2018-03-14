package lexical;

import java.io.*;
import java.util.Properties;

public class Driver {

    private String[] inputFiles;
    private String[] outputFiles;
    private String errorFile;
    private String atoccFile;
    PrintStream errorOutput;
    PrintStream atoccOutput;

    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.start();
//        driver.groupRun();
        driver.run(0);
    }

    public void start() {
        Properties properties = new Properties();
        String propertiesFile = "properties";
        try {
            properties.load(new FileInputStream(new File(propertiesFile)));
        } catch (IOException e) {
            System.err.println(e);
        }

        String inputs = properties.getProperty("inputFile");
        inputFiles = inputs.split(",");

        String outputs = properties.getProperty("outputFile");
        outputFiles = outputs.split(",");

        errorFile = properties.getProperty("errorFile1");

        atoccFile = properties.getProperty("atoccFile");

        try {
            errorOutput = new PrintStream(new FileOutputStream(errorFile));
            atoccOutput = new PrintStream(new FileOutputStream(atoccFile));
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return;
        }
    }


    public void run(int index) {
        Cleaner.clean(inputFiles[index]);

        FileInputStream input;
        try {
            input = new FileInputStream(inputFiles[index]);
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return;
        }

        PrintStream output;
        try {
            output = new PrintStream(new FileOutputStream(outputFiles[index]));
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return;
        }

        Scanner tokenizer = new Scanner(input);
        Token token = null;
        do {
            token = tokenizer.nextToken();
                if (token!=null) {
                    if (token.getTokenType().equals(TokenType.TOKEN_ERROR)){
                        errorOutput.println(token);
                    }
                    else {
                        output.println(token);
                    }
                    atoccOutput.print(typeToString(token.getTokenType())+"+");
                }
        } while (!tokenizer.done());

    }

    public void groupRun() {
        int index = 0;
        while (index < inputFiles.length && index < outputFiles.length) {
            run(index);
            index++;
        }
    }

    public String typeToString(TokenType tokenType){
        if (tokenType.equals(TokenType.TOKEN_ERROR))
            return "error";
        else if (tokenType.equals(TokenType.TOKEN_ID))
            return "id";
        else if (tokenType.equals(TokenType.TOKEN_PUNCTUATION))
            return "punctuation";
        else if (tokenType.equals(TokenType.TOKEN_FLOAT))
            return "float";
        else if (tokenType.equals(TokenType.TOKEN_INTEGER))
            return "int";
        else
            return "reservedWord";
    }
}
