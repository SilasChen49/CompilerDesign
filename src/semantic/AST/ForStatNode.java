package semantic.AST;

import semantic.Visitors.Visitor;

public class ForStatNode extends Node {
    public ForStatNode(){
        super("for");
    }

    public ForStatNode(Node parent){
        super("for", parent);
    }

    public ForStatNode(Node typeNode, Node idNode, Node exprNode, Node relExprNode, Node assignStatNode, Node statBlockNode){
        super("for");
        this.addChild(typeNode);
        this.addChild(idNode);
        this.addChild(exprNode);
        this.addChild(relExprNode);
        this.addChild(assignStatNode);
        this.addChild(statBlockNode);
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
