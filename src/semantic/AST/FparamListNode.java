package semantic.AST;

import semantic.Visitors.Visitor;

import java.util.List;

public class FparamListNode extends Node {

    public FparamListNode(){
        super("");
    }

    public FparamListNode(List<Node> listOfFparamNodes){
        super("");
        for (Node child : listOfFparamNodes)
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
