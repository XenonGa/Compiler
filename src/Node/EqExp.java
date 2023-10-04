package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

// 相等性表达式 EqExp → RelExp | EqExp ('==' | '!=') RelExp
public class EqExp extends Node {
    private RelExp relExp;
    private EqExp eqExp;
    private Token sign;

    public EqExp(RelExp relExp) {
        this.relExp = relExp;
    }

    public EqExp(RelExp relExp, EqExp eqExp, Token sign) {
        this.relExp = relExp;
        this.eqExp = eqExp;
        this.sign = sign;
    }

    public RelExp getRelExp() {
        return relExp;
    }

    public EqExp getEqExp() {
        return eqExp;
    }

    public void writeNode() {
        relExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.EqExp));
        if(eqExp != null) {
            MyFileWriter.write(sign.getWholeToken());
            eqExp.writeNode();
        }
    }

    public static EqExp makeEqExp() {
        RelExp relExp = RelExp.makeRelExp();
        Token sign = null;
        EqExp eqExp = null;

        if(checkCurrentTokenCategory("EQL")) {
            sign = Parser.checkCategory("EQL");
            eqExp = makeEqExp();
        }
        else if(checkCurrentTokenCategory("NEQ")) {
            sign = Parser.checkCategory("NEQ");
            eqExp = makeEqExp();
        }
        return new EqExp(relExp, eqExp, sign);
    }

    public static void eqExpErrorHandler(EqExp eqExp) {
        RelExp.relExpErrorHandler(eqExp.relExp);
        if(eqExp.eqExp != null) {
            eqExpErrorHandler(eqExp.eqExp);
        }
    }
}
