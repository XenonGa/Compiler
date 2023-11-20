package LLVM_IR.Instruction;

import LLVM_IR.BuilderAttribute;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.Structure.ConstNum;
import LLVM_IR.Structure.Value;

public class Instruction_Zext extends Instruction {
    public Instruction_Zext(String operator, Value value) {
        super(BuilderAttribute.i32, operator);
        this.setValueName("%" + registerIdNum);
        registerIdNum += 1;
        addOp(value);
    }

    public static Value makeInstructionZext(Value value, BasicBlock block) {
           if (value instanceof ConstNum) {
               return new ConstNum(((ConstNum) value).getNum());
           }
           Instruction_Zext zext = new Instruction_Zext("Zext", value);
           zext.addInstructionInBlock(block);
           return zext;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getValueName());
        sb.append(" = ");
        sb.append("zext i1 ");
        sb.append(getOpList().get(0).getValueName());
        sb.append(" to i32");
        return sb.toString();
    }
}
