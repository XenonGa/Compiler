package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;

// 加减表达式 AddExp → MulExp | AddExp ('+' | '−') MulExp
public class AddExp extends Node {
    private MulExp mulExp;
    private AddExp addExp;
    private Token sign;

    public AddExp(MulExp mulExp) {
        this.mulExp = mulExp;
    }

    public AddExp(MulExp mulExp, AddExp addExp, Token sign) {
        this.mulExp = mulExp;
        this.addExp = addExp;
        this.sign = sign;
    }

    public void writeNode() {
        if(addExp == null) {
            mulExp.writeNode();
        }
        else {
            addExp.writeNode();
            MyFileWriter.write(sign.getWholeToken());
            mulExp.writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.AddExp));
    }
}
