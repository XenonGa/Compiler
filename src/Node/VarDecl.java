package Node;

import FileProcess.MyFileWriter;
import LLVM_IR.BuilderAttribute;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;

// 变量声明 VarDecl → BType VarDef { ',' VarDef } ';'
public class VarDecl extends Node {
    private BType bType;
    private ArrayList<VarDef> varDefArrayList;
    private ArrayList<Token> commaArrayList;
    private Token semicolon;

    public VarDecl(BType bType, ArrayList<VarDef> varDefArrayList, ArrayList<Token> commaArrayList, Token semicolon) {
        this.bType = bType;
        this.varDefArrayList = varDefArrayList;
        this.commaArrayList = commaArrayList;
        this.semicolon = semicolon;
    }

    public void writeNode() {
        bType.writeNode();
        varDefArrayList.get(0).writeNode();
        for(int i = 1; i < varDefArrayList.size(); i++) {
            MyFileWriter.write(commaArrayList.get(0).getWholeToken());
            varDefArrayList.get(i).writeNode();
        }
        MyFileWriter.write(semicolon.getWholeToken());
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.VarDecl));
    }

    public static VarDecl makeVarDecl() {
        ArrayList<VarDef> varDefs = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();
        Token semicolon;

        BType bType1 = BType.makeBType();
        varDefs.add(VarDef.makeVarDef());
        while(checkCurrentTokenCategory("COMMA")) {
            commas.add(Parser.checkCategory("COMMA"));
            varDefs.add(VarDef.makeVarDef());  
        }
        semicolon = Parser.checkCategory("SEMICN");
        return new VarDecl(bType1, varDefs, commas, semicolon);
    }

    public static void varDeclErrorHandler(VarDecl varDecl) {
        for(VarDef varDef : varDecl.varDefArrayList) {
            VarDef.varDefErrorHandler(varDef);
        }
    }

    public static void varDeclLLVMBuilder(VarDecl varDecl) {
        BuilderAttribute.curTempType = BuilderAttribute.i32;
        for (VarDef varDef : varDecl.varDefArrayList) {
            VarDef.varDefLLVMBuilder(varDef);
        }
    }
}
