package LLVM_IR.Instruction;

import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypePointer;

public class Instruction_Alloca extends Instruction {
    private Type targetType;

    public Instruction_Alloca(Type targetType) {
        super(new TypePointer(targetType), "Alloca");
        this.targetType = targetType;
        this.setValueName("%" + registerIdNum);
        registerIdNum += 1;
        // TODO ARRAY allocation
    }

    public Type getTargetType() {
        return targetType;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getValueName());
        sb.append(" = ");
        sb.append("alloca ");
        sb.append(this.targetType);
        return sb.toString();
    }
}
