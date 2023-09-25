package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

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

    public static UnaryExp makeUnaryExp() {
        if(Objects.equals(Parser.currentToken.getCategory(), "IDENFR") && Objects.equals(Parser.tokenArrayList.get(Parser.index + 1).getCategory(), "LPARENT")) {
            Token identifier = Parser.checkCategory("IDENFR");
            Token leftParent = Parser.checkCategory("LPARENT");
            FuncRParams funcRParams1 = null;
            if(!Objects.equals(Parser.currentToken.getCategory(), "RPARENT")) {
                funcRParams1 = FuncRParams.makeFuncParams();
            }
            Token rightParent = Parser.checkCategory("RPARENT");
            return new UnaryExp(identifier, leftParent, funcRParams1, rightParent);
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "PLUS") || Objects.equals(Parser.currentToken.getCategory(), "MINU") || Objects.equals(Parser.currentToken.getCategory(), "NOT")) {
            UnaryOp unaryOp1 = UnaryOp.makeUnaryOp();
            UnaryExp unaryExp1 = UnaryExp.makeUnaryExp();
            return new UnaryExp(unaryOp1, unaryExp1);
        }
        else {
            PrimaryExp primaryExp1 = PrimaryExp.makePrimaryExp();
            return new UnaryExp(primaryExp1);
        }
    }
}
