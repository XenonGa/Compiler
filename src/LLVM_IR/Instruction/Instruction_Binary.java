package LLVM_IR.Instruction;

import LLVM_IR.BuilderAttribute;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.Structure.ConstNum;
import LLVM_IR.Structure.Value;

public class Instruction_Binary extends Instruction {
    public Instruction_Binary(String Operator, Value left, Value right) {
        super(BuilderAttribute.typeVoid, Operator);
        // TODO Zext maker

        this.addOp(left);
        this.addOp(right);

        this.setType(this.getOpList().get(0).getType());

        if(this.getOperator().equals("Lt") || this.getOperator().equals("Le") ||
                this.getOperator().equals("Ge") || this.getOperator().equals("Gt") ||
                this.getOperator().equals("Eq") || this.getOperator().equals("Ne")) {
            this.setType(BuilderAttribute.i1);
        }
        this.setValueName("%" + registerIdNum);
        registerIdNum += 1;
    }

    public static Instruction_Binary makeBinaryInst(BasicBlock block, String operator, Value left, Value right) {
        Instruction_Binary binaryInst = new Instruction_Binary(operator, left, right);
        if(operator.equals("And") || operator.equals("Or")) {
            Instruction_Binary inst = new Instruction_Binary("Ne", binaryInst, BuilderAttribute.zero);
            inst.addInstructionInBlock(block);
            return inst;
        }
        else {
            binaryInst.addInstructionInBlock(block);
            return binaryInst;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getValueName());
        sb.append(" = ");
        switch (this.getOperator()) {
            case "Add" -> {
                sb.append("add i32 ");
            }
            case "Sub" -> {
                sb.append("sub i32 ");
            }
            case "Mul" -> {
                sb.append("mul i32 ");
            }
            case "Div" -> {
                sb.append("div i32 ");
            }
            case "Mod" -> {
                sb.append("srem i32 ");
            }
            case "Shl" -> {
                sb.append("shl i32 ");
            }
            case "Shr" -> {
                sb.append("ashr i32 ");
            }
            case "And" -> {
                sb.append("and ");
                sb.append(this.getOpList().get(0).getType().toString());
                sb.append(" ");
            }
            case "Or" -> {
                sb.append("or ");
                sb.append(this.getOpList().get(0).getType().toString());
                sb.append(" ");
            }
            case "Lt" -> {
                sb.append("icmp slt ");
                sb.append(this.getOpList().get(0).getType().toString());
                sb.append(" ");
            }
            case "Le" -> {
                sb.append("icmp sle ");
                sb.append(this.getOpList().get(0).getType().toString());
                sb.append(" ");
            }
            case "Gt" -> {
                sb.append("icmp sgt ");
                sb.append(this.getOpList().get(0).getType().toString());
                sb.append(" ");
            }
            case "Ge" -> {
                sb.append("icmp sge ");
                sb.append(this.getOpList().get(0).getType().toString());
                sb.append(" ");
            }
            case "Eq" -> {
                sb.append("icmp eq ");
                sb.append(this.getOpList().get(0).getType().toString());
                sb.append(" ");
            }
            case "Ne" -> {
                sb.append("icmp ne ");
                sb.append(this.getOpList().get(0).getType().toString());
                sb.append(" ");
            }
        }
        sb.append(this.getOpList().get(0).getValueName());
        sb.append(", ");
        sb.append(this.getOpList().get(1).getValueName());
        return sb.toString();
    }
}
