package LLVM_IR.Instruction;

import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeArray;
import LLVM_IR.LLVMType.TypePointer;

public class Instruction_Alloca extends Instruction {
    private Type targetType;

    public Instruction_Alloca(Type targetType) {
        super(new TypePointer(targetType), "Alloca");
        this.targetType = targetType;
        this.setValueName("%" + registerIdNum);
        registerIdNum += 1;
        // ARRAY allocation
        if(targetType instanceof TypeArray) {
            if(((TypeArray)targetType).getArrayLength() == -1) {
                this.targetType = new TypePointer(((TypeArray)targetType).getType());
                this.setType(new TypePointer(this.targetType));
            }
        }
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
