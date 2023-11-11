package Node;

import FileProcess.MyFileWriter;
import Identifier.Identifier;
import LLVM_IR.Builder;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_Alloca;
import LLVM_IR.Instruction.Instruction_Store;
import LLVM_IR.LLVMType.TypeArray;
import LLVM_IR.Structure.Value;
import LLVM_IR.SymbolTable;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;
import ErrorHandler.*;
import Identifier.*;

import java.util.ArrayList;
import java.util.Objects;

// 函数形参 FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
public class FuncFParam extends Node{
    private BType bType;
    private Token ident;
    private ArrayList<Token> leftBracketArrayList;
    private ArrayList<Token> rightBracketArrayList;
    private ConstExp constExp;

    public FuncFParam(BType bType, Token ident, ArrayList<Token> leftBracketArrayList, ArrayList<Token> rightBracketArrayList, ConstExp constExp) {
        this.bType = bType;
        this.ident = ident;
        this.leftBracketArrayList = leftBracketArrayList;
        this.rightBracketArrayList = rightBracketArrayList;
        this.constExp = constExp;
    }

    public BType getBType() {
        return bType;
    }

    public Token getIdent() {
        return ident;
    }

    public ArrayList<Token> getLeftBracketArrayList() {
        return leftBracketArrayList;
    }

    public ConstExp getConstExp() {
        return constExp;
    }

    public void writeNode() {
        bType.writeNode();
        MyFileWriter.write(ident.getWholeToken());
        if(!leftBracketArrayList.isEmpty()) {
            MyFileWriter.write(leftBracketArrayList.get(0).getWholeToken());
            MyFileWriter.write(rightBracketArrayList.get(0).getWholeToken());
            if(constExp!= null) {
                MyFileWriter.write(leftBracketArrayList.get(1).getWholeToken());
                constExp.writeNode();
                MyFileWriter.write(rightBracketArrayList.get(1).getWholeToken());
            }
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.FuncFParam));
    }

    public static FuncFParam makeFuncFParam() {
        ArrayList<Token> leftBrackets = new ArrayList<>();
        ArrayList<Token> rightBrackets = new ArrayList<>();
        ConstExp constExp1 = null;

        BType bType1 = BType.makeBType();
        Token identifier = Parser.checkCategory("IDENFR");
        if(checkCurrentTokenCategory("LBRACK")){
            leftBrackets.add(Parser.checkCategory("LBRACK"));
            rightBrackets.add(Parser.checkCategory("RBRACK"));
            if(checkCurrentTokenCategory("LBRACK")){
                leftBrackets.add(Parser.checkCategory("LBRACK"));
                constExp1 = ConstExp.makeConstExp();
                rightBrackets.add(Parser.checkCategory("RBRACK"));
            }
        }
        return new FuncFParam(bType1, identifier, leftBrackets, rightBrackets, constExp1);

    }

    public static void funcFParamErrorHandler(FuncFParam funcFParam) {
        String name = funcFParam.ident.getToken();
        if(ErrorHandler.isIdentConflicted(name)) {
            MyError myError = new MyError("b", funcFParam.ident.getLineNumber());
            ErrorHandler.addNewError(myError);
        }
        Identifier ident = new ValIdent(name, false, funcFParam.getLeftBracketArrayList().size());
        ErrorHandler.addInSymbolTable(name, ident);
    }

    // TODO BType Ident [ '[' ']' { '[' ConstExp ']' }]
    public static void funcFParamLLVMBuilder(FuncFParam funcFParam) {
        if(BuilderAttribute.isCreatingFunction) {
            int i = BuilderAttribute.tempIndex;
            Instruction_Alloca alloc = new Instruction_Alloca(BuilderAttribute.paramTypeArrayList.get(i));
            alloc.addInstructionInBlock(BuilderAttribute.currentBlock);
            if(BuilderAttribute.funcParamArrayList.get(i) != null) {
                Instruction_Store store = new Instruction_Store(BuilderAttribute.funcParamArrayList.get(i), alloc);
                store.addInstructionInBlock(BuilderAttribute.currentBlock);
            }
            SymbolTable.addValSymbol(funcFParam.ident.getToken(), alloc);
        }
        else {
            if(funcFParam.leftBracketArrayList.isEmpty()) {
                BuilderAttribute.curTempType = BuilderAttribute.i32;
            }
            else {
                ArrayList<Integer> dimensions = new ArrayList<>();
                dimensions.add(-1);
                if(funcFParam.constExp != null) {
                    BuilderAttribute.isConstant = true;
                    ConstExp.constExpLLVMBuilder(funcFParam.constExp);
                    dimensions.add(BuilderAttribute.curSaveValue);
                    BuilderAttribute.isConstant = false;
                }
                BuilderAttribute.curTempType = null;
                for(int i = dimensions.size() - 1; i >= 0; i--) {
                    if(BuilderAttribute.curTempType == null) {
                        BuilderAttribute.curTempType = BuilderAttribute.i32;
                    }
                    BuilderAttribute.curTempType = new TypeArray(BuilderAttribute.curTempType, dimensions.get(i));
                }
            }
        }
    }
}
