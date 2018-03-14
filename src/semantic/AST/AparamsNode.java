package semantic.AST;

import semantic.Visitors.Visitor;

import java.util.List;

public class AparamsNode extends Node {
    public AparamsNode(){
        super("");
    }

    public AparamsNode(Node parent){
        super("", parent);
    }

    public AparamsNode(List<Node> listOfExprNode){
        super("");
        for (Node child : listOfExprNode)
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
