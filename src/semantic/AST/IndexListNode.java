package semantic.AST;

import semantic.Visitors.Visitor;

import java.util.List;

public class IndexListNode extends Node {
    public IndexListNode(){
        super("");
    }

    public IndexListNode(Node parent){
        super("", parent);
    }

    public IndexListNode(List<Node> listOfArithExprNode){
        super("");
        for (Node child : listOfArithExprNode)
            this.addChild(child);
    }

    /**
     * If a visitable class contains members that also may need
     * to be visited, it should make these members to accept
     * the visitor before itself being visited.
     */
    public void accept(Visitor visitor) {
        for (Node child : this.getChildren() )
            child.accept(visitor);
        visitor.visit(this);
    }
}
