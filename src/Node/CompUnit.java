package Node;

import ErrorHandler.ErrorHandler;
import FileProcess.MyFileWriter;
import LLVM_IR.Builder;
import LLVM_IR.LLVMType.*;
import LLVM_IR.Structure.Function;
import LLVM_IR.SymbolTable;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
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

    public static void compUnitErrorHandler(CompUnit compUnit) {
        ErrorHandler.pushSymbolTable(false, null);
        for(Decl decl : compUnit.declArrayList) {
            Decl.DeclErrorHandler(decl);
        }
        for(FuncDef funcDef : compUnit.funcDefArrayList) {
            FuncDef.funcDefErrorHandler(funcDef);
        }
        MainFuncDef.mainFuncDefErrorHandler(compUnit.mainFuncDef);
    }

    public static void compUnitBuildLLVM(CompUnit compUnit) {
        SymbolTable.pushLLVMSymbolTable();
        ArrayList<Type> params1 = new ArrayList<>();
        SymbolTable.addValSymbol("getint", Function.createLibFunction("getint", Builder.i32, params1));
        ArrayList<Type> params2 = new ArrayList<>();
        params2.add(Builder.i32);
        SymbolTable.addValSymbol("putint", Function.createLibFunction("putint", Builder.typeVoid, params2));
        ArrayList<Type> params3 = new ArrayList<>();
        params3.add(Builder.i32);
        SymbolTable.addValSymbol("putch", Function.createLibFunction("putch", Builder.typeVoid, params3));
        ArrayList<Type> params4 = new ArrayList<>();
        Type pointerType = new TypePointer(Builder.i8);
        params4.add(pointerType);
        SymbolTable.addValSymbol("putstr", Function.createLibFunction("putstr", Builder.typeVoid, params4));
    }
}
