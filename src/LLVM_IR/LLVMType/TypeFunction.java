package LLVM_IR.LLVMType;

import java.util.ArrayList;

public class TypeFunction extends Type {
    private Type returnType;
    private ArrayList<Type> paramsType;

    public TypeFunction(Type returnType, ArrayList<Type> paramsType) {
        this.returnType = returnType;
        this.paramsType = paramsType;

        ArrayList<Integer> list = new ArrayList<>();
        for(Type type : paramsType) {
            if(type instanceof TypeArray) {
                int len = ((TypeArray) type).getArrayLength();
                if(len == -1) {
                    list.add(paramsType.indexOf(type));
                }
            }
        }
        for(int num : list) {
            TypeArray array = (TypeArray) paramsType.get(num);
            TypePointer pointer = new TypePointer(array.getType());
            paramsType.set(num, pointer);
        }
    }

    public Type getReturnType() {
        return returnType;
    }

    public ArrayList<Type> getParamsType() {
        return paramsType;
    }
}
