package LLVM_IR.LLVMType;

import java.util.ArrayList;

public class TypeFunction extends Type {
    private Type returnType;
    private ArrayList<Type> paramsType;

    public TypeFunction(Type returnType, ArrayList<Type> paramsType) {
        this.returnType = returnType;
        this.paramsType = paramsType;
    }

    public Type getReturnType() {
        return returnType;
    }

    public ArrayList<Type> getParamsType() {
        return paramsType;
    }

    // TODO ARRAY
}
