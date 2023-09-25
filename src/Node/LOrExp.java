package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;

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
        if(lOrExp == null) {
            lAndExp.writeNode();
        }
        else {
            lOrExp.writeNode();
            MyFileWriter.write(or.getWholeToken());
            lAndExp.writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.LOrExp));
    }
}
