package LLVM_IR.Structure;

import LLVM_IR.LLVMType.Type;

public class FunctionParameter extends Value {
    public FunctionParameter(Type type, boolean isLib) {
        super("", type);
        if(!isLib) {
            this.setValueName("%" + registerIdNum);
            registerIdNum += 1;
        }
    }

    public String toString() {
        return this.getType().toString() + " " + this.getValueName();
    }
}
