package semantic.AST;

import semantic.Visitors.Visitor;

public class ArithExprNode extends Node{
    public ArithExprNode() {
        super("");
    }

    public ArithExprNode(Node child) {
        super("");
        this.addChild(child);
    }

    /**
     * Every class that will be visited needs an accept method, which
     * then calls the specific visit method in the visitor, achieving
     * double dispatch.
     */
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
