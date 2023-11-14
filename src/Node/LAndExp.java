package Node;

import FileProcess.MyFileWriter;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_Br;
import LLVM_IR.Structure.BasicBlock;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import javax.swing.plaf.basic.BasicButtonUI;
import java.util.Objects;

// 逻辑与表达式 LAndExp → EqExp | LAndExp '&&' EqExp
public class LAndExp extends Node {
    private EqExp eqExp;
    private Token and;
    private LAndExp lAndExp;

    public LAndExp(EqExp eqExp) {
        this.eqExp = eqExp;
    }

    public LAndExp(EqExp eqExp, Token and, LAndExp lAndExp) {
        this.eqExp = eqExp;
        this.and = and;
        this.lAndExp = lAndExp;
    }

    public EqExp getEqExp() {
        return eqExp;
    }

    public LAndExp getlAndExp() {
        return lAndExp;
    }

    public void writeNode() {
        eqExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.LAndExp));
        if(lAndExp != null) {
            MyFileWriter.write(and.getWholeToken());
            lAndExp.writeNode();
        }
    }

    public static LAndExp makeLAndExp() {
        EqExp eqExp1 = EqExp.makeEqExp();
        Token sign = null;
        LAndExp lAndExp1 = null;

        if(checkCurrentTokenCategory("AND")) {
            sign = Parser.checkCategory("AND");
            lAndExp1 = makeLAndExp();
        }
        return new LAndExp(eqExp1, sign, lAndExp1);
    }

    public static void lAndExpErrorHandler(LAndExp lAndExp) {
        EqExp.eqExpErrorHandler(lAndExp.eqExp);
        if(lAndExp.lAndExp != null) {
            lAndExpErrorHandler(lAndExp.lAndExp);
        }
    }

    // TODO LAndExp -> EqExp | EqExp '&&' LAndExp
    public static void lAndExpLLVMBuilder(LAndExp lAndExp) {
        BasicBlock ifBlock = BuilderAttribute.currentIfBlock;
        BasicBlock elseBlock = BuilderAttribute.currentElseBlock;
        BasicBlock nextBlock = null;
        BasicBlock tempBlock = ifBlock;
        if(lAndExp.lAndExp != null) {
            nextBlock = new BasicBlock(BuilderAttribute.curentFunction);
            tempBlock = nextBlock;
        }
        BuilderAttribute.currentIfBlock = tempBlock;
        BuilderAttribute.curTempValue = null;
        EqExp.eqExpLLVMBuilder(lAndExp.eqExp);
        Instruction_Br br = new Instruction_Br(BuilderAttribute.currentBlock, BuilderAttribute.curTempValue,
                BuilderAttribute.currentIfBlock, BuilderAttribute.currentElseBlock);
        br.addInstructionInBlock(BuilderAttribute.currentBlock);
        BuilderAttribute.currentIfBlock = ifBlock;
        BuilderAttribute.currentElseBlock = elseBlock;
        if(lAndExp.lAndExp != null) {
            BuilderAttribute.currentBlock = nextBlock;
            lAndExpLLVMBuilder(lAndExp.lAndExp);
        }
    }
}
