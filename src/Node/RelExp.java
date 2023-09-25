package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

// 关系表达式 RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
public class RelExp extends Node {
    private AddExp addExp;
    private RelExp relExp;
    private Token sign;

    public RelExp(AddExp addExp) {
        this.addExp = addExp;
    }

    public RelExp(AddExp addExp, RelExp relExp, Token sign) {
        this.addExp = addExp;
        this.relExp = relExp;
        this.sign = sign;
    }

    public AddExp getAddExp() {
        return addExp;
    }

    public RelExp getRelExp() {
        return relExp;
    }

    public void writeNode() {
        addExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.RelExp));
        if(relExp != null) {
            MyFileWriter.write(sign.getWholeToken());
            relExp.writeNode();
        }
    }

    public static RelExp makeRelExp() {
        AddExp addExp = AddExp.makeAddExp();
        Token sign = null;
        RelExp relExp = null;

        if(Objects.equals(Parser.currentToken.getCategory(), "LSS")) {
            sign = Parser.checkCategory("LSS");
            relExp = makeRelExp();
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "GRE")) {
            sign = Parser.checkCategory("GRE");
            relExp = makeRelExp();
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "LEQ")) {
            sign = Parser.checkCategory("LEQ");
            relExp = makeRelExp();
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "GEQ")) {
            sign = Parser.checkCategory("GEQ");
            relExp = makeRelExp();
        }
        return new RelExp(addExp, relExp, sign);
    }
}
