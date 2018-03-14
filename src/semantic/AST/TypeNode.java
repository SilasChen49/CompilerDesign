package semantic.AST;
import semantic.Visitors.Visitor;

public class TypeNode extends Node {
	
	public TypeNode(String data){
		super(data);
	}
	
	public TypeNode(String data, Node child){
		super(data, child);
	}
	
	/**
     * Every class that will be visited needs an accept method, which 
     * then calls the specific visit method in the visitor, achieving
     * double dispatch. 
     */   
	public void accept(Visitor visitor) {
		visitor.visit(this);
	}
}
