package Node;

import FileProcess.MyFileWriter;
import Identifier.FuncParam;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

// 基本表达式 PrimaryExp → '(' Exp ')' | LVal | Number
public class PrimaryExp extends Node {
    private Token leftParent;
    private Exp exp;
    private Token rightParent;
    private LVal lVal;
    private NumberNode numberNode;

    public PrimaryExp(Token leftParent, Exp exp, Token rightParent) {
        this.leftParent = leftParent;
        this.exp = exp;
        this.rightParent = rightParent;
    }

    public PrimaryExp(LVal lval) {
        this.lVal = lval;
    }

    public PrimaryExp(NumberNode numberNode) {
        this.numberNode = numberNode;
    }

    public Exp getExp() {
        return exp;
    }

    public LVal getLval() {
        return lVal;
    }

    public NumberNode getNumberNode() {
        return numberNode;
    }

    public void writeNode() {
        if(exp != null) {
            MyFileWriter.write(leftParent.getWholeToken());
            exp.writeNode();
            MyFileWriter.write(rightParent.getWholeToken());
        }
        else if(lVal != null) {
            lVal.writeNode();
        }
        else {
            numberNode.writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.PrimaryExp));
    }

    public static PrimaryExp makePrimaryExp() {
        if(checkCurrentTokenCategory("LPARENT")) {
            Token leftParent1 = Parser.checkCategory("LPARENT");
            Exp exp1 = Exp.makeExp();
            Token rightParent1 = Parser.checkCategory("RPARENT");
            return new PrimaryExp(leftParent1, exp1, rightParent1);
        }
        else if(checkCurrentTokenCategory("INTCON")) {
            NumberNode numberNode1 = NumberNode.makeNumberNode();
            return new PrimaryExp(numberNode1);
        }
        else {
            LVal lVal1 = LVal.makeLVal();
            return new PrimaryExp(lVal1);
        }
    }

    public static void primaryExpErrorHandler(PrimaryExp primaryExp) {
        if(primaryExp.exp != null) {
            Exp.expErrorHandler(primaryExp.exp);
        }
        else if(primaryExp.lVal != null) {
            LVal.lValErrorHandler(primaryExp.lVal);
        }
    }

    public static FuncParam getFuncParamFromPrimaryExp(PrimaryExp primaryExp) {
        if(primaryExp.exp != null) {
            return Exp.getFuncParamFromExp(primaryExp.exp);
        }
        else if(primaryExp.lVal != null) {
            return LVal.getFuncParamFromLVal(primaryExp.lVal);
        }
        else {
            return new FuncParam(null, 0);
        }
    }
}
