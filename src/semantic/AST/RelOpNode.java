package semantic.AST;

import semantic.Visitors.Visitor;

public class RelOpNode extends Node {

    public RelOpNode(String data){
        super(data);
    }

    public RelOpNode(String data, Node parent){
        super(data, parent);
    }

    public RelOpNode(String data, String type){
        super(data, type);
    }

    /**
     * Every class that may be visited by any visitor needs
     * to implement the accept method, that calls the appropriate
     * visit method in the passed visitor, achieving double
     * dispatch.
     *
     */
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
