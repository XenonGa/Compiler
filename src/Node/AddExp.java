package Node;

import FileProcess.MyFileWriter;
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

        if(Objects.equals(Parser.currentToken.getCategory(), "PLUS")) {
            sign = Parser.checkCategory("PLUS");
            addExp = makeAddExp();
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "MINU")) {
            sign = Parser.checkCategory("MINU");
            addExp = makeAddExp();
        }
        return new AddExp(mulExp, addExp, sign);
    }
}
