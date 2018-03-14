package semantic.AST;

import semantic.Visitors.Visitor;

public class TermNode extends Node{
    public TermNode() {
        super("");
    }

    public TermNode(Node child) {
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
