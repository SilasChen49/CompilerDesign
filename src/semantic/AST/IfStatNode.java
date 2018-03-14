package semantic.AST;

import semantic.Visitors.Visitor;

public class IfStatNode extends Node {

    public IfStatNode(){
        super("if");
    }

    public IfStatNode(Node parent){
        super("if", parent);
    }

    public IfStatNode(Node relExprNode, Node StatBlockNode1, Node StatBlockNode2){
        super("if");
        this.addChild(relExprNode);
        this.addChild(StatBlockNode1);
        this.addChild(StatBlockNode2);
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
