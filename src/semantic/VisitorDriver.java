package semantic;

import java.util.Arrays;
import semantic.AST.*;
import semantic.Visitors.*;

public class VisitorDriver {
	
	public static void main(String[] args){
		System.out.println("==CREATING TREE==");
		
		// assignment statement subtree for a=a+b*c 
		Node a           = new IdNode("a", "int");
		Node b           = new IdNode("b", "int");
		Node c           = new IdNode("c", "int");
		Node times       = new MultOpNode("*", b, c);
		Node plus        = new AddOpNode("+", a, times); 
		Node stat1       = new AssignStatNode(a,plus); 

		// assignment statement subtree for x=a+b*c 
		Node x           = new IdNode("x", "float");
		Node times1      = new MultOpNode("*", b, c);
		Node plus1       = new AddOpNode("+", a, times1); 
		Node stat2       = new AssignStatNode(x,plus1); 

		// assignment statement subtree for a=x+z*y
		Node y           = new IdNode("y", "int");
		Node z           = new IdNode("z", "float");
		Node times2      = new MultOpNode("*", z, y);
		Node plus2       = new AddOpNode("+", x, times2);
		Node stat3       = new AssignStatNode(a,plus2); 		
		
		// variable declaration subtree for int a[5][3][2]
		Node type        = new TypeNode("int");
		Node id          = new IdNode("a");
		Node d1          = new NumNode("5");
		Node d2          = new NumNode("3");
		Node d3          = new NumNode("2");
		Node dimlist     = new DimListNode(Arrays.asList(d1,d2,d3));
		Node vd1         = new VarDeclNode(type, id, dimlist);

		// variable declaration subtree for float var1[10]
		Node type2       = new TypeNode("float");
		Node id2         = new IdNode("var1");
		Node d4          = new NumNode("10");
		Node dimlist2    = new DimListNode(Arrays.asList(d4));
		Node vd2         = new VarDeclNode(type2, id2, dimlist2);

		// variable declaration subtree for a float f[100]
		Node type3       = new TypeNode("float");
		Node id3         = new IdNode("f");
		Node d5          = new NumNode("100");
		Node dimlist3    = new DimListNode(Arrays.asList(d5));
		Node vd3         = new VarDeclNode(type3, id3, dimlist3);
		
		// class containing a vd3
		Node c1          = new IdNode("c1");
		Node cl1         = new ClassNode(c1, Arrays.asList(vd3));

		// class containing a vd1, vd2, vd3
		Node c2          = new IdNode("c2");
		Node cl2         = new ClassNode(c2, Arrays.asList(vd1,vd2,vd3));
		
		// statement block containing vd3, stat1
		Node statblock1  = new StatBlockNode(Arrays.asList(vd3,stat1));
		
		// statement block containing vd1, vd3, stat1, stat2
		Node statblock2  = new StatBlockNode(Arrays.asList(vd1,vd3,stat1,stat2));
		
		// statement block containing vd1, vd3, stat1, stat2
		Node statblock3  = new StatBlockNode(Arrays.asList(vd1,vd1,vd1,vd2,vd3,stat1,stat2, stat3));
		
		// function definition 1
		Node f			 = new IdNode("func1");
		Node pl			 = new AparamListNode(Arrays.asList(vd1, vd3));
		Node fd1		 = new FuncDefNode(type, f, f, pl, statblock1);
		
		// function definition 2
		Node f2			 = new IdNode("func2");
		Node pl2		 = new AparamListNode(Arrays.asList(vd3, vd3));
		Node fd2		 = new FuncDefNode(type2, f, f2, pl2, statblock2);

		// program is composed of a class list, a function definition list, and a statement block
		Node classlist   = new ClassListNode(Arrays.asList(cl1, cl2));
		Node funcdeflist = new FuncDefListNode(Arrays.asList(fd1, fd2));
		Node prog        = new ProgNode(classlist, funcdeflist, statblock3);

		System.out.println("==TREE CREATED=======");

		System.out.println("==PRINTING TREE======");
		prog.print();
		System.out.println("==TREE PRINTED=======");
		
		System.out.println("==VISITING TREE WITH STRING CONSTRUCTION VISITOR======");
		ConstructAssignmentAndExpressionStringVisitor CSVisitor = new ConstructAssignmentAndExpressionStringVisitor(); 
		prog.accept(CSVisitor);
		System.out.println("==TREE VISITED WITH STRING CONSTRUCTION VISITOR=======");
		
		System.out.println("==PRINTING TREE======");
		prog.print();
		System.out.println("==TREE PRINTED=======");		
		
		System.out.println("==VISITING TREE WITH TYPE CHECKING VISITOR======");
		TypeCheckingVisitor TCVisitor = new TypeCheckingVisitor(); 
		prog.accept(TCVisitor);
		System.out.println("==TREE VISITED WITH TYPE CHECKING VISITOR=======");
		
		System.out.println("==PRINTING TREE======");
		prog.print();
		System.out.println("==TREE PRINTED=======");
		
		System.out.println("==VISITING TREE WITH SYMBOL TABLE VISITOR======");
		SymTabCreationVisitor STCVisitor = new SymTabCreationVisitor(); 
		prog.accept(STCVisitor);
		System.out.println("==TREE VISITED WITH SYMBOL TABLE VISITOR=======");

		System.out.println("==PRINTING TABLE=====");		
		System.out.println(prog.symtab);
		System.out.println("==TABLE PRINTED=======");

		System.out.println("==PRINTING TREE======");
		prog.print();
		System.out.println("==TREE PRINTED=======");
	}
}