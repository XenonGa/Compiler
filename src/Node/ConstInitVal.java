package Node;

import FileProcess.MyFileWriter;
import LLVM_IR.Builder;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_GEP;
import LLVM_IR.Instruction.Instruction_Store;
import LLVM_IR.LLVMType.TypeArray;
import LLVM_IR.LLVMType.TypePointer;
import LLVM_IR.Structure.ConstArray;
import LLVM_IR.Structure.ConstNum;
import LLVM_IR.Structure.GlobalVariable;
import LLVM_IR.Structure.Value;
import LLVM_IR.SymbolTable;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.awt.event.PaintEvent;
import java.util.ArrayList;
import java.util.Objects;

// 常量初值 ConstInitVal → ConstExp
// | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
public class ConstInitVal extends Node {
    private ConstExp constExp;
    private Token leftBrace;
    private ArrayList<ConstInitVal> constInitValArrayList;
    private ArrayList<Token> commaArrayList;
    private Token rightBrace;

    public ConstInitVal(ConstExp constExp) {
        this.constExp = constExp;
    }

    public ConstInitVal(Token leftBrace, ArrayList<ConstInitVal> constInitValArrayList, ArrayList<Token> commaArrayList, Token rightBrace) {
        this.leftBrace = leftBrace;
        this.constInitValArrayList = constInitValArrayList;
        this.commaArrayList = commaArrayList;
        this.rightBrace = rightBrace;
    }

    public ConstExp getConstExp() {
        return constExp;
    }

    public ArrayList<ConstInitVal> getConstInitValArrayList() {
        return constInitValArrayList;
    }

    public void writeNode() {
        if(constExp != null) constExp.writeNode();
        else {
            MyFileWriter.write(leftBrace.getWholeToken());
            if(!constInitValArrayList.isEmpty()) {
                constInitValArrayList.get(0).writeNode();
                for(int i = 1; i < constInitValArrayList.size(); i++)
                {
                    MyFileWriter.write(commaArrayList.get(i - 1).getWholeToken());
                    constInitValArrayList.get(i).writeNode();
                }
            }
            MyFileWriter.write(rightBrace.getWholeToken());
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.ConstInitVal));
    }

    public static ConstInitVal makeConstInitVal() {
        if(checkCurrentTokenCategory( "LBRACE")) {
            ArrayList<ConstInitVal> constInitVals = new ArrayList<>();
            ArrayList<Token> commas = new ArrayList<>();

            Token leftBrace = Parser.checkCategory("LBRACE");
            if(!checkCurrentTokenCategory("RBRACE")) {
                constInitVals.add(makeConstInitVal());
                while (!checkCurrentTokenCategory("RBRACE")) {
                    commas.add(Parser.checkCategory("COMMA"));
                    constInitVals.add(makeConstInitVal());
                }
            }
            Token rightBrace = Parser.checkCategory("RBRACE");
            return new ConstInitVal(leftBrace, constInitVals, commas, rightBrace);
        }
        else {
            ConstExp constExp1 = ConstExp.makeConstExp();
            return new ConstInitVal(constExp1);
        }
    }

    public static void constInitValErrorHandler(ConstInitVal constInitVal) {
        if(constInitVal.constExp != null) {
            ConstExp.constExpErrorHandler(constInitVal.constExp);
            return;
        }
        for(ConstInitVal constInitVal1 : constInitVal.constInitValArrayList) {
            constInitValErrorHandler(constInitVal1);
        }
    }

    // TODO ConstInitVal -> ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
    public static void constInitValLLVMBuilder(ConstInitVal constInitVal) {
        if(constInitVal.constExp != null && !BuilderAttribute.isCreatingArray) {
            ConstExp.constExpLLVMBuilder(constInitVal.constExp);
        }
        else {
            // ARRAY
            if(constInitVal.constExp != null) {
                // constExp
                BuilderAttribute.curTempValue = null;
                assert constInitVal.constExp != null;
                ConstExp.constExpLLVMBuilder(constInitVal.constExp);
                BuilderAttribute.curTempValue = new ConstNum(BuilderAttribute.curSaveValue);
                BuilderAttribute.arrayDepth = 1;
                if(BuilderAttribute.isAtGlobal) {
                    Value globalValue = ((GlobalVariable) BuilderAttribute.currentArray).getGlobalValue();
                    ((ConstArray) globalValue).setElement(BuilderAttribute.curTempValue, BuilderAttribute.arrayOffset);
                }
                else {
                    Instruction_GEP gep = new Instruction_GEP(BuilderAttribute.currentArray, BuilderAttribute.arrayOffset);
                    gep.addInstructionInBlock(BuilderAttribute.currentBlock);
                    Instruction_Store store = new Instruction_Store(BuilderAttribute.curTempValue, gep);
                    store.addInstructionInBlock(BuilderAttribute.currentBlock);
                }
                // name
                StringBuilder sb = new StringBuilder();
                sb.append(BuilderAttribute.curTempIdent);
                TypePointer tp = (TypePointer) BuilderAttribute.currentArray.getType();
                ArrayList<Value> arrayArgs =((TypeArray) tp.getType()).turnOffsetToIndex(BuilderAttribute.arrayOffset);
                for(Value args : arrayArgs) {
                    sb.append(args.getValueName());
                    sb.append(";");
                }
                String ident = sb.toString();
                SymbolTable.addConstSymbol(ident, BuilderAttribute.curSaveValue);
                BuilderAttribute.arrayOffset += 1;
            }
            else if(!constInitVal.constInitValArrayList.isEmpty()) {
                // {' [ ConstInitVal { ',' ConstInitVal } ] '}'
                int depth = 0;
                int offset = BuilderAttribute.arrayOffset;
                for(ConstInitVal civ : constInitVal.constInitValArrayList) {
                    constInitValLLVMBuilder(civ);
                    if (depth <= BuilderAttribute.arrayDepth) {
                        depth = BuilderAttribute.arrayDepth;
                    }
                }
                depth = depth + 1;
                int len = 1;
                for(int i = 1; i < depth; i++) {
                    int index = BuilderAttribute.arrayDimensionList.size() - i;
                    len = len * BuilderAttribute.arrayDimensionList.get(index);
                }
                BuilderAttribute.arrayDepth = depth;
                if(BuilderAttribute.arrayOffset <= offset + len) {
                    BuilderAttribute.arrayOffset = offset + len;
                }
            }
        }
    }
}
