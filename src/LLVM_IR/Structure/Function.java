package LLVM_IR.Structure;

import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeArray;
import LLVM_IR.LLVMType.TypeFunction;

import java.util.ArrayList;

public class Function extends Value {
    private boolean isLib;
    public Function(String name, Type type, boolean isLib) {
        super(name, type);
        this.isLib = isLib;
    }

    public static Function createFunction(String name, Type returnType, ArrayList<Type> paramsType) {
        Type typeFunction = new TypeFunction(returnType, paramsType);
        return new Function(name, typeFunction, false);
    }

    public static Function createLibFunction(String name, Type returnType, ArrayList<Type> paramsType) {
        Type typeFunction = new TypeFunction(returnType, paramsType);
        return new Function(name, typeFunction, true);
    }
}
