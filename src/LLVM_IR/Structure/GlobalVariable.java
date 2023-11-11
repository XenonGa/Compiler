package LLVM_IR.Structure;

import LLVM_IR.Builder;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypePointer;

public class GlobalVariable extends User {
    private Value value;
    private Boolean isConstantVariable;
    public GlobalVariable(String ident, Type paramType, Value value) {
        super("", new TypePointer(paramType));
        this.setValueName("@" + ident);
        this.value = value;
        Builder.globalVariableArrayList.add(this);
    }

    public GlobalVariable(String ident, Type paramType, Value value, Boolean isConstantVariable) {
        super("", new TypePointer(paramType));
        this.setValueName("@" + ident);
        this.value = value;
        this.isConstantVariable = isConstantVariable;
        Builder.globalVariableArrayList.add(this);
    }

    public Value getGlobalValue() {
        return value;
    }

    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(this.getValueName());
        str.append(" = ");
        str.append("dso_local ");
        if(this.isConstantVariable) {
            str.append("constant ");
        }
        else {
            str.append("global ");
        }
        if(value != null) {
            str.append(value.toString());
        }
        return str.toString();
    }
}
