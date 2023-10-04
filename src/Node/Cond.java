package Node;

import FileProcess.MyFileWriter;
import Parse.NodeTypeMap;

// 条件表达式 Cond → LOrExp
public class Cond extends Node {
    private LOrExp lOrExp;

    public Cond(LOrExp lorExp) {
        this.lOrExp = lorExp;
    }

    public LOrExp getLOrExp() {
        return lOrExp;
    }

    public void writeNode() {
        lOrExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.Cond));
    }

    public static Cond makeCond() {
        LOrExp lOrExp1 = LOrExp.makeLOrExp();
        return new Cond(lOrExp1);
    }

    public static void condErrorHandler(Cond cond) {
        LOrExp.lOrExpErrorHandler(cond.lOrExp);
    }
}
