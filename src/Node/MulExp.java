package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;

// 乘除模表达式 MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp
public class MulExp extends Node {
    private UnaryExp unaryExp;
    private MulExp mulExp;
    private Token sign;

    public MulExp(UnaryExp unaryExp) {
        this.unaryExp = unaryExp;
    }

    public MulExp(UnaryExp unaryExp, MulExp mulExp, Token sign) {
        this.unaryExp = unaryExp;
        this.mulExp = mulExp;
        this.sign = sign;
    }

    public UnaryExp getUnaryExp() {
        return unaryExp;
    }

    public MulExp getMulExp() {
        return mulExp;
    }

    public void writeNode() {
        if(mulExp == null) {
            unaryExp.writeNode();
        }
        else {
            mulExp.writeNode();
            MyFileWriter.write(sign.getWholeToken());
            unaryExp.writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.MulExp));
    }
}
