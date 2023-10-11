package LLVM_IR.Structure;

import LLVM_IR.LLVMType.Type;

public class Value {
    private String value;
    private Type type;
    private String id;
    public static int register = 0;
    public Value(String value, Type type, String id) {
        this.value = value;
        this.type = type;
        this.id = id;
    }

    public Value(String value, Type type) {
        this.value = value;
        this.type = type;
    }
}
