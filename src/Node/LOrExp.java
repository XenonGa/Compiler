package Node;

import FileProcess.MyFileWriter;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_Br;
import LLVM_IR.Structure.BasicBlock;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

// 逻辑或表达式 LOrExp → LAndExp | LOrExp '||' LAndExp
public class LOrExp extends Node {
    private LAndExp lAndExp;
    private Token or;
    private LOrExp lOrExp;

    public LOrExp(LAndExp lAndExp) {
        this.lAndExp = lAndExp;
    }

    public LOrExp(LAndExp lAndExp, Token or, LOrExp lOrExp) {
        this.lAndExp = lAndExp;
        this.or = or;
        this.lOrExp = lOrExp;
    }

    public LAndExp getlAndExp() {
        return lAndExp;
    }

    public LOrExp getlOrExp() {
        return lOrExp;
    }

    public void writeNode() {
        lAndExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.LOrExp));
        if(lOrExp != null) {
            MyFileWriter.write(or.getWholeToken());
            lOrExp.writeNode();
        }
    }

    public static LOrExp makeLOrExp() {
        LAndExp lAndExp1 = LAndExp.makeLAndExp();
        Token sign = null;
        LOrExp lOrExp1 = null;

        if(checkCurrentTokenCategory("OR")) {
            sign = Parser.checkCategory("OR");
            lOrExp1 = makeLOrExp();
        }
        return new LOrExp(lAndExp1, sign, lOrExp1);
    }

    public static void lOrExpErrorHandler(LOrExp lOrExp) {
        LAndExp.lAndExpErrorHandler(lOrExp.lAndExp);
        if(lOrExp.lOrExp != null) {
            lOrExpErrorHandler(lOrExp.lOrExp);
        }
    }

    // TODO LOrExp -> LAndExp | LAndExp '||' LOrExp
    public static void lOrExpLLVMBuilder(LOrExp lOrExp) {
        BasicBlock ifBlock = BuilderAttribute.currentIfBlock;
        BasicBlock elseBlock = BuilderAttribute.currentElseBlock;
        BasicBlock nextBlock = null;
        BasicBlock tempBlock = elseBlock;
        if(lOrExp.lOrExp != null) {
            nextBlock = new BasicBlock(BuilderAttribute.curentFunction);
            tempBlock = nextBlock;
        }
        BuilderAttribute.currentElseBlock = tempBlock;
        LAndExp.lAndExpLLVMBuilder(lOrExp.lAndExp);
        BuilderAttribute.currentIfBlock = ifBlock;
        BuilderAttribute.currentElseBlock = elseBlock;
        if(lOrExp.lOrExp != null) {
            BuilderAttribute.currentBlock = nextBlock;
            lOrExpLLVMBuilder(lOrExp.lOrExp);
        }
    }
}
