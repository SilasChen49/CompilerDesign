package semantic.AST;

import semantic.Visitors.Visitor;

public class GetStatNode extends Node {
    public GetStatNode(){
        super("get");
    }

    public GetStatNode(Node varNode){
        super("get");
        this.addChild(varNode);
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
