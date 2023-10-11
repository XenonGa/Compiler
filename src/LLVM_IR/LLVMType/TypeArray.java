package LLVM_IR.LLVMType;

import java.util.ArrayList;

import LLVM_IR.Structure.ConstNum;
import LLVM_IR.Structure.Value;

public class TypeArray extends Type {
    private Type type;
    private int arrayLength;

    public TypeArray(Type type) {
        this.type = type;
        this.arrayLength = 0;
    }

    public TypeArray(Type type, int arrayLength) {
        this.type = type;
        this.arrayLength = arrayLength;
    }

    public Type getType() {
        return type;
    }

    public int getArrayLength() {
        return arrayLength;
    }

    public boolean isString() {
        if(type instanceof TypeSpace space) {
            return space.getSpace() == 8;
        }
        return false;
    }

    public ArrayList<Integer> calDimensions() {
        ArrayList<Integer> dims = new ArrayList<>();
        Type type1 = this;
        for(; ; type1 = ((TypeArray) type1).type) {
            if(!(type1 instanceof TypeArray)) {
               break;
            }
            dims.add(((TypeArray) type1).arrayLength);
        }
        return dims;
    }

    public int getSpaceNum() {
        ArrayList<Integer> dims = calDimensions();
        int spaceNum = 1;
        for(int dim : dims) {
            spaceNum = dim * spaceNum;
        }
        return spaceNum;
    }

    public String toString() {
        return "[" + this.arrayLength +"x" + this.type.toString() + "]";
    }

    public ArrayList<Value> turnOffsetToIndex(int offset) {
        ArrayList<Value> result = new ArrayList<>();
        Type type1 = this;
        for(; ; type1 = ((TypeArray) type1).type) {
            if(!(type1 instanceof TypeArray)) {
                break;
            }
            int num = offset / ((TypeArray) type1).getSpaceNum();
            Value index = new ConstNum(num);
            result.add(index);
            offset = offset % ((TypeArray) type1).getSpaceNum();
        }
        Value index = new ConstNum(offset);
        result.add(index);
        return result;
    }
}
