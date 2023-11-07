package LLVM_IR.Instruction;

import LLVM_IR.Structure.Value;

public class Instruction_Store extends Instruction {
    Value value;
    Value target;

    public Instruction_Store(Value value, Value target) {
        super(value.getType(), "Store");
        this.value = value;
        this.target = target;
        this.addOp(value);
        this.addOp(target);
    }

    public Value getTarget() {
        return target;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("store ");
        sb.append(value.getType());
        sb.append(" ");
        sb.append(value.getValueName());
        sb.append(", ");
        sb.append(target.getType());
        sb.append(" ");
        sb.append(target.getValueName());
        return sb.toString();
    }
}
