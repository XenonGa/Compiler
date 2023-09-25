package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

// 语句 ForStmt → LVal '=' Exp
public class ForStmt extends Node{
    private LVal lVal;
    private Token assign;
    private Exp exp;

    public ForStmt(LVal lVal, Token assign, Exp exp) {
        this.lVal = lVal;
        this.assign = assign;
        this.exp = exp;
    }

    public LVal getlVal() {
        return lVal;
    }

    public Exp getExp() {
        return exp;
    }

    public void writeNode() {
        lVal.writeNode();
        MyFileWriter.write(assign.getWholeToken());
        exp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.ForStmt));
    }

    public static ForStmt makeForStmt() {
        LVal lVal = LVal.makeLVal();
        Token assign = Parser.checkCategory("ASSIGN");
        Exp exp = Exp.makeExp();
        return new ForStmt(lVal, assign, exp);
    }
}
