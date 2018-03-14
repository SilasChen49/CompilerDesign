package semantic.AST;

import semantic.Visitors.Visitor;

import java.util.List;

public class VarNode extends Node {
    public VarNode(){
        super("");
    }

    public VarNode(Node parent){
        super("", parent);
    }

    public VarNode(List<Node> listOfVarElementsNodes){
        super("");
        for (Node child : listOfVarElementsNodes)
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
