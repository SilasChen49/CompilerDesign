package semantic.AST;

import semantic.Visitors.Visitor;

import java.util.List;

public class EpsilonNode extends Node {
    public EpsilonNode(){
        super("EPSILON");
    }

    public EpsilonNode(Node parent){
        super("EPSILON", parent);
    }

    /**
     * If a visitable class contains members that also may need
     * to be visited, it should make these members to accept
     * the visitor before itself being visited.
     */
    public void accept(Visitor visitor) {
        for (Node child : this.getChildren())
            child.accept(visitor);
        visitor.visit(this);
    }
}
