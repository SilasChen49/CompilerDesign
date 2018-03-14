package semantic.AST;

import semantic.Visitors.Visitor;

import java.util.List;

public class FuncDeclNode extends Node {
    public FuncDeclNode(){
        super("");
    }

    public FuncDeclNode(Node parent){
        super("", parent);
    }

    public FuncDeclNode(Node typeNode, Node idNode, Node fparamListNode){
        super("");
        this.addChild(typeNode);
        this.addChild(idNode);
        this.addChild(fparamListNode);
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
