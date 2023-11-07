package Node;

import ErrorHandler.ErrorHandler;
import FileProcess.MyFileWriter;
import Identifier.Identifier;
import LLVM_IR.Builder;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_Alloca;
import LLVM_IR.Instruction.Instruction_Store;
import LLVM_IR.Structure.ConstNum;
import LLVM_IR.Structure.GlobalVariable;
import LLVM_IR.SymbolTable;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;
import ErrorHandler.MyError;
import Identifier.ValIdent;

import javax.print.DocFlavor;
import java.util.ArrayList;

// 常数定义 ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal
public class ConstDef extends Node {
    private Token ident;
    private ArrayList<Token> leftBracketArrayList;
    private ArrayList<ConstExp> constExpArrayList;
    private ArrayList<Token> rightBracketArrayList;
    private Token assign;
    private ConstInitVal constInitVal;

    public ConstDef(Token ident, ArrayList<Token> leftBracketArrayList, ArrayList<ConstExp> constExpArrayList, ArrayList<Token> rightBracketArrayList, Token assign, ConstInitVal constInitVal) {
        this.ident = ident;
        this.leftBracketArrayList = leftBracketArrayList;
        this.constExpArrayList = constExpArrayList;
        this.rightBracketArrayList = rightBracketArrayList;
        this.assign = assign;
        this.constInitVal = constInitVal;
    }

    public Token getIdent() {
        return ident;
    }

    public ArrayList<ConstExp> getConstExpArrayList() {
        return constExpArrayList;
    }

    public ConstInitVal getConstInitVal() {
        return constInitVal;
    }

    public void writeNode() {
        MyFileWriter.write(ident.getWholeToken());
        for(int i = 0; i < constExpArrayList.size(); i++) {
            MyFileWriter.write(leftBracketArrayList.get(i).getWholeToken());
            constExpArrayList.get(i).writeNode();
            MyFileWriter.write(rightBracketArrayList.get(i).getWholeToken());
        }
        MyFileWriter.write(assign.getWholeToken());
        constInitVal.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.ConstDef));
    }

    public static ConstDef makeConstDef() {
        ArrayList<Token> leftBrackets = new ArrayList<>();
        ArrayList<ConstExp> constExps = new ArrayList<>();
        ArrayList<Token> rightBrackets = new ArrayList<>();

        Token identifier = Parser.checkCategory("IDENFR");
        while(checkCurrentTokenCategory("LBRACK")) {
            leftBrackets.add(Parser.checkCategory("LBRACK"));
            constExps.add(ConstExp.makeConstExp());
            rightBrackets.add(Parser.checkCategory("RBRACK"));
        }
        Token assign1 = Parser.checkCategory("ASSIGN");
        ConstInitVal constInitVal1 = ConstInitVal.makeConstInitVal();
        return new ConstDef(identifier, leftBrackets, constExps, rightBrackets, assign1, constInitVal1);
    }

    public static void constDefErrorHandler(ConstDef constDef) {
        if(ErrorHandler.isIdentConflicted(constDef.ident.getToken())) {
            MyError error = new MyError("b", constDef.ident.getLineNumber());
            ErrorHandler.addNewError(error);
            return;
        }
        if(!constDef.constExpArrayList.isEmpty()) {
            for(ConstExp constExp : constDef.constExpArrayList) {
                ConstExp.constExpErrorHandler(constExp);
            }
        }

        String name = constDef.ident.getToken();
        Identifier ident = new ValIdent(name, true, constDef.constExpArrayList.size());
        ErrorHandler.addInSymbolTable(name, ident);

        ConstInitVal.constInitValErrorHandler(constDef.constInitVal);
    }

    // TODO ConstDef -> Ident { '[' ConstExp ']' } '=' ConstInitVal
    public static void constDefLLVMBuilder(ConstDef constDef) {
        String ident = constDef.ident.getToken();

        if (!constDef.constExpArrayList.isEmpty()) {
            // TODO ARRAY
        }
        else {
            ConstInitVal.constInitValLLVMBuilder(constDef.constInitVal);
            BuilderAttribute.curTempValue = null;
            if(BuilderAttribute.curSaveValue != null) {
                BuilderAttribute.curTempValue = new ConstNum(BuilderAttribute.curSaveValue);
            }
            SymbolTable.addConstSymbol(ident, BuilderAttribute.curSaveValue);

            if(BuilderAttribute.isAtGlobal) {
                // global variables
                BuilderAttribute.curTempValue = new GlobalVariable(ident, BuilderAttribute.curTempType,
                        BuilderAttribute.curTempValue, true);
                SymbolTable.addValSymbol(ident, BuilderAttribute.curTempValue);
            }
            else {
                // TODO local variables
                Instruction_Alloca allocate = new Instruction_Alloca(BuilderAttribute.curTempType);
                allocate.addInstructionInBlock(BuilderAttribute.currentBlock);
                if(BuilderAttribute.curTempValue != null) {
                    Instruction_Store store = new Instruction_Store(BuilderAttribute.curTempValue, allocate);
                    store.addInstructionInBlock(BuilderAttribute.currentBlock);
                }
                SymbolTable.addValSymbol(ident, allocate);
                BuilderAttribute.curTempValue = allocate;
             }
        }

    }
}
