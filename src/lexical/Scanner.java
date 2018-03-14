package lexical;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;

public class Scanner {

    private int lineNumber = 0;
    private HashSet<String> matcher;
    private HashSet<Character> punctuation;
    private DFATable DFA;
    LexicalInputStream reader;
    Boolean doneFlag = false;

    public Scanner(FileInputStream fileInputStream) {
        reader = new LexicalInputStream(fileInputStream);
        initMatchMap();
        initPunctuation();
        DFA = new DFATable();
    }

    public Token nextToken() {
        Token token = null;
        State state = new State();
        int look;
        char lookup;

        //eof
        do {
            look = nextChar();
            if (look == -1) {
                doneFlag = true;
                lookup = '\n';
//                token = new Lexical.Token(Lexical.TokenType.TOKEN_FLOAT);
//                token.setLexeme(state.getValue());
//                token.setLine(reader.getLineNumber());
//                return token;
            } else
                lookup = (char) look;


            if (state.getState() == 0 && punctuation.contains(lookup)) {
                String value = processPunctuation(lookup);
                token = new Token(TokenType.TOKEN_PUNCTUATION);
                token.setLexeme(value);
                token.setLine(reader.getLineNumber());
                return token;
            }
            state = DFA.nextState(state, lookup);
//            System.out.print("state=" + state.getState() + ",");
            if (DFA.isRollBack(state)) {
//                System.out.print("here is roll back man");
                try {
                    reader.unread(look);
                } catch (IOException e) {

                }
            } else {
                if (lookup != ' ' && lookup != '\n')
                    state.extend(lookup);
            }

            if (DFA.isFinalState(state)) {
                TokenType tokenType = DFA.getTokenType(state);
                if (tokenType == TokenType.TOKEN_ID && matcher.contains(state.getValue()))
                    tokenType = TokenType.TOKEN_RESERVED_WORDS;
                token = new Token(tokenType);
                token.setLexeme(state.getValue());
                token.setLine(reader.getLineNumber());
            }

        } while (token == null && !doneFlag);
//        System.out.println();

        return token;
    }


    public int nextChar() {
        int x = -1;
        try {
            x = reader.read();
            if (x == -1) {
//                System.out.println("end of file");
            }
            else
                return x;
        } catch (IOException e) {
            System.err.println(e);
        }
        return x;
    }

    public String processPunctuation(char lookup) {
        String value = "";
        char next = ' ';
        try {
            switch (lookup) {
                case '=': {
                    next = (char) reader.read();
                    if (next == '=')
                        value = "==";
                    else {
                        value = "=";
                        reader.unread(next);
                    }
                    break;
                }
                case '<': {
                    next = (char) reader.read();
                    if (next == '>')
                        value = "<>";
                    else if (next == '='){
                        value = "<=";
                    }else {
                        value = "<";
                        reader.unread(next);
                    }
                    break;
                }
                case '>': {
                    if (next == '=')
                        value = ">=";
                    else{
                        value = ">";
                        reader.unread(next);
                    }
                    break;
                }
                case ';': {
                    value = ";";
                    break;
                }
                case ',': {
                    value = ",";
                    break;
                }
                case ':': {
                    next = (char) reader.read();
                    if (next == ':')
                        value = "::";
                    else {
                        value = ":";
                        reader.unread(next);
                    }
                    break;
                }
                case '*': {
                    value = "*";
                    break;
                }
                case '/': {
                    next = (char) reader.read();
                    if (next == '*')
                        value = "/*";
                    else if (next == '/')
                        value = "//";
                    else{
                        value = "/";
                        reader.unread(next);
                    }
                    break;
                }
                case '(': {
                    value = "(";
                    break;
                }
                case ')': {
                    value = ")";
                    break;
                }
                case '{': {
                    value = "{";
                    break;
                }
                case '}': {
                    value = "}";
                    break;
                }
                case '[': {
                    value = "[";
                    break;
                }
                case ']': {
                    value = "]";
                    break;
                }
                default: {
                    break;
                }
            }
        }catch (IOException e){

        }
        return value;
    }

    public void initMatchMap() {
        matcher = new HashSet<>();
        matcher.add("and");
        matcher.add("not");
        matcher.add("or");
        matcher.add("if");
        matcher.add("then");
        matcher.add("else");
        matcher.add("for");
        matcher.add("class");
        matcher.add("int");
        matcher.add("float");
        matcher.add("get");
        matcher.add("put");
        matcher.add("return");
        matcher.add("program");
    }

    public void initPunctuation() {
        punctuation = new HashSet<>();
        punctuation.add('=');
        punctuation.add('<');
        punctuation.add('>');
        punctuation.add(';');
        punctuation.add(',');
        punctuation.add(':');
        punctuation.add('*');
        punctuation.add('/');
        punctuation.add('(');
        punctuation.add(')');
        punctuation.add('{');
        punctuation.add('}');
        punctuation.add('[');
        punctuation.add(']');
    }

    public int getLineNumber(){
        return reader.getLineNumber();
    }

    public boolean done() {
        return doneFlag;
    }
}
