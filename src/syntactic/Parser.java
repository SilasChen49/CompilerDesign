package syntactic;

import lexical.Scanner;
import lexical.Token;
import lexical.TokenType;
import semantic.AST.*;
import syntactic.dataSet.TokenSet;

import java.io.*;
import java.net.IDN;
import java.util.*;

public class Parser {

    public TokenSet tokenSet;

    String[] nodeSet = {
            "id", "intNum", "floatNum",
            "int", "float",
            "==", "<>", "<", ">", "<=", ">=",
            "*", "/", "and",
            "+", "-", "or",
            };

    HashSet<String> node = new HashSet<>();
    HashSet<String> semanticAction = new HashSet<>();

    HashSet<Class> statBlock = new HashSet<>();
    HashSet<Class> stat = new HashSet<>();
    HashSet<Class> membDecl = new HashSet<>();
    HashSet<Class> expr = new HashSet<>();
    HashSet<Class> arithExpr = new HashSet<>();
    HashSet<Class> term = new HashSet<>();
    HashSet<Class> factor = new HashSet<>();
    HashSet<Class> varElement = new HashSet<>();

    public Stack<String> stack = new Stack<>();

    public Stack<Node> treeStack = new Stack<>();


    PrintStream errorPrinter, outputPrinter, astPrinter;
    FileInputStream reader;
    String[] inputFiles, outputFiles, errorFiles, astFiles;

    Parser() {
        for (int i = 100; i < 300; i++) {
            semanticAction.add("A" + i);
        }

        initClassSet();

        for (String s : nodeSet)
            node.add(s);
    }

    public void init() {
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

        String ast = properties.getProperty("astFile");
        astFiles = ast.split(",");

        String error = properties.getProperty("errorFile");
        errorFiles = error.split(",");
    }

    public boolean parse(int i) {

        try {
            reader = new FileInputStream(inputFiles[i]);
            outputPrinter = new PrintStream(new FileOutputStream(outputFiles[i]));
            errorPrinter = new PrintStream(new FileOutputStream(errorFiles[i]));
            astPrinter = new PrintStream(new FileOutputStream(astFiles[i]));
        } catch (FileNotFoundException e) {
            System.err.println(e);
            return false;
        }

        Scanner scanner = new Scanner(reader);

        stack.push("$");
        stack.push("prog");
        Token aToken = scanner.nextToken();
        String a = nextToken(aToken);
        System.out.println("====" + a);
        //while (!stack.empty())
        while (!stack.empty()) {
            String x = stack.peek();
            System.out.println("here is the top of stack : " + x);
            printStack();
            if (semanticAction.contains(x)) {
                treeStack.push(doSemanticAction(x));
                stack.pop();
            } else if (tokenSet.terminals.contains(x)) {
//                System.out.print("why not");
                if (x.equals(a)) {
                    stack.pop();
                    if (node.contains(x)) {
                        Node node = null;
                        if (x.equals("id"))
                            node = new IdNode(aToken.getLexeme());
                        else if (x.equals("floatNum"))
                            node = new NumNode(aToken.getLexeme(), "float");
                        else if (x.equals("intNum"))
                            node = new NumNode(aToken.getLexeme(), "int");
                        else if (x.equals("int") || x.equals("float"))
                            node = new TypeNode(aToken.getLexeme());
                        else if (x.equals("+") || x.equals("-") || x.equals("or"))
                            node = new AddOpNode(aToken.getLexeme());
                        else if (x.equals("*") || x.equals("/") || x.equals("and"))
                            node = new MultOpNode(aToken.getLexeme());
                        else
                            node = new RelOpNode(aToken.getLexeme());
                        treeStack.push(node);
                    }

//                    System.out.println("popopopop");
                    aToken = scanner.nextToken();
                    if (aToken != null)
                        a = nextToken(aToken);
                    else
                        a = "$";
                } else {
//                    skipErrors(x, a);
                    aToken = scanner.nextToken();
                    a = nextToken(aToken);
                    errorPrinter.println("Does not find error in the Follow Sets, does scan()");
                    errorPrinter.println("Error token : " + a);
                    errorPrinter.println("ERROR" + " : Line " + scanner.getLineNumber());
                    errorPrinter.println();
                }

//                if (node.contains(x)) {
//                    System.out.println("terminal push : " + x);
//                    Token token = stack.pop();
//                    if (aToken)
//                    treeStack.push(node);
//                }
            }
//        skipErrors() ; error = true
            else {
                if (tokenSet.parseTable.get(x).containsKey(a)) {
                    stack.pop();
                    inverseRHSMultiplePush(tokenSet.parseTable.get(x).get(a));
                } else {
                    if (!skipErrors(x, a))
                        a = nextToken(scanner.nextToken());
                    errorPrinter.println("ERROR" + " : Line " + scanner.getLineNumber());
                    errorPrinter.println();
//                    printError(scanner.getLineNumber());
                }
            }
            System.out.println();
        }

        System.out.println("------------");
        printTree(treeStack.pop());
        return true;
    }

