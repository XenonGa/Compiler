package LLVM_IR.Instruction;

import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeFunction;
import LLVM_IR.LLVMType.TypeInt;
import LLVM_IR.LLVMType.TypeVoid;
import LLVM_IR.Structure.Function;
import LLVM_IR.Structure.Value;
import com.sun.jdi.VoidType;

import java.util.List;

public class Instruction_Call extends Instruction {
    public Instruction_Call(Function function, List<Value> parameters) {
        super(((TypeFunction) function.getType()).getReturnType(), "Call");
        Type type = ((TypeFunction) function.getType()).getReturnType();
        if (!(type instanceof TypeVoid)) {
            this.setValueName("%" + registerIdNum);
            registerIdNum += 1;
        }
        this.addOp(function);

        for(int i = 0; i < parameters.size(); i++) {
            Type paramType = parameters.get(i).getType();
            Type realType = ((TypeFunction) function.getType()).getParamsType().get(i);
            Value op = handleCurrentRealParamType(parameters.get(i), paramType, realType);
            this.addOp(op);
        }
    }

    private Value handleCurrentRealParamType(Value value, Type curParamType, Type realType) {
        Boolean isCurrentParamI1 = curParamType instanceof TypeInt && ((TypeInt) curParamType).integer == 1;
        Boolean isCurrentParamI32 = curParamType instanceof TypeInt && ((TypeInt) curParamType).integer == 32;
        Boolean isRealParamI1 = realType instanceof TypeInt && ((TypeInt) realType).integer == 1;
        Boolean isRealParamI32 = realType instanceof TypeInt && ((TypeInt) realType).integer == 32;

        if(!isCurrentParamI1 && !isCurrentParamI32 && !isRealParamI1 && !isRealParamI32) {
            return value;
        }
        else if((isCurrentParamI1 &&isRealParamI1) || (isCurrentParamI32 &&isRealParamI32)) {
            return value;
        }
        else if(isCurrentParamI1 && isRealParamI32) {
            // TODO zext
            return null;
        }
        else if(isCurrentParamI32 && isRealParamI1) {
            // TODO convTo i1
            return null;
        }
        else {
            return value;
        }
    }

    public Function getTargetFunction() {
        return (Function) this.getOpList().get(0);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Type returnType = ((TypeFunction)this.getTargetFunction().getType()).getReturnType();
        if(returnType instanceof TypeVoid) {
            sb.append("call ");
        }
        else {
            sb.append(this.getValueName());
            sb.append(" = ");
            sb.append("call ");
        }
        sb.append(returnType.toString());
        sb.append(" @");
        sb.append(this.getTargetFunction().getValueName());
        sb.append("(");
        for(int i = 1; i < this.getOpList().size(); i++) {
            sb.append(this.getOpList().get(i).getType().toString());
            sb.append(" ");
            sb.append(this.getOpList().get(i).getValueName());
            if(i != this.getOpList().size() - 1) {
                sb.append(", ");
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
