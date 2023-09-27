package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

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

    public static UnaryOp makeUnaryOp() {
        Token sign = null;
        if(checkCurrentTokenCategory("PLUS")) {
            sign = Parser.checkCategory("PLUS");
        }
        else if(checkCurrentTokenCategory("MINU")) {
            sign = Parser.checkCategory("MINU");
        }
        else {
            sign = Parser.checkCategory("NOT");
        }
        return new UnaryOp(sign);
    }
}
