package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;

// 关系表达式 RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
public class RelExp extends Node {
    private AddExp addExp;
    private RelExp relExp;
    private Token sign;

    public RelExp(AddExp addExp) {
        this.addExp = addExp;
    }

    public AddExp getAddExp() {
        return addExp;
    }

    public RelExp getRelExp() {
        return relExp;
    }

    public void writeNode() {
        if(relExp == null) {
            addExp.writeNode();
        }
        else {
            relExp.writeNode();
            MyFileWriter.write(sign.getWholeToken());
            addExp.writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.RelExp));
    }
}
