package LLVM_IR.Structure;

import LLVM_IR.Builder;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeSpace;

public class ConstNum extends Value {
    private int num;

    public ConstNum() {
        super("", Builder.i32);
        this.num = 0;
    }

    public ConstNum(int num) {
        super(String.valueOf(num), Builder.i32);
        this.num = num;
    }

    public ConstNum(int num, int iOne) {
        super(String.valueOf(num), Builder.i1);
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }

    public String toString() {
        return "i32 " + this.num;
    }
}
