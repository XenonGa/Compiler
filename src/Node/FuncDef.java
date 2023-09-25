package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

// 函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block
public class FuncDef extends Node{
    private FuncType funcType;
    private Token ident;
    private Token leftParent;
    private FuncFParams funcFParams;
    private Token rightParent;
    private Block block;

    public FuncDef(FuncType funcType, Token ident, Token leftParent, FuncFParams funcFParams, Token rightParent, Block block) {
        this.funcType = funcType;
        this.ident = ident;
        this.leftParent = leftParent;
        this.funcFParams = funcFParams;
        this.rightParent = rightParent;
        this.block = block;
    }

    public FuncType getFuncType() {
        return funcType;
    }

    public Token getIdent() {
        return ident;
    }

    public FuncFParams getFuncFParams() {
        return funcFParams;
    }

    public Block getBlock() {
        return block;
    }

    public void writeNode() {
        funcType.writeNode();
        MyFileWriter.write(ident.getWholeToken());
        MyFileWriter.write(leftParent.getWholeToken());
        if(funcFParams != null) funcFParams.writeNode();
        MyFileWriter.write(rightParent.getWholeToken());
        block.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.FuncDef));
    }

    public static FuncDef makeFuncDef() {
        FuncType funcType1 = FuncType.makeFuncType();
        Token identifier = Parser.checkCategory("IDENFR");
        Token leftParent1 = Parser.checkCategory("LPARENT");
        FuncFParams funcFParams1 = null;
        if(!Objects.equals(Parser.currentToken.getCategory(), "RPARENT")) {
            funcFParams1 = FuncFParams.makeFuncFParams();
        }
        Token rightParent1 = Parser.checkCategory("RPARENT");
        Block block1 = Block.makeBlock();
        return new FuncDef(funcType1, identifier, leftParent1, funcFParams1, rightParent1, block1);
    }
}