    public String nextToken(Token token) {
        TokenType tokenType = token.getTokenType();
        System.out.println("--------" + tokenType + " : " + token.getLexeme());
        if (tokenType.equals(TokenType.TOKEN_ID))
            return "id";
        else if (tokenType.equals(TokenType.TOKEN_FLOAT))
            return "floatNum";
        else if (tokenType.equals(TokenType.TOKEN_INTEGER))
            return "intNum";
        else
            return token.getLexeme();

    }

    public Node doSemanticAction(String x) {
        Node node = null;
        List<Node> nodeList = new ArrayList<>();
        switch (x) {
            // prog
            case "A100": {
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                Node node3 = treeStack.pop();
                node = new ProgNode(node3, node2, node1);
                break;
            }
            // class list
            case "A101": {
                nodeList = popList(ClassDeclNode.class);
                node = new ClassListNode(nodeList);
                break;
            }
            // function definition list
            case "A102": {
                nodeList = popList(FuncDefNode.class);
                node = new FuncDefListNode(nodeList);
                break;
            }
            // inheritance list
            case "A103": {
                nodeList = popList(IdNode.class);
                node = new InherListNode(nodeList);
                break;
            }
            //  member list
            case "A104": {
                nodeList = popList(membDecl);
                node = new MembListNode(nodeList);
                break;
            }
            // arithmetic parameter list
            case "A105": {
                nodeList = popList(expr);
                node = new AparamsNode(nodeList);
                break;
            }
            // function parameter list
            case "A106": {
                nodeList = popList(FparamNode.class);
                node = new FparamListNode(nodeList);
                break;
            }
            // dimension list
            case "A107": {
                nodeList = popList(NumNode.class);
                node = new DimListNode(nodeList);
                break;
            }
            // index list
            case "A108": {
                nodeList = popList(ArithExprNode.class);
                node = new IndexListNode(nodeList);
                break;
            }
            // state block
            case "A109" :{
                nodeList = popList(statBlock);
                node = new StatBlockNode(nodeList);
                break;
            }
            // function declaration
            case "A110": {
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                Node node3 = treeStack.pop();
                node = new FuncDeclNode(node3, node2, node1);
                break;
            }
            // variable declaration
            case "A111": {
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                Node node3 = treeStack.pop();
                node = new VarDeclNode(node3, node2, node1);
                break;
            }
            // function parameter
            case "A112": {
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                Node node3 = treeStack.pop();
                node = new FparamNode(node3, node2, node1);
                break;
            }
            //
            case "A113": {
                break;
            }
            //function definition
            case "A114": {
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                Node node3 = treeStack.pop();
                Node node4 = treeStack.pop();
                Node node5 = treeStack.pop();
                node = new FuncDefNode(node5, node3, node4, node2, node1);
                break;
            }
            // scope specification
            case "A115": {
                node = new ScopeSpecNode();
                break;
            }
            // class declaration
            case "A116":{
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                node = new ClassDeclNode(node2, node1);
                break;
            }
            // scope specification with id
            case "A117":{
                Node node1 = treeStack.pop();
                node = new ScopeSpecNode(node1);
                break;
            }
            // term
            case "A120": {
                Node node1 = treeStack.pop();
                node = new TermNode(node1);
                break;
            }
            // factor
            case "A121": {
                Node node1 = treeStack.pop();
                node = new FactorNode(node1);
                break;
            }
            // arithmetic expression
            case "A122": {
                Node node1 = treeStack.pop();
                node = new ArithExprNode(node1);
                break;
            }
            // variable list
            case "A140":{
                nodeList = popList(varElement);
                node = new VarNode(nodeList);
                break;
            }
            // data member
            case "A141":{
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                node = new DataMemberNode(node2, node1);
                break;
            }
            // function call
            case "A142":{
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                node = new FCallNode(node2, node1);
                break;
            }
            // if state
            case "A150": {
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                Node node3 = treeStack.pop();
                node = new IfStatNode(node3, node2, node1);
                break;
            }
            // assignment state
            case "A151":{
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                node = new AssignStatNode(node2, node1);
                break;
            }
            // for state
            case "A152":{
                Node node1 = treeStack.pop();
                Node node3 = treeStack.pop();
                Node node4 = treeStack.pop();
                Node node5 = treeStack.pop();
                Node node6 = treeStack.pop();
                Node node2 = node1.getChildren().get(0);
                node1.getChildren().remove(node2);
                node = new ForStatNode(node6, node5, node4, node3, node2, node1);
                break;
            }
            // get state
            case "A153":{
                Node node1 = treeStack.pop();
                node = new GetStatNode(node1);
                break;
            }
            // put state
            case "A154":{
                Node node1 = treeStack.pop();
                node = new PutStatNode(node1);
                break;
            }
            // return state
            case "A155":{
                Node node1 = treeStack.pop();
                node = new ReturnStatNode(node1);
                break;
            }

            // add operation
            case "A160": {
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                Node node3 = treeStack.pop();
                node = new AddOpNode(node2.getData(), node3, node1);
                break;
            }
            //relational operation
            case "A161":{
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                Node node3 = treeStack.pop();
                node = new RelExprNode(node3, node2, node1);
                break;
            }
            // multiply operation
            case "A162":{
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                Node node3 = treeStack.pop();
                node = new MultOpNode(node2.getData(), node3, node1);
                break;
            }
            // not operation
            case "A163":{
                Node node1 = treeStack.pop();
                node = new NotNode(node1);
                break;
            }
            // sign
            case "A164":{
                Node node1 = treeStack.pop();
                Node node2 = treeStack.pop();
                node = new SignNode(node2.getData(), node1);
                break;
            }

        }
        return node;
    }

