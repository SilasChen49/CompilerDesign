package semantic.AST;

import semantic.Visitors.Visitor;

public class PutStatNode extends Node {
    public PutStatNode(){
        super("put");
    }

    public PutStatNode(Node exprNode){
        super("put");
        this.addChild(exprNode);
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
