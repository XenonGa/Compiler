package LLVM_IR.Instruction;

import LLVM_IR.LLVMType.TypePointer;
import LLVM_IR.Structure.Value;

public class Instruction_Load extends Instruction {
    private Value target;
    public Instruction_Load(Value target) {
        super(((TypePointer)target.getType()).getType(), "Load");
        this.setValueName("%" + registerIdNum);
        registerIdNum += 1;
        this.target = target;
        // TODO ARRAY
        this.addOp(target);
    }

    public Value getTarget() {
        return this.target;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getValueName());
        sb.append(" = ");
        sb.append("load ");
        sb.append(this.getType());
        sb.append(", ");
        sb.append(this.target.getType());
        sb.append(" ");
        sb.append(this.target.getValueName());
        return sb.toString();
    }

}
