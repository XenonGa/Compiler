package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

// 逻辑或表达式 LOrExp → LAndExp | LOrExp '||' LAndExp
public class LOrExp extends Node {
    private LAndExp lAndExp;
    private Token or;
    private LOrExp lOrExp;

    public LOrExp(LAndExp lAndExp) {
        this.lAndExp = lAndExp;
    }

    public LOrExp(LAndExp lAndExp, Token or, LOrExp lOrExp) {
        this.lAndExp = lAndExp;
        this.or = or;
        this.lOrExp = lOrExp;
    }

    public LAndExp getlAndExp() {
        return lAndExp;
    }

    public LOrExp getlOrExp() {
        return lOrExp;
    }

    public void writeNode() {
        lAndExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.LOrExp));
        if(lOrExp != null) {
            MyFileWriter.write(or.getWholeToken());
            lOrExp.writeNode();
        }
    }

    public static LOrExp makeLOrExp() {
        LAndExp lAndExp1 = LAndExp.makeLAndExp();
        Token sign = null;
        LOrExp lOrExp1 = null;

        if(Objects.equals(Parser.currentToken.getCategory(), "OR")) {
            sign = Parser.checkCategory("OR");
            lOrExp1 = makeLOrExp();
        }
        return new LOrExp(lAndExp1, sign, lOrExp1);
    }
}
