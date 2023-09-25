package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

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
        unaryExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.MulExp));
        if(mulExp != null) {
            MyFileWriter.write(sign.getWholeToken());
            mulExp.writeNode();
        }
    }

    public static MulExp makeMulExp() {
        UnaryExp unaryExp = UnaryExp.makeUnaryExp();
        Token sign = null;
        MulExp mulExp = null;

        if(Objects.equals(Parser.currentToken.getCategory(), "MULT")) {
            sign = Parser.checkCategory("MULT");
            mulExp = makeMulExp();
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "DIV")) {
            sign = Parser.checkCategory("DIV");
            mulExp = makeMulExp();
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "MOD")) {
            sign = Parser.checkCategory("MOD");
            mulExp = makeMulExp();
        }
        return new MulExp(unaryExp, mulExp, sign);
    }
}
