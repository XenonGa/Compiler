package LLVM_IR.LLVMType;

public class TypeInt extends Type {
    private int integer;

    public TypeInt(int integer) {
        this.integer = integer;
    }

    public int getSpace() {
        return integer;
    }

    public String toString() {
        return "i" + integer;
    }
}
