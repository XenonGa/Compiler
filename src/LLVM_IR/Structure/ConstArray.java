package LLVM_IR.Structure;

import LLVM_IR.BuilderAttribute;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeArray;

import java.util.ArrayList;

public class ConstArray extends Value {
    private Type elementType;
    private int length;
    private ArrayList<Value> storedArray;
    private Boolean isInitialized = false;

    public ConstArray(Type arrayType, Type elementType, int length) {
        super("", arrayType);
        this.elementType = elementType;
        this.length = length;
        this.storedArray = new ArrayList<>();
        if(this.elementType instanceof TypeArray) {
            for(int i = 0; i < ((TypeArray) arrayType).getArrayLength(); i++) {
                Type eleType = ((TypeArray) elementType).getType();
                int len = ((TypeArray) elementType).getArrayCapacity();
                ConstArray value = new ConstArray(elementType, eleType, len);
                storedArray.add(value);
            }
        }
        else {
            for(int i = 0; i < ((TypeArray) arrayType).getArrayLength(); i++) {
                storedArray.add(BuilderAttribute.zero);
            }
        }
    }

    public void setElement(Value value, int index) {
        if(!(this.elementType instanceof TypeArray)) {
            this.storedArray.set(index, value);
        }
        else {
            int len = ((TypeArray) elementType).getArrayCapacity();
            int index_1 = index / len;
            int index_2 = index % len;
            ((ConstArray) storedArray.get(index_1)).setElement(value, index_2);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(this.isArrayStuffedByZero()) {
            sb.append(this.getType().toString());
            sb.append(" ");
            sb.append("zeroinitializer");
        }
        else {
            sb.append(this.getType().toString());
            sb.append(" [");
            for(int i = 0; i < this.storedArray.size(); i++) {
                sb.append(this.storedArray.get(i).toString());
                if(i != this.storedArray.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("]");
        }
        return sb.toString();
    }

    public boolean isArrayStuffedByZero() {
        for(int i = 0; i < this.storedArray.size(); i++) {
            if(!(this.storedArray.get(i) instanceof ConstNum)) {
                boolean flag = ((ConstArray) this.storedArray.get(i)).isArrayStuffedByZero();
                if(!flag) {
                    return false;
                }
            }
            else {
                int num = ((ConstNum) this.storedArray.get(i)).getNum();
                if(num != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public void arrayIsInitialized() {
        this.isInitialized = true;
    }

    public ArrayList<Value> unfoldArray() {
        ArrayList<Value> list = new ArrayList<>();
        for(int i = 0; i < this.storedArray.size(); i++) {
            if(!(this.storedArray.get(i) instanceof ConstArray)) {
                list.add(this.storedArray.get(i));
            }
            else {
                ArrayList<Value> array = ((ConstArray) this.storedArray.get(i)).unfoldArray();
                list.addAll(array);
            }
        }
        return list;
    }
}
