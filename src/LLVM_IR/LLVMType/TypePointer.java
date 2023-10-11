package LLVM_IR.LLVMType;

public class TypePointer extends Type {
    private Type type;

    public TypePointer(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public String toString() {
        return type.toString() + "*";
    }

    public boolean isTargetString() {
        if(type instanceof TypeArray typeArray) {
            return typeArray.isString();
        }
        return false;
    }
}
