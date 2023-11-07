package Node;

import ErrorHandler.ErrorHandler;
import FileProcess.MyFileWriter;
import Identifier.*;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_Br;
import LLVM_IR.Instruction.Instruction_Ret;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.SymbolTable;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;
import LLVM_IR.Structure.Function;
import LLVM_IR.LLVMType.*;
import LLVM_IR.Structure.*;

import java.util.ArrayList;

// TODO MainFuncDef → 'int' 'main' '(' ')' Block
public class MainFuncDef extends Node{
    private Token intTK;
    private Token mainTK;
    private Token leftParent;
    private Token rightParent;
    private Block block;

    public MainFuncDef(Token intTK, Token mainTK, Token leftParent, Token rightParent, Block block) {
        this.intTK = intTK;
        this.mainTK = mainTK;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public void writeNode() {
        MyFileWriter.write(intTK.getWholeToken());
        MyFileWriter.write(mainTK.getWholeToken());
        MyFileWriter.write(leftParent.getWholeToken());
        MyFileWriter.write(rightParent.getWholeToken());
        block.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.MainFuncDef));
    }

    public static MainFuncDef makeMainFuncDef() {
        Token intTK1 = Parser.checkCategory("INTTK");
        Token mainTK1 = Parser.checkCategory("MAINTK");
        Token leftParent1 = Parser.checkCategory("LPARENT");
        Token rightParent1 = Parser.checkCategory("RPARENT");
        Block block1 = Block.makeBlock();
        return new MainFuncDef(intTK1, mainTK1, leftParent1, rightParent1, block1);
    }

    public static void mainFuncDefErrorHandler(MainFuncDef mainFuncDef) {
        String name = "main";
        String type = "int";
        Identifier ident = new FuncIdent(name, type, new ArrayList<>());
        ErrorHandler.addInSymbolTable(name, ident);
        ErrorHandler.pushSymbolTable(true, type);
        Block.blockErrorHandler(mainFuncDef.block);
        ErrorHandler.popSymbolTable();
    }

    public static void mainFuncDefLLVMBuilder(MainFuncDef mainFuncDef) {
        ArrayList<Type> mainParamsType = new ArrayList<>();
        BuilderAttribute.curentFunction = Function.createFunction("main", BuilderAttribute.i32, mainParamsType);
        BuilderAttribute.isAtGlobal = false;

        SymbolTable.addValSymbol("main", BuilderAttribute.curentFunction);
        SymbolTable.pushLLVMSymbolTable();
        SymbolTable.addValSymbol("main", BuilderAttribute.curentFunction);

        BuilderAttribute.currentBlock = new BasicBlock(BuilderAttribute.curentFunction);
        // TODO funcArgs
        Block.blockLLVMBuilder(mainFuncDef.block);
        SymbolTable.popLLVMSymbolTable();
        BuilderAttribute.isAtGlobal = true;

        TypeFunction funcType = (TypeFunction) BuilderAttribute.currentBlock.getParentFunc().getParentList().getValue().getType();
        Type functionReturnType = funcType.getReturnType();
        if(!BuilderAttribute.currentBlock.getInstList().listIsEmpty()) {
            Value instruction =BuilderAttribute.currentBlock.getInstList().getLastNode().getNodeValue();
            if(instruction instanceof Instruction_Br || instruction instanceof Instruction_Ret) return;
            if(functionReturnType instanceof TypeInt) Instruction_Ret.makeReturnInst(BuilderAttribute.currentBlock, BuilderAttribute.zero);
            else Instruction_Ret.makeReturnInst(BuilderAttribute.currentBlock);
        }
    }
}
