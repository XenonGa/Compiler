package Node;

import FileProcess.MyFileWriter;
import Identifier.FuncParam;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_Binary;
import LLVM_IR.Structure.Value;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

// 加减表达式 AddExp → MulExp | AddExp ('+' | '−') MulExp
public class AddExp extends Node {
    private MulExp mulExp;
    private AddExp addExp;
    private Token sign;

    public AddExp(MulExp mulExp) {
        this.mulExp = mulExp;
    }

    public AddExp(MulExp mulExp, AddExp addExp, Token sign) {
        this.mulExp = mulExp;
        this.addExp = addExp;
        this.sign = sign;
    }

    public MulExp getMulExp() {
        return mulExp;
    }

    public AddExp getAddExp() {
        return addExp;
    }

    public void writeNode() {
        mulExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.AddExp));
        if(addExp != null) {
            MyFileWriter.write(sign.getWholeToken());
            addExp.writeNode();
        }
    }

    public static AddExp makeAddExp() {
        MulExp mulExp = MulExp.makeMulExp();
        Token sign = null;
        AddExp addExp = null;
        if(checkCurrentTokenCategory("PLUS")) {
            sign = Parser.checkCategory("PLUS");
            addExp = makeAddExp();
        }
        else if(checkCurrentTokenCategory("MINU")) {
            sign = Parser.checkCategory("MINU");
            addExp = makeAddExp();
        }
        return new AddExp(mulExp, addExp, sign);
    }

    public static void addExpErrorHandler(AddExp addExp) {
        MulExp.mulExpErrorHandler(addExp.mulExp);
        if(addExp.addExp != null) {
            addExpErrorHandler(addExp.addExp);
        }
    }

    public static FuncParam getFuncParamFromAddExp(AddExp addExp) {
        return MulExp.getFuncParamFromMulExp(addExp.mulExp);
    }

    // TODO AddExp → MulExp | AddExp ('+' | '−') MulExp
    public static void AddExpLLVMBuilder(AddExp addExp) {
        if(BuilderAttribute.isConstant) {
            Integer tempValue = BuilderAttribute.curSaveValue;
            String operator = BuilderAttribute.curSaveOperator;
            BuilderAttribute.curSaveValue = null;

            MulExp.mulExpLLVMBuilder(addExp.mulExp);

            if(tempValue != null) {
                if(operator != null) {
                    if(operator.equals("Add")) {
                        BuilderAttribute.curSaveValue = tempValue + BuilderAttribute.curSaveValue;
                    }
                    else if(operator.equals("Sub")) {
                        BuilderAttribute.curSaveValue = tempValue - BuilderAttribute.curSaveValue;
                    }
                }
                else {
                    BuilderAttribute.curSaveValue = 0;
                }
            }

            if(addExp.addExp != null) {
                if(addExp.sign.getCategory().equals("PLUS")) {
                    BuilderAttribute.curSaveOperator = "Add";
                }
                else {
                    BuilderAttribute.curSaveOperator = "Sub";
                }
                AddExpLLVMBuilder(addExp.addExp);
            }

        }
        else {
            Value tempValue = BuilderAttribute.curTempValue;
            String operator = BuilderAttribute.curTempOperator;
            BuilderAttribute.curTempValue = null;

            MulExp.mulExpLLVMBuilder(addExp.mulExp);

            if(tempValue != null) {
                BuilderAttribute.curTempValue = Instruction_Binary.makeBinaryInst(
                        BuilderAttribute.currentBlock, operator, tempValue, BuilderAttribute.curTempValue
                );
            }

            if(addExp.addExp != null) {
                if(addExp.sign.getCategory().equals("PLUS")) {
                    BuilderAttribute.curTempOperator = "Add";
                }
                else {
                    BuilderAttribute.curTempOperator = "Sub";
                }
                AddExpLLVMBuilder(addExp.addExp);
            }
        }
    }
}
