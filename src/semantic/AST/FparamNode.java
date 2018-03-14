package semantic.AST;

import semantic.Visitors.Visitor;

public class FparamNode extends Node{
    public FparamNode(){
        super("");
    }

    public FparamNode(Node typeNode, Node idNode, Node dimListNode){
        super("");
        this.addChild(typeNode);
        this.addChild(idNode);
        this.addChild(dimListNode);
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
