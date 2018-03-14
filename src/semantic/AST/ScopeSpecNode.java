package semantic.AST;

import semantic.Visitors.Visitor;

public class ScopeSpecNode extends Node {
    public ScopeSpecNode(){
        super("");
    }

    public ScopeSpecNode(Node child){
        super("");
        this.addChild(child);
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
