package LLVM_IR.Structure;

import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction;
import LLVM_IR.Instruction.LinkList;
import LLVM_IR.Instruction.LinkListNode;
import LLVM_IR.LLVMType.TypeIdent;

import java.util.ArrayList;

public class BasicBlock extends Value {
    private ArrayList<BasicBlock> frontBlocks;
    private ArrayList<BasicBlock> backBlocks;
//    private Instruction
    private LinkList<Instruction, BasicBlock> instList;
    private LinkListNode<BasicBlock, Function> parentFunc;

    public BasicBlock(Function function) {
        super("" + BuilderAttribute.register, new TypeIdent());
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