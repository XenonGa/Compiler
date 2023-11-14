package LLVM_IR;

import LLVM_IR.Instruction.*;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeFunction;
import LLVM_IR.LLVMType.TypeInt;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.Structure.Function;
import LLVM_IR.Structure.GlobalVariable;
import LLVM_IR.Structure.Value;
import Node.CompUnit;

import java.util.ArrayList;
import java.util.HashMap;

public class Builder {
    public static LinkList<Function, Builder> functionList;
    public static HashMap<Integer, Instruction>instructions;
    public static ArrayList<GlobalVariable> globalVariableArrayList;

    public Builder() {
        functionList = new LinkList<>(this);
        instructions = new HashMap<>();
        globalVariableArrayList = new ArrayList<>();
    }

    public void BuildLLVM(CompUnit compUnit) {
        CompUnit.compUnitLLVMBuilder(compUnit);
    }

    public String writeLLVM() {
        StringBuilder sb = new StringBuilder();

        // global var
        for(GlobalVariable gv : globalVariableArrayList) {
            sb.append(gv.toString());
            sb.append("\n");
        }
        if(!globalVariableArrayList.isEmpty()) sb.append("\n");

        resetBlockAndInstRegisters();

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
                    sb.append(":\n");
                    sb.append(basicBlockNode.getNodeValue().writeBasicBlock());

                    basicBlockNode = basicBlockNode.getRight();
                }
                sb.append("}\n\n");
            }

            func = func.getRight();
        }

        return sb.toString();
    }

    private void resetBlockAndInstRegisters() {
        LinkListNode<Function, Builder> func = functionList.getFirstNode();
        while (func != null) {
            Value.registerIdNum = 0;
            func.getNodeValue().resetParamsRegisters();
            if(!func.getNodeValue().isLib()) {
                LinkListNode<BasicBlock, Function> basicBlockNode = func.getNodeValue().getBasicBlockList().getFirstNode();
                while (basicBlockNode != null) {
                    BasicBlock basicBlock = basicBlockNode.getNodeValue();
                    if(basicBlockNode.getNodeValue().getInstList().listIsEmpty()) {
                        TypeFunction funcType = (TypeFunction) basicBlock.getParentFunc().getParentList().getValue().getType();
                        Type functionReturnType = funcType.getReturnType();
                        if(!basicBlock.getInstList().listIsEmpty()) {
                            Value instruction =basicBlock.getInstList().getLastNode().getNodeValue();
                            if(instruction instanceof Instruction_Br || instruction instanceof Instruction_Ret) return;
                            if(functionReturnType instanceof TypeInt) Instruction_Ret.makeReturnInst(basicBlock, BuilderAttribute.zero);
                            else Instruction_Ret.makeReturnInst(basicBlock);
                        }
                    }
                    basicBlock.setValueName(String.valueOf(Value.registerIdNum));
                    Value.registerIdNum += 1;
                    basicBlock.resetInstructionRegisters();

                    basicBlockNode = basicBlockNode.getRight();
                }
            }

            func = func.getRight();
        }

    }
}
