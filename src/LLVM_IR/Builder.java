package LLVM_IR;

import LLVM_IR.Instruction.Instruction;
import LLVM_IR.Instruction.LinkList;
import LLVM_IR.Instruction.LinkListNode;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.Structure.Function;
import Node.CompUnit;

import java.util.HashMap;

public class Builder {

    // TODO global var
    public static LinkList<Function, Builder> functionList;
    public static HashMap<Integer, Instruction>instructions;

    public Builder() {
        // TODO global var
        functionList = new LinkList<>(this);
        instructions = new HashMap<>();
    }

    public void BuildLLVM(CompUnit compUnit) {
        CompUnit.compUnitLLVMBuilder(compUnit);
    }

    public String writeLLVM() {
        StringBuilder sb = new StringBuilder();
        // TODO global var
        LinkListNode<Function, Builder> func = functionList.getFirstNode();
        while (func != null) {
            if(func.getNodeValue().isLib()) {
                sb.append("declare ");
                sb.append(func.getNodeValue().writeFunction());
                sb.append("\n\n");
            }
            else {
                sb.append("define dso_local ");
                sb.append(func.getNodeValue().writeFunction());
                sb.append("{\n");

                LinkListNode<BasicBlock, Function> basicBlockNode = func.getNodeValue().getBasicBlockList().getFirstNode();
                while (basicBlockNode != null) {
                    if(basicBlockNode != func.getNodeValue().getBasicBlockList().getFirstNode()) {
                        sb.append("\n");
                    }
                    sb.append(";<label>:");
                    sb.append(basicBlockNode.getNodeValue().getValueName());
                    sb.append("\n");
                    sb.append(basicBlockNode.getNodeValue().writeBasicBlock());

                    basicBlockNode = basicBlockNode.getRight();
                }
                sb.append("}\n\n");
            }

            func = func.getRight();
        }

        return sb.toString();
    }
}
