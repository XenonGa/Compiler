package Node;

import FileProcess.MyFileWriter;
import Identifier.FuncParam;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

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

    public MulExp getMulExp() {
        return mulExp;
    }

    public AddExp getAddExp() {
        return addExp;
    }

    public void writeNode() {
        mulExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.AddExp));
        if(addExp != null) {
            MyFileWriter.write(sign.getWholeToken());
            addExp.writeNode();
        }
    }

    public static AddExp makeAddExp() {
        MulExp mulExp = MulExp.makeMulExp();
        Token sign = null;
        AddExp addExp = null;
        if(checkCurrentTokenCategory("PLUS")) {
            sign = Parser.checkCategory("PLUS");
            addExp = makeAddExp();
        }
        else if(checkCurrentTokenCategory("MINU")) {
            sign = Parser.checkCategory("MINU");
            addExp = makeAddExp();
        }
        return new AddExp(mulExp, addExp, sign);
    }

    public static void addExpErrorHandler(AddExp addExp) {
        MulExp.mulExpErrorHandler(addExp.mulExp);
        if(addExp.addExp != null) {
            addExpErrorHandler(addExp.addExp);
        }
    }

    public static FuncParam getFuncParamFromAddExp(AddExp addExp) {
        return MulExp.getFuncParamFromMulExp(addExp.mulExp);
    }
}
