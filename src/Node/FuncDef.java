package Node;

import ErrorHandler.*;
import FileProcess.MyFileWriter;
import Identifier.FuncIdent;
import Identifier.FuncParam;
import Identifier.Identifier;
import LLVM_IR.Builder;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_Br;
import LLVM_IR.Instruction.Instruction_Ret;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeFunction;
import LLVM_IR.LLVMType.TypeInt;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.Structure.Function;
import LLVM_IR.Structure.Value;
import LLVM_IR.SymbolTable;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
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
        if(!checkCurrentTokenCategory("RPARENT")) {
            funcFParams1 = FuncFParams.makeFuncFParams();
        }
        Token rightParent1 = Parser.checkCategory("RPARENT");
        Block block1 = Block.makeBlock();
        return new FuncDef(funcType1, identifier, leftParent1, funcFParams1, rightParent1, block1);
    }

    public static void funcDefErrorHandler(FuncDef funcDef) {
        if(ErrorHandler.isIdentConflicted(funcDef.ident.getToken())) {
            MyError error = new MyError("b", funcDef.ident.getLineNumber());
            ErrorHandler.addNewError(error);
            return;
        }
        if(funcDef.funcFParams == null) {
            String name = funcDef.ident.getToken();
            Identifier funcIdent = new FuncIdent(name, funcDef.funcType.getTK().getToken(), new ArrayList<>());
            ErrorHandler.addInSymbolTable(name, funcIdent);
        }
        else {
            String name = funcDef.ident.getToken();
            String type = funcDef.funcType.getTK().getToken();
            ArrayList<FuncParam> paramList = new ArrayList<>();
            for(FuncFParam funcFParam : funcDef.funcFParams.getFuncFParamArrayList()) {
                String paramName = funcFParam.getIdent().getToken();
                int paramDimension = funcFParam.getLeftBracketArrayList().size();
                FuncParam param = new FuncParam(paramName, paramDimension);
                paramList.add(param);
            }
            Identifier ident = new FuncIdent(name, type, paramList);
            ErrorHandler.addInSymbolTable(name, ident);
        }
        ErrorHandler.pushSymbolTable(true, funcDef.funcType.getTK().getToken());
        if(funcDef.funcFParams != null) {
            FuncFParams.funcFParamsErrorHandler(funcDef.funcFParams);
        }
        Block.blockErrorHandler(funcDef.block);
        ErrorHandler.popSymbolTable();
    }

    // TODO FuncDef -> FuncType Ident '(' [FuncFParams] ')' Block
    public static void funcDefLLVMBuilder(FuncDef funcDef) {
        BuilderAttribute.isAtGlobal = false;

        String ident = funcDef.ident.getToken();
        String category = funcDef.funcType.getTK().getCategory();
        Type type = null;
        if(category.equals("INTTK")) {
            type = BuilderAttribute.i32;
        }
        else if(category.equals("VOIDTK")) {
            type = BuilderAttribute.typeVoid;
        }
        BuilderAttribute.paramTypeArrayList = new ArrayList<>();
        if(funcDef.funcFParams != null) {
            FuncFParams.funcFParamsLLVMBuilder(funcDef.funcFParams);
        }
        Function func = Function.createFunction(ident, type, BuilderAttribute.paramTypeArrayList);
        BuilderAttribute.curentFunction = func;
        SymbolTable.addValSymbol(ident, func);
        SymbolTable.pushLLVMSymbolTable();
        SymbolTable.addValSymbol(ident, func);

        BuilderAttribute.currentBlock = new BasicBlock(BuilderAttribute.curentFunction);
        BuilderAttribute.funcParamArrayList = BuilderAttribute.curentFunction.getFunctionParameterArrayList();
        BuilderAttribute.isCreatingFunction = true;
        if(funcDef.funcFParams != null) {
            FuncFParams.funcFParamsLLVMBuilder(funcDef.funcFParams);
        }
        BuilderAttribute.isCreatingFunction = false;
        Block.blockLLVMBuilder(funcDef.block);
        BuilderAttribute.isAtGlobal = true;
        SymbolTable.popLLVMSymbolTable();

        Builder.handleBlockEnd(BuilderAttribute.currentBlock);
    }
}
