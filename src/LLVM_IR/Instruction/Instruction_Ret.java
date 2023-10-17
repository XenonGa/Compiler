package LLVM_IR.Instruction;


import LLVM_IR.BuilderAttribute;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.Structure.Value;

public class Instruction_Ret extends Instruction {
    public Instruction_Ret() {
        super(BuilderAttribute.typeVoid, "ret");
    }

    public Instruction_Ret(Value value) {
        super(value.getType(), "ret");
        this.addOp(value);
    }
}
