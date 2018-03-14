package semantic.AST;

import semantic.Visitors.Visitor;

import java.util.List;

public class MembListNode extends Node {
    public MembListNode(){
        super("");
    }

    public MembListNode(Node parent){
        super("", parent);
    }

    public MembListNode(List<Node> listOfMembDeclNodes){
        super("");
        for (Node child : listOfMembDeclNodes)
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
