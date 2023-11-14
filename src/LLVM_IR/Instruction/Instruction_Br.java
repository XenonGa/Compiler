package LLVM_IR.Instruction;

import LLVM_IR.BuilderAttribute;
import LLVM_IR.LLVMType.TypeInt;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.Structure.ConstNum;
import LLVM_IR.Structure.Value;

public class Instruction_Br extends Instruction{
    public Instruction_Br(BasicBlock targetBlock) {
        super(BuilderAttribute.typeVoid, "br");
        this.addOp(targetBlock);
    }

    public Instruction_Br(BasicBlock block, Value condition, BasicBlock ifBlock, BasicBlock elseBlock) {
        super(BuilderAttribute.typeVoid, "br");
        Value value = condition;
        boolean cond1 = condition.getType() instanceof TypeInt && ((TypeInt) condition.getType()).integer == 1;
        if (!cond1) {
            value = Instruction_Binary.makeBinaryInst(block, "Ne", condition, new ConstNum(0));
        }
        this.addOp(value);
        this.addOp(ifBlock);
        this.addOp(elseBlock);
    }

    public Value getCondition() {
        if(this.getOpList().size() == 3) {
            return this.getOpList().get(0);
        }
        else return null;
    }

    public Value getIfBlock() {
        if(this.getOpList().size() == 3) {
            return this.getOpList().get(1);
        }
        else return this.getOpList().get(0);
    }

    public Value getElseBlock() {
        if(this.getOpList().size() == 3) {
            return this.getOpList().get(2);
        }
        else return null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("br ");
        if(this.getOpList().size() == 1) {
            sb.append("label ");
            sb.append("%");
            sb.append(this.getOpList().get(0).getValueName());
        }
        else {
            sb.append(this.getOpList().get(0).getType());
            sb.append(" ");
            sb.append(this.getOpList().get(0).getValueName());
            sb.append(", ");
            sb.append("label ");
            sb.append("%");
            sb.append(this.getOpList().get(1).getValueName());
            sb.append(", ");
            sb.append("label ");
            sb.append("%");
            sb.append(this.getOpList().get(2).getValueName());
        }
        return sb.toString();
    }
}
