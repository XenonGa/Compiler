package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;

// 一元表达式 UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
public class UnaryExp extends Node{
    private PrimaryExp primaryExp;
    private Token ident;
    private Token leftParent;
    private FuncRParams funcRParams;
    private Token rightParent;

    private UnaryOp unaryOp;
    private UnaryExp unaryExp;

    public UnaryExp(PrimaryExp primaryExp) {
        this.primaryExp = primaryExp;
    }

    public UnaryExp(Token ident, Token leftParent, Token rightParent) {
        this.ident = ident;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
    }

    public UnaryExp(Token ident, Token leftParent, FuncRParams params, Token rightParent) {
        this.ident = ident;
        this.leftParent = leftParent;
        this.funcRParams = params;
        this.rightParent = rightParent;
    }

    public UnaryExp(UnaryOp unaryOp, UnaryExp unaryExp) {
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    public PrimaryExp getPrimaryExp() {
        return primaryExp;
    }

    public Token getIdent() {
        return ident;
    }

    public FuncRParams getFuncRParams() {
        return funcRParams;
    }

    public UnaryOp getUnaryOp() {
        return unaryOp;
    }

    public UnaryExp getUnaryExp() {
        return unaryExp;
    }

    public void writeNode() {
        if(primaryExp != null) {
            primaryExp.writeNode();
        }
        else if(ident != null) {
            MyFileWriter.write(ident.getWholeToken());
            MyFileWriter.write(leftParent.getWholeToken());
            if(funcRParams != null) {
                funcRParams.writeNode();
            }
            MyFileWriter.write(rightParent.getWholeToken());
        }
        else {
            unaryOp.writeNode();
            unaryExp.writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.UnaryExp));
    }
}
