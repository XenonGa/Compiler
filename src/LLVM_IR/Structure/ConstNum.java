package LLVM_IR.Structure;

import LLVM_IR.BuilderAttribute;

public class ConstNum extends Value {
    private int num;

    public ConstNum() {
        super("", BuilderAttribute.i32);
        this.num = 0;
    }

    public ConstNum(int num) {
        super(String.valueOf(num), BuilderAttribute.i32);
        this.num = num;
    }

    public ConstNum(int num, int iOne) {
        super(String.valueOf(num), BuilderAttribute.i1);
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }

    public String toString() {
        return "i32 " + this.num;
    }
}
