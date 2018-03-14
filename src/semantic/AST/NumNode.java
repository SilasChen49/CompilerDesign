package semantic.AST;
import semantic.Visitors.Visitor;

public class NumNode extends Node {
	
	public NumNode(String data){
		super(data);
	}
	
	public NumNode(String data, Node parent){
		super(data, parent);
	}

	public NumNode(String data, String type){
		super(data, type);
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
