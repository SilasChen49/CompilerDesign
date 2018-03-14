package semantic.AST;

import semantic.Visitors.Visitor;

public class RelExprNode extends Node {
    public RelExprNode(){
        super("");
    }

    public RelExprNode(Node exprNode, Node relOpNode, Node ExprNode){
        super("");
        this.addChild(exprNode);
        this.addChild(relOpNode);
        this.addChild(ExprNode);
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
