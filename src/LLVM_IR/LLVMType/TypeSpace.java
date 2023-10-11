package LLVM_IR.LLVMType;

public class TypeSpace extends Type {
    private int space;

    public TypeSpace(int space) {
        this.space = space;
    }

    public int getSpace() {
        return space;
    }

    public String toString() {
        return "i" + space;
    }
}
