package syntactic;

import apple.laf.JRSUIUtils;

import java.util.ArrayList;

public class TreeNode {

    Boolean terminal;
    String value;
    TreeNode parent;
    ArrayList<TreeNode> children;

    TreeNode(String s, boolean terminal){
        children = new ArrayList<>();
        this.value = s;
        this.terminal = terminal;
    }

    public void linkChild(TreeNode node){
        children.add(node);
        node.parent = this;
    }


}
