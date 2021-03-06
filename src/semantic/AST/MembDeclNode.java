package semantic.AST;

import semantic.Visitors.Visitor;

public class MembDeclNode extends Node {

    public MembDeclNode(){
        super("");
    }

    public MembDeclNode(Node parent){
        super("", parent);
    }

    public MembDeclNode(Node leftChild, Node rightChild){
        super("");
        this.addChild(leftChild);
        this.addChild(rightChild);
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
