package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;

// 逻辑与表达式 LAndExp → EqExp | LAndExp '&&' EqExp
public class LAndExp extends Node {
    private EqExp eqExp;
    private Token and;
    private LAndExp lAndExp;

    public LAndExp(EqExp eqExp) {
        this.eqExp = eqExp;
    }

    public LAndExp(EqExp eqExp, Token and, LAndExp lAndExp) {
        this.eqExp = eqExp;
        this.and = and;
        this.lAndExp = lAndExp;
    }

    public EqExp getEqExp() {
        return eqExp;
    }

    public LAndExp getlAndExp() {
        return lAndExp;
    }

    public void writeNode() {
        if(lAndExp == null) {
            eqExp.writeNode();
        }
        else {
            lAndExp.writeNode();
            MyFileWriter.write(and.getWholeToken());
            eqExp.writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.LAndExp));
    }
}
