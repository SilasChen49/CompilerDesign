package semantic.AST;
import java.util.ArrayList;
import java.util.List;

import semantic.SymbolTable.SymTab;
import semantic.SymbolTable.SymTabEntry;
import semantic.Visitors.Visitor;

public abstract class Node {
	
    private List<Node> children    = new ArrayList<>();
    private Node parent            = null;
    private String data            = null;
    private static int nodelevel   = 0;

    // The following data members have been added
    // during the implementation of the visitors
    // These could be added using a decorator pattern
    // triggered by a visitor
    private String type            = null;
    private String subtreeString   = null;
    public SymTab symtab           = null;
    public SymTabEntry symtabentry = null;
    
    public Node(String data) {
        this.setData(data);
    }

    public Node(String data, Node parent) {
        this.setData(data);
        this.setParent(parent);
        parent.addChild(this);
    }
    
    public Node(String data, String type) {
        this.setData(data);
        this.setType(type);
    }    

    public List<Node> getChildren() {
        return children;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public void addChild(Node child) {
        child.setParent(this);
        this.children.add(child);
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtreeString() {
        return this.subtreeString;
    }

    public void setSubtreeString(String data) {
        this.subtreeString = data;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        if(this.children.size() == 0) 
            return true;
        else 
            return false;
    }

    public void removeParent() {
        this.parent = null;
    }

    public void print(){
    	System.out.println("===================================================================================");
    	System.out.println("Node type                 | data  | type      | subtreestring | symtabentry");
    	System.out.println("===================================================================================");
    	this.printSubtree();
    	System.out.println("===================================================================================");

    }

    public void printSubtree(){
    	for (int i = 0; i < Node.nodelevel; i++ )
    		System.out.print("  ");
    	
    	String toprint = String.format("%-25s" , this.getClass().getName()); 
    	for (int i = 0; i < Node.nodelevel; i++ )
    		toprint = toprint.substring(0, toprint.length() - 2);
    	toprint += String.format("%-8s" , (this.getData() == null || this.getData().isEmpty())         ? " | " : " | " + this.getData());    	
    	toprint += String.format("%-12s" , (this.getType() == null || this.getType().isEmpty())         ? " | " : " | " + this.getType());
    	toprint += String.format("%-16s" , (this.subtreeString == null || this.subtreeString.isEmpty()) ? " | " : " | " + this.subtreeString);
    	toprint += (this.symtabentry == null)                                   ? " | " : " | " + this.symtabentry.m_entry;
    	
    	
    	System.out.println(toprint);
    	
    	Node.nodelevel++;
    	List<Node> children = this.getChildren();
		for (int i = 0; i < children.size(); i++ ){
			children.get(i).printSubtree();
		}
		Node.nodelevel--;
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