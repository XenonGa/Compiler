package Node;

import FileProcess.MyFileWriter;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_GEP;
import LLVM_IR.Instruction.Instruction_Store;
import LLVM_IR.Structure.ConstArray;
import LLVM_IR.Structure.ConstNum;
import LLVM_IR.Structure.GlobalVariable;
import LLVM_IR.Structure.Value;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

// 变量初值 InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
public class InitVal extends Node {
    private Exp exp;
    private Token leftBrace;
    private ArrayList<InitVal> initValArrayList;
    private ArrayList<Token> commaArrayList;
    private Token rightBrace;

    public InitVal(Exp exp) {
        this.exp = exp;
    }

    public InitVal(Token leftBrace, ArrayList<InitVal> initValArrayList, ArrayList<Token> commaArrayList, Token rightBrace) {
        this.leftBrace = leftBrace;
        this.initValArrayList = initValArrayList;
        this.commaArrayList = commaArrayList;
        this.rightBrace = rightBrace;
    }

    public Exp getExp() {
        return exp;
    }

    public ArrayList<InitVal> getInitValArrayList() {
        return initValArrayList;
    }

    public void writeNode() {
        if(exp != null) exp.writeNode();
        else {
            MyFileWriter.write(leftBrace.getWholeToken());
            if(!initValArrayList.isEmpty()) {
                for(int i = 0; i < initValArrayList.size(); i++)
                {
                    initValArrayList.get(i).writeNode();
                    if(i < initValArrayList.size() - 1) MyFileWriter.write(commaArrayList.get(i).getWholeToken());
                }
            }
            MyFileWriter.write(rightBrace.getWholeToken());
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.InitVal));
    }

    public static InitVal makeInitVal() {
        ArrayList<InitVal> initVals = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();

        if(!checkCurrentTokenCategory("LBRACE")) {
            Exp exp1 = Exp.makeExp();
            return new InitVal(exp1);
        }
        else {
            Token leftBrace1 = Parser.checkCategory("LBRACE");
            if(!checkCurrentTokenCategory("RBRACE")) {
                initVals.add(InitVal.makeInitVal());
                while(checkCurrentTokenCategory("COMMA")) {
                    commas.add(Parser.checkCategory("COMMA"));
                    initVals.add(InitVal.makeInitVal());
                }
            }
            Token rightBrace1 = Parser.checkCategory("RBRACE");
            return new InitVal(leftBrace1, initVals, commas, rightBrace1);
        }
    }

    public static void initValErrorHandler(InitVal initVal) {
        if(initVal.exp != null) {
            Exp.expErrorHandler(initVal.exp);
            return;
        }
        for(InitVal initVal1 : initVal.initValArrayList) {
            initValErrorHandler(initVal1);
        }
    }

    // TODO InitVal -> Exp | '{' [ InitVal { ',' InitVal } ] '}'
    public static void initValLLVMBuilder(InitVal initVal) {
        if(initVal.exp != null && !BuilderAttribute.isCreatingArray) {
            Exp.expLLVMBuilder(initVal.exp);
        }
        else {
            // ARRAY
            if(initVal.exp != null) {
                // exp
                BuilderAttribute.curTempValue = null;
                BuilderAttribute.curSaveValue = null;
                if(BuilderAttribute.isAtGlobal) {
                    BuilderAttribute.isConstant = true;
                    Exp.expLLVMBuilder(initVal.exp);
                    BuilderAttribute.isConstant = false;
                }
                else {
                    Exp.expLLVMBuilder(initVal.exp);
                }
                BuilderAttribute.arrayDepth = 1;
                if(BuilderAttribute.isAtGlobal) {
                    BuilderAttribute.curTempValue = new ConstNum(BuilderAttribute.curSaveValue);
                    Value globalValue = ((GlobalVariable) BuilderAttribute.currentArray).getGlobalValue();
                    ((ConstArray) globalValue).setElement(BuilderAttribute.curTempValue, BuilderAttribute.arrayOffset);
                }
                else {
                    Instruction_GEP gep = new Instruction_GEP(BuilderAttribute.currentArray, BuilderAttribute.arrayOffset);
                    gep.addInstructionInBlock(BuilderAttribute.currentBlock);
                    Instruction_Store store = new Instruction_Store(BuilderAttribute.curTempValue, gep);
                    store.addInstructionInBlock(BuilderAttribute.currentBlock);
                }
                BuilderAttribute.arrayOffset += 1;
            }
            else if(!initVal.initValArrayList.isEmpty()) {
                // '{' [ InitVal { ',' InitVal } ] '}'
                int depth = 0;
                int offset = BuilderAttribute.arrayOffset;
                for(InitVal iv : initVal.initValArrayList) {
                    initValLLVMBuilder(iv);
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
