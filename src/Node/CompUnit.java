package Node;

import ErrorHandler.ErrorHandler;
import FileProcess.MyFileWriter;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.LLVMType.*;
import LLVM_IR.Structure.Function;
import LLVM_IR.SymbolTable;
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

    public static void compUnitLLVMBuilder(CompUnit compUnit) {
        SymbolTable.pushLLVMSymbolTable();

        ArrayList<Type> params1 = new ArrayList<>();
        Function getintFunction = Function.createLibFunction("getint", BuilderAttribute.i32, params1);
        SymbolTable.addValSymbol("getint", getintFunction);

        ArrayList<Type> params2 = new ArrayList<>();
        params2.add(BuilderAttribute.i32);
        Function putintFunction = Function.createLibFunction("putint", BuilderAttribute.typeVoid, params2);
        SymbolTable.addValSymbol("putint", putintFunction);

        ArrayList<Type> params3 = new ArrayList<>();
        params3.add(BuilderAttribute.i32);
        Function putchFunction = Function.createLibFunction("putch", BuilderAttribute.typeVoid, params3);
        SymbolTable.addValSymbol("putch", putchFunction);

        ArrayList<Type> params4 = new ArrayList<>();
        Type pointerType = new TypePointer(BuilderAttribute.i8);
        params4.add(pointerType);
        Function putstrFunction = Function.createLibFunction("putstr", BuilderAttribute.typeVoid, params4);
        SymbolTable.addValSymbol("putstr", putstrFunction);

        for(Decl decl : compUnit.declArrayList) {
            Decl.declLLVMBuilder(decl);
        }

        for(FuncDef funcDef : compUnit.funcDefArrayList) {
            FuncDef.funcDefLLVMBuilder(funcDef);
        }

        MainFuncDef.mainFuncDefLLVMBuilder(compUnit.mainFuncDef);
    }
}
