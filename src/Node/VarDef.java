package Node;

import ErrorHandler.ErrorHandler;
import ErrorHandler.MyError;
import FileProcess.MyFileWriter;
import Identifier.Identifier;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_Alloca;
import LLVM_IR.Instruction.Instruction_Store;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeArray;
import LLVM_IR.Structure.ConstArray;
import LLVM_IR.Structure.ConstNum;
import LLVM_IR.Structure.GlobalVariable;
import LLVM_IR.SymbolTable;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;
import Identifier.*;

import java.util.ArrayList;

// 变量定义 VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
public class VarDef extends Node {
    private Token ident;
    private ArrayList<Token> leftBracketArrayList;
    private ArrayList<ConstExp> constExpArrayList;
    private ArrayList<Token> rightBracketArrayList;
    private Token assign;
    private InitVal initVal;

    public VarDef(Token ident, ArrayList<Token> leftBracketArrayList, ArrayList<ConstExp> constExpArrayList, ArrayList<Token> rightBracketArrayList) {
        this.ident = ident;
        this.leftBracketArrayList = leftBracketArrayList;
        this.constExpArrayList = constExpArrayList;
        this.rightBracketArrayList = rightBracketArrayList;
    }

    public VarDef(Token ident, ArrayList<Token> leftBracketArrayList, ArrayList<ConstExp> constExpArrayList, ArrayList<Token> rightBracketArrayList, Token assign, InitVal initVal) {
        this.ident = ident;
        this.leftBracketArrayList = leftBracketArrayList;
        this.constExpArrayList = constExpArrayList;
        this.rightBracketArrayList = rightBracketArrayList;
        this.assign = assign;
        this.initVal = initVal;
    }

    public Token getIdent() {
        return ident;
    }

    public ArrayList<ConstExp> getConstExpArrayList() {
        return constExpArrayList;
    }

    public InitVal getInitVal() {
        return initVal;
    }

    public void writeNode() {
        MyFileWriter.write(ident.getWholeToken());
        for (ConstExp constExp : constExpArrayList) {
            MyFileWriter.write(leftBracketArrayList.get(0).getWholeToken());
            constExp.writeNode();
            MyFileWriter.write(rightBracketArrayList.get(0).getWholeToken());
        }
        if(initVal != null) {
            MyFileWriter.write(assign.getWholeToken());
            initVal.writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.VarDef));
    }

    public static VarDef makeVarDef() {
        ArrayList<Token> leftBrackets = new ArrayList<>();
        ArrayList<ConstExp> constExps = new ArrayList<>();
        ArrayList<Token> rightBrackets = new ArrayList<>();

        Token identifier = Parser.checkCategory("IDENFR");
        while(checkCurrentTokenCategory("LBRACK")) {
            leftBrackets.add(Parser.checkCategory("LBRACK"));
            constExps.add(ConstExp.makeConstExp());
            rightBrackets.add(Parser.checkCategory("RBRACK"));
        }
        if(checkCurrentTokenCategory("ASSIGN")) {
            Token assign1 = Parser.checkCategory("ASSIGN");
            InitVal initVal1 = InitVal.makeInitVal();
            return new VarDef(identifier, leftBrackets, constExps, rightBrackets, assign1, initVal1);
        }
        return new VarDef(identifier, leftBrackets, constExps, rightBrackets);
    }

    public static void varDefErrorHandler(VarDef varDef) {
        if(ErrorHandler.isIdentConflicted(varDef.ident.getToken())) {
            MyError error = new MyError("b", varDef.ident.getLineNumber());
            ErrorHandler.addNewError(error);
            return;
        }
        if(!varDef.constExpArrayList.isEmpty()) {
            for(ConstExp constExp : varDef.constExpArrayList) {
                ConstExp.constExpErrorHandler(constExp);
            }
        }

        String name = varDef.ident.getToken();
        Identifier ident = new ValIdent(name, false, varDef.constExpArrayList.size());
        ErrorHandler.addInSymbolTable(name, ident);

        if(varDef.initVal != null) {
            InitVal.initValErrorHandler(varDef.initVal);
        }
    }

    // TODO VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
    public static void varDefLLVMBuilder(VarDef varDef) {
        String ident = varDef.ident.getToken();

        if(!varDef.constExpArrayList.isEmpty()) {
            // ARRAY
            BuilderAttribute.isConstant = true;
            ArrayList<Integer> arrayDim = new ArrayList<>();
            for(ConstExp constExp : varDef.constExpArrayList) {
                ConstExp.constExpLLVMBuilder(constExp);
                arrayDim.add(BuilderAttribute.curSaveValue);
            }
            BuilderAttribute.isConstant = false;

            BuilderAttribute.arrayDimensionList = new ArrayList<>();
            BuilderAttribute.arrayDimensionList.addAll(arrayDim);
            TypeArray arrayType = null;
            for(int i = arrayDim.size() - 1; i >= 0; i--) {
                int len = arrayDim.get(i);
                if(arrayType != null) arrayType = new TypeArray(arrayType, len);
                else arrayType = new TypeArray(BuilderAttribute.curTempType, len);
            }
            if(BuilderAttribute.isAtGlobal) {
                Type elementType = arrayType.getType();
                int len = arrayType.getArrayCapacity();
                ConstArray array = new ConstArray(arrayType, elementType, len);
                BuilderAttribute.curTempValue = new GlobalVariable(ident, arrayType, array, false);
                if(varDef.initVal != null) {
                    array.arrayIsInitialized();
                }
            }
            else {
                Instruction_Alloca allocation = new Instruction_Alloca(arrayType);
                allocation.addInstructionInBlock(BuilderAttribute.currentBlock);
                BuilderAttribute.curTempValue = allocation;
            }
            SymbolTable.addValSymbol(ident, BuilderAttribute.curTempValue);
            BuilderAttribute.currentArray = BuilderAttribute.curTempValue;
            if(varDef.initVal != null) {
                initializeArray(ident, varDef.initVal);
            }
            BuilderAttribute.isConstant = false;
        }
        else {
            BuilderAttribute.curTempValue = null;
            if(varDef.initVal != null) {
                if(BuilderAttribute.isAtGlobal) {
                    BuilderAttribute.isConstant = true;
                    BuilderAttribute.curSaveValue = null;
                }
                InitVal.initValLLVMBuilder(varDef.initVal);
                BuilderAttribute.isConstant = false;
            }
            else {
                if(BuilderAttribute.isAtGlobal) {
                    BuilderAttribute.curSaveValue = null;
                }
            }

            if(BuilderAttribute.isAtGlobal) {
                int globalInt = 0;
                if(BuilderAttribute.curSaveValue != null) {
                    globalInt = BuilderAttribute.curSaveValue;
                }
                BuilderAttribute.curTempValue = new GlobalVariable(ident, BuilderAttribute.curTempType,
                      new ConstNum(globalInt), false);
                SymbolTable.addValSymbol(ident, BuilderAttribute.curTempValue);
            }
            else {
                //  local variables
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

    public static void initializeArray(String ident, InitVal initVal) {
        BuilderAttribute.isCreatingArray = true;
        BuilderAttribute.curTempIdent = ident;
        BuilderAttribute.arrayDepth = 0;
        BuilderAttribute.arrayOffset = 0;
        InitVal.initValLLVMBuilder(initVal);
        BuilderAttribute.isCreatingArray = false;
    }
}
