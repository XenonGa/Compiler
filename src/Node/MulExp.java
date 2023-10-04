package Node;

import FileProcess.MyFileWriter;
import Identifier.FuncParam;
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

        if(checkCurrentTokenCategory("MULT")) {
            sign = Parser.checkCategory("MULT");
            mulExp = makeMulExp();
        }
        else if(checkCurrentTokenCategory("DIV")) {
            sign = Parser.checkCategory("DIV");
            mulExp = makeMulExp();
        }
        else if(checkCurrentTokenCategory("MOD")) {
            sign = Parser.checkCategory("MOD");
            mulExp = makeMulExp();
        }
        return new MulExp(unaryExp, mulExp, sign);
    }

    public static FuncParam getFuncParamFromMulExp(MulExp mulExp) {
        return UnaryExp.getFuncParamFromUnaryExp(mulExp.unaryExp);
    }

    public static void mulExpErrorHandler(MulExp mulExp) {
        UnaryExp.unaryExpErrorHandler(mulExp.unaryExp);
        if(mulExp.mulExp != null) {
            mulExpErrorHandler(mulExp.mulExp);
        }
    }
}
