package LLVM_IR.Structure;

import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.*;
import LLVM_IR.LLVMType.TypeFunction;
import LLVM_IR.LLVMType.TypeIdent;
import LLVM_IR.LLVMType.TypeVoid;
import com.sun.jdi.VoidType;

import java.util.ArrayList;

public class BasicBlock extends Value {
    private ArrayList<BasicBlock> frontBlocks;
    private ArrayList<BasicBlock> backBlocks;
//    private Instruction
    private LinkList<Instruction, BasicBlock> instList;
    private LinkListNode<BasicBlock, Function> parentFunc;

    public BasicBlock(Function function) {
        super("" + registerIdNum, new TypeIdent());
        registerIdNum += 1;
        this.frontBlocks = new ArrayList<>();
        this.backBlocks = new ArrayList<>();

        this.instList = new LinkList<>(this);
        this.parentFunc = new LinkListNode<>(this);

        this.parentFunc.insertAfterWholeList(function.getBasicBlockList());
    }

    public ArrayList<BasicBlock> getFrontBlocks() {
        return frontBlocks;
    }

    public ArrayList<BasicBlock> getBackBlocks() {
        return backBlocks;
    }

    public LinkList<Instruction, BasicBlock> getInstList() {
        return instList;
    }

    public String writeBasicBlock() {
        StringBuilder sb = new StringBuilder();
        LinkListNode<Instruction, BasicBlock> inst = this.instList.getFirstNode();
        while(inst != null) {
            sb.append("\t");
            sb.append(inst.getNodeValue().toString());
            sb.append("\n");
            inst = inst.getRight();
        }
        return sb.toString();
    }

    public void resetInstructionRegisters() {
        LinkListNode<Instruction, BasicBlock> inst = this.instList.getFirstNode();
        while(inst != null) {
            Instruction instruction = inst.getNodeValue();
            if(!(instruction instanceof Instruction_Br || instruction instanceof Instruction_Store || instruction instanceof Instruction_Ret
            || (instruction instanceof Instruction_Call &&
                    ((TypeFunction) instruction.getOpList().get(0).getType()).getReturnType() instanceof TypeVoid) )) {
                instruction.setValueName("%" + registerIdNum);
                registerIdNum += 1;
            }

            inst = inst.getRight();
        }
    }

    public LinkListNode<BasicBlock, Function> getParentFunc() {
        return parentFunc;
    }

    public void addFrontBlock(BasicBlock frontBlock) {
        this.frontBlocks.add(frontBlock);
    }

    public void addBackBlock(BasicBlock backBlock) {
        this.backBlocks.add(backBlock);
    }
}