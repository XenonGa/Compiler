package Node;

import FileProcess.MyFileWriter;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_Binary;
import LLVM_IR.Structure.Value;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

// 关系表达式 RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp
public class RelExp extends Node {
    private AddExp addExp;
    private RelExp relExp;
    private Token sign;

    public RelExp(AddExp addExp) {
        this.addExp = addExp;
    }

    public RelExp(AddExp addExp, RelExp relExp, Token sign) {
        this.addExp = addExp;
        this.relExp = relExp;
        this.sign = sign;
    }

    public AddExp getAddExp() {
        return addExp;
    }

    public RelExp getRelExp() {
        return relExp;
    }

    public void writeNode() {
        addExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.RelExp));
        if(relExp != null) {
            MyFileWriter.write(sign.getWholeToken());
            relExp.writeNode();
        }
    }

    public static RelExp makeRelExp() {
        AddExp addExp = AddExp.makeAddExp();
        Token sign = null;
        RelExp relExp = null;

        if(checkCurrentTokenCategory("LSS")) {
            sign = Parser.checkCategory("LSS");
            relExp = makeRelExp();
        }
        else if(checkCurrentTokenCategory( "GRE")) {
            sign = Parser.checkCategory("GRE");
            relExp = makeRelExp();
        }
        else if(checkCurrentTokenCategory("LEQ")) {
            sign = Parser.checkCategory("LEQ");
            relExp = makeRelExp();
        }
        else if(checkCurrentTokenCategory("GEQ")) {
            sign = Parser.checkCategory("GEQ");
            relExp = makeRelExp();
        }
        return new RelExp(addExp, relExp, sign);
    }

    public static void relExpErrorHandler(RelExp relExp) {
        AddExp.addExpErrorHandler(relExp.addExp);
        if(relExp.relExp != null) {
            relExpErrorHandler(relExp.relExp);
        }
    }

    public static void relExpLLVMBuilder(RelExp relExp) {
        Value tempValue = BuilderAttribute.curTempValue;
        String operator = BuilderAttribute.curTempOperator;
        BuilderAttribute.curTempValue = null;

        AddExp.AddExpLLVMBuilder(relExp.addExp);
        if(tempValue != null) {
            BuilderAttribute.curTempValue = Instruction_Binary.makeBinaryInst(
                    BuilderAttribute.currentBlock, operator, tempValue, BuilderAttribute.curTempValue
            );
        }
        if(relExp.relExp != null) {
            if (relExp.sign.getCategory().equals("LSS")) {
                BuilderAttribute.curTempOperator = "Lt";
            } else if (relExp.sign.getCategory().equals("LEQ")) {
                BuilderAttribute.curTempOperator = "Le";
            } else if (relExp.sign.getCategory().equals("GRE")) {
                BuilderAttribute.curTempOperator = "Gt";
            } else if (relExp.sign.getCategory().equals("GEQ")) {
                BuilderAttribute.curTempOperator = "Ge";
            }
            relExpLLVMBuilder(relExp.relExp);
        }
    }
}
