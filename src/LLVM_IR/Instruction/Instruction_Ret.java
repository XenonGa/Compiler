package LLVM_IR.Instruction;


import LLVM_IR.BuilderAttribute;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.Structure.Value;

public class Instruction_Ret extends Instruction {
    public Instruction_Ret() {
        super(BuilderAttribute.typeVoid, "ret");
    }

    public Instruction_Ret(Value value) {
        super(value.getType(), "ret");
        this.addOp(value);
    }

    public static Instruction_Ret makeReturnInst(BasicBlock block, Value value) {
        Instruction_Ret ret = new Instruction_Ret(value);
        ret.addInstructionInBlock(block);
        return ret;
    }

    public static Instruction_Ret makeReturnInst(BasicBlock block) {
        Instruction_Ret ret = new Instruction_Ret();
        ret.addInstructionInBlock(block);
        return ret;
    }

    public String toString() {
        if(getOpList().size() == 1) {
            return "ret" + " " + getOpList().get(0).getType() + " " + getOpList().get(0).getValueName();
        }
        else {
            return " ret void";
        }
    }
}
