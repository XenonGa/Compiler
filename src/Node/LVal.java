package Node;

import ErrorHandler.*;
import FileProcess.MyFileWriter;
import Identifier.FuncParam;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_GEP;
import LLVM_IR.Instruction.Instruction_Load;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeArray;
import LLVM_IR.LLVMType.TypePointer;
import LLVM_IR.Structure.ConstNum;
import LLVM_IR.Structure.Value;
import LLVM_IR.SymbolTable;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

// 左值表达式 LVal → Ident {'[' Exp ']'}
public class LVal extends Node {
    private Token ident;
    private ArrayList<Token> leftBracketArrayList;
    private ArrayList<Exp> expArrayList;
    private ArrayList<Token> rightBracketArrayList;

    public LVal(Token ident, ArrayList<Token> leftBracketArrayList, ArrayList<Exp> expArrayList, ArrayList<Token> rightBracketArrayList) {
        this.ident = ident;
        this.leftBracketArrayList = leftBracketArrayList;
        this.expArrayList = expArrayList;
        this.rightBracketArrayList = rightBracketArrayList;
    }

    public Token getIdent() {
        return ident;
    }

    public ArrayList<Exp> getExpArrayList() {
        return expArrayList;
    }

    public void writeNode() {
        MyFileWriter.write(ident.getWholeToken());
        if(!expArrayList.isEmpty()) {
            for(int i = 0; i < expArrayList.size(); i++) {
                MyFileWriter.write(leftBracketArrayList.get(i).getWholeToken());
                expArrayList.get(i).writeNode();
                MyFileWriter.write(rightBracketArrayList.get(i).getWholeToken());
            }
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.LVal));
    }

    public static LVal makeLVal() {
        ArrayList<Token> leftBrackets = new ArrayList<>();
        ArrayList<Exp> exps = new ArrayList<>();
        ArrayList<Token> rightBrackets = new ArrayList<>();

        Token identifier = Parser.checkCategory("IDENFR");
        while(checkCurrentTokenCategory("LBRACK")) {
            leftBrackets.add(Parser.checkCategory("LBRACK"));
            exps.add(Exp.makeExp());
            rightBrackets.add(Parser.checkCategory("RBRACK"));
        }
        return new LVal(identifier, leftBrackets, exps, rightBrackets);
    }

    public static void lValErrorHandler(LVal lVal) {
        if(!ErrorHandler.isDeclared(lVal.ident.getToken())) {
            MyError error = new MyError("c", lVal.ident.getLineNumber());
            ErrorHandler.addNewError(error);
        }
        for(Exp exp : lVal.expArrayList) {
            Exp.expErrorHandler(exp);
        }
    }

    public static FuncParam getFuncParamFromLVal(LVal lVal) {
        return new FuncParam(lVal.ident.getToken(), lVal.expArrayList.size());
    }

    // TODO LVal → Ident {'[' Exp ']'}
    public static void lValLLVMBuilder(LVal lVal) {
        if(BuilderAttribute.isConstant) {
            StringBuilder sb = new StringBuilder();
            sb.append(lVal.ident.getToken());
            if(!lVal.expArrayList.isEmpty()) {
                sb.append("0;");
                for(Exp exp : lVal.expArrayList) {
                    Exp.expLLVMBuilder(exp);
                    int num = 0;
                    if(BuilderAttribute.curSaveValue != null) {
                        num = BuilderAttribute.curSaveValue;
                    }
                    ConstNum cn = new ConstNum(num);
                    sb.append(cn.getNum());
                    sb.append(";");
                }
            }
            String ident = sb.toString();
            BuilderAttribute.curSaveValue = SymbolTable.getConstSymbol(ident);
        }
        else {
            if(lVal.expArrayList.isEmpty()) {
                Value lval = SymbolTable.getValSymbol(lVal.getIdent().getToken());
                BuilderAttribute.curTempValue = lval;
                Type type = lval.getType();
                Type targetType = ((TypePointer) type).getType();
                if(targetType instanceof TypeArray) {
                    // ARRAY
                    ArrayList<Value> list = new ArrayList<>();
                    list.add(BuilderAttribute.zero);
                    list.add(BuilderAttribute.zero);
                    Instruction_GEP gep = new Instruction_GEP(BuilderAttribute.curTempValue, list);
                    gep.addInstructionInBlock(BuilderAttribute.currentBlock);
                    BuilderAttribute.curTempValue = gep;
                }
                else {
                    Instruction_Load load = new Instruction_Load(BuilderAttribute.curTempValue);
                    load.addInstructionInBlock(BuilderAttribute.currentBlock);
                    BuilderAttribute.curTempValue = load;
                }
            }
            else {
                // ARRAY
                ArrayList<Value> list = new ArrayList<>();
                for(Exp exp : lVal.expArrayList) {
                    Exp.expLLVMBuilder(exp);
                    list.add(BuilderAttribute.curTempValue);
                }
                String ident = lVal.getIdent().getToken();
                BuilderAttribute.curTempValue = SymbolTable.getValSymbol(ident);
                Type type = BuilderAttribute.curTempValue.getType();
                Type targetType = ((TypePointer) type).getType();
                if(targetType instanceof TypePointer) {
                    // func f param like a[][1]
                    Instruction_Load load = new Instruction_Load(BuilderAttribute.curTempValue);
                    load.addInstructionInBlock(BuilderAttribute.currentBlock);
                    BuilderAttribute.curTempValue = load;
                }
                else {
                    // a[1][1]
                    list.add(0, BuilderAttribute.zero);
                }
                Instruction_GEP gep = new Instruction_GEP(BuilderAttribute.curTempValue, list);
                gep.addInstructionInBlock(BuilderAttribute.currentBlock);
                TypePointer pointer =  ((TypePointer) gep.getType());
                if(pointer.getType() instanceof TypeArray) {
                    ArrayList<Value> indices = new ArrayList<>();
                    indices.add(BuilderAttribute.zero);
                    indices.add(BuilderAttribute.zero);
                    Instruction_GEP gep1 = new Instruction_GEP(gep, indices);
                    gep1.addInstructionInBlock(BuilderAttribute.currentBlock);
                    BuilderAttribute.curTempValue = gep1;
                }
                else {
                    Instruction_Load load = new Instruction_Load(gep);
                    load.addInstructionInBlock(BuilderAttribute.currentBlock);
                    BuilderAttribute.curTempValue = load;
                }
            }
        }
    }
}
