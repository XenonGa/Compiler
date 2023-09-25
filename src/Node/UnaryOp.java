package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;

// 单目运算符 UnaryOp → '+' | '−' | '!'
public class UnaryOp extends Node {
    private Token sign;

    public UnaryOp(Token sign) {
        this.sign = sign;
    }

    public Token getSign() {
        return sign;
    }

    public void writeNode() {
        MyFileWriter.write(sign.getWholeToken());
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.UnaryOp));
    }
}
