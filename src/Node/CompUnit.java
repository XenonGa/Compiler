package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.TokenType;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

// 编译单元 CompUnit → {Decl} {FuncDef} MainFuncDef
public class CompUnit extends Node{
    private ArrayList<Decl> declArrayList;
    private ArrayList<FuncDef> funcDefArrayList;
    private MainFuncDef mainFuncDef;
    public CompUnit(ArrayList<Decl> declArrayList, ArrayList<FuncDef> funcDefArrayList, MainFuncDef mainFuncDef) {
        this.declArrayList = declArrayList;
        this.funcDefArrayList = funcDefArrayList;
        this.mainFuncDef = mainFuncDef;
    }

    public ArrayList<Decl> getDeclArrayList() {
        return declArrayList;
    }

    public ArrayList<FuncDef> getFuncDefArrayList() {
        return funcDefArrayList;
    }

    public MainFuncDef getMainFuncDef() {
        return mainFuncDef;
    }

    public void writeNode() {
        declArrayList.forEach(Decl::writeNode);
        funcDefArrayList.forEach(FuncDef::writeNode);
        mainFuncDef.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.CompUnit));
    }

    public static CompUnit makeCompUnit() {
        ArrayList<Decl> decls = new ArrayList<>();
        ArrayList<FuncDef> funcDefs = new ArrayList<>();
        MainFuncDef mainFuncDef1;
        while(!Objects.equals(Parser.tokenArrayList.get(Parser.index + 2).getCategory(), "LPARENT")) {
            decls.add(Decl.makeDecl());
        }
        while(!Objects.equals(Parser.tokenArrayList.get(Parser.index + 1).getCategory(), "MAINTK")) {
            funcDefs.add(FuncDef.makeFuncDef());
        }
        mainFuncDef1 = MainFuncDef.makeMainFuncDef();
        return new CompUnit(decls, funcDefs, mainFuncDef1);
    }

    public static void CompUnitErrorHandler(CompUnit compUnit) {
        ////////////////////////////////
        for(Decl decl : compUnit.declArrayList) {
            Decl.DeclErrorHandler(decl);
        }
        for(FuncDef funcDef : compUnit.funcDefArrayList) {
            FuncDef.funcDefErrorHandler(funcDef);
        }
        MainFuncDef.mainFuncDefErrorHandler(compUnit.mainFuncDef);
    }
}
