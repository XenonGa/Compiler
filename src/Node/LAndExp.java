package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

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
        eqExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.LAndExp));
        if(lAndExp != null) {
            MyFileWriter.write(and.getWholeToken());
            lAndExp.writeNode();
        }
    }

    public static LAndExp makeLAndExp() {
        EqExp eqExp1 = EqExp.makeEqExp();
        Token sign = null;
        LAndExp lAndExp1 = null;

        if(checkCurrentTokenCategory("AND")) {
            sign = Parser.checkCategory("AND");
            lAndExp1 = makeLAndExp();
        }
        return new LAndExp(eqExp1, sign, lAndExp1);
    }

    public static void lAndExpErrorHandler(LAndExp lAndExp) {
        EqExp.eqExpErrorHandler(lAndExp.eqExp);
        if(lAndExp.lAndExp != null) {
            lAndExpErrorHandler(lAndExp.lAndExp);
        }
    }
}
