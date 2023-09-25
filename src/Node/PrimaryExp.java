package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;

// 基本表达式 PrimaryExp → '(' Exp ')' | LVal | Number
public class PrimaryExp {
    private Token leftParent;
    private Exp exp;
    private Token rightParent;
    private LVal lVal;
    private NumberNode numberNode;

    public PrimaryExp(Token leftParent, Exp exp, Token rightParent) {
        this.leftParent = leftParent;
        this.exp = exp;
        this.rightParent = rightParent;
    }

    public PrimaryExp(LVal lval) {
        this.lVal = lval;
    }

    public PrimaryExp(NumberNode numberNode) {
        this.numberNode = numberNode;
    }

    public Exp getExp() {
        return exp;
    }

    public LVal getLval() {
        return lVal;
    }

    public NumberNode getNumberNode() {
        return numberNode;
    }

    public void writeNode() {
        if(exp != null) {
            MyFileWriter.write(leftParent.getWholeToken());
            exp.writeNode();
            MyFileWriter.write(rightParent.getWholeToken());
        }
        else if(lVal != null) {
            lVal.writeNode();
        }
        else {
            numberNode.writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.PrimaryExp));
    }
}
