package LLVM_IR.Instruction;

import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeArray;
import LLVM_IR.LLVMType.TypePointer;
import LLVM_IR.Structure.GlobalVariable;
import LLVM_IR.Structure.Value;

import java.util.ArrayList;

public class Instruction_GEP extends Instruction {
    private Value target;

    public Instruction_GEP(Value target, ArrayList<Value> indexArraylist) {
        super(new TypePointer(getTypeInGEP(target, indexArraylist)), "GEP");
        this.setValueName("%" + registerIdNum);
        registerIdNum += 1;
        if(target instanceof Instruction_GEP) {
            this.target = ((Instruction_GEP) target).target;
        }
        else if(target instanceof Instruction_Alloca) {
            this.target = target;
        }
        else if(target instanceof GlobalVariable) {
            this.target = target;
        }
        this.addOp(target);
        for(Value v : indexArraylist) {
            this.addOp(v);
        }
    }

    public Instruction_GEP(Value target, int offset) {
        this(target, ((TypeArray) ((TypePointer) target.getType()).getType()).turnOffsetToIndex(offset));
    }

    private static Type getTypeInGEP(Value target, ArrayList<Value> indexArraylist) {
        Type type = target.getType();
        for (int i = 0; i < indexArraylist.size(); i++) {
            if(type instanceof TypePointer) {
                type = ((TypePointer) type).getType();
            }
            else if(type instanceof TypeArray) {
                // ARRAY
                type = ((TypeArray) type).getType();
            }
            else {
                break;
            }
        }
        return type;
    }

    public Value getTarget() {
        return this.getOpList().get(0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getValueName());
        sb.append(" = getelementptr ");
        if(this.getTarget().getType() instanceof TypePointer && ((TypePointer) getTarget().getType()).isTargetString()) {
            sb.append("inbounds ");
        }
        sb.append(((TypePointer) getTarget().getType()).getType().toString());
        sb.append(", ");
        for(int i = 0; i < this.getOpList().size(); i++) {
                sb.append(getOpList().get(i).getType());
                sb.append(" ");
                sb.append(getOpList().get(i).getValueName());
            if(i != this.getOpList().size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
