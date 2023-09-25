package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;

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
        if(eqExp == null) {
            relExp.writeNode();
        }
        else {
            eqExp.writeNode();
            MyFileWriter.write(sign.getWholeToken());
            relExp.writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.EqExp));
    }
}