    public List<Node> popList(Class c) {
        List<Node> nodeList = new ArrayList<>();
        while (!treeStack.empty() && treeStack.peek().getClass().equals(c)) {
            nodeList.add(treeStack.pop());
        }
        List<Node> newNodeList = new ArrayList<>();
        for (int i=nodeList.size()-1; i>=0; i--)
            newNodeList.add(nodeList.get(i));
        return newNodeList;
    }

    public List<Node> popList(HashSet<Class> c) {
        List<Node> nodeList = new ArrayList<>();
        while (!treeStack.empty() && c.contains(treeStack.peek().getClass())) {
            nodeList.add(treeStack.pop());
        }
        List<Node> newNodeList = new ArrayList<>();
        for (int i=nodeList.size()-1; i>=0; i--)
            newNodeList.add(nodeList.get(i));
        return newNodeList;
    }

    public void printStack() {
        Stack<Node> stack = new Stack<>();
        System.out.print("Print Stack : ");
        while (!treeStack.empty()) {
            System.out.print(treeStack.peek().getClass().getName().replaceAll("semantic.AST.", "") + " ");
            stack.push(treeStack.pop());
        }
        while (!stack.empty()) {
            treeStack.push(stack.pop());
        }
        System.out.println();
    }

    public boolean skipErrors(String x, String a) {
        System.out.print("----ERROR" + x);
        if (tokenSet.followSet.get(x).contains(a)) {
            stack.pop();
            TreeNode node = new TreeNode(x, false);
//            treeStack.push(node);
            errorPrinter.println("Find error in the Follow Sets, does pop()");
            errorPrinter.println("Error token : " + a);
            return true;
        } else {
            errorPrinter.println("Does not find error in the Follow Sets, does scan()");
            errorPrinter.println("Error token : " + a);
            return false;
        }
    }

    public void inverseRHSMultiplePush(int x) {
        ArrayList<String> strings = tokenSet.semanticProductions.get(x);
        System.out.print(strings.get(0) + " -> ");
        for (int i = 1; i < strings.size(); i++)
            System.out.print(strings.get(i) + " ");
        System.out.println();

        for (int i = strings.size() - 1; i > 0; i--) {
            if (!strings.get(i).equals("EPSILON")) {
                stack.push(strings.get(i));
                System.out.print(strings.get(i)+" ");
            }
        }
        strings = tokenSet.productions.get(x);
        outputPrinter.print(strings.get(0) + " -> ");
        for (int i = 1; i < strings.size()-1; i++)
            outputPrinter.print(strings.get(i) + " ");
        outputPrinter.println();
        System.out.println("----------Derivation-----------");
    }

    public void printTree(Node node) {
        if (node.getChildren().size() > 0) {
            astPrinter.print(node.getClass().getName().replaceAll("semantic.AST.", "") + " -> ");
            for (Node child : node.getChildren()) {
                astPrinter.print(child.getClass().getName().replaceAll("semantic.AST.", "") + "(" + child.getData() + ") ");
            }
            astPrinter.println();
        }
        for (Node child : node.getChildren())
            printTree(child);
    }

    public void setTokenSet(TokenSet tokenSet) {
        this.tokenSet = tokenSet;
    }

    public void initClassSet(){

        stat.add(IfStatNode.class);
        stat.add(AssignStatNode.class);
        stat.add(ForStatNode.class);
        stat.add(ReturnStatNode.class);
        stat.add(GetStatNode.class);
        stat.add(PutStatNode.class);

        statBlock.add(VarDeclNode.class);
        statBlock.addAll(stat);

        membDecl.add(VarDeclNode.class);
        membDecl.add(FuncDeclNode.class);

        varElement.add(DataMemberNode.class);
        varElement.add(FCallNode.class);

        factor.add(NumNode.class);
        factor.add(VarNode.class);
        factor.add(FCallNode.class);
        factor.add(ArithExprNode.class);
        factor.add(NotNode.class);
        factor.add(SignNode.class);

        term.addAll(factor);

        expr.add(ArithExprNode.class);
        expr.add(RelExprNode.class);
    }
}
