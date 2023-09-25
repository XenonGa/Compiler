package Node;

import FileProcess.MyFileWriter;
import Parse.NodeTypeMap;

// 表达式 Exp → AddExp
public class Exp extends Node {
    private AddExp addExp;

    public Exp(AddExp addExp) {
        this.addExp = addExp;
    }

    public AddExp getAddExp() {
        return addExp;
    }

    public void writeNode() {
        addExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.Exp));
    }

    public static Exp makeExp() {

    }
}
