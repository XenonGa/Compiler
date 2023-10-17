package LLVM_IR.Structure;

import LLVM_IR.BuilderAttribute;
import LLVM_IR.LLVMType.TypeIdent;

import java.util.ArrayList;

public class BasicBlock extends Value {
    private ArrayList<BasicBlock> frontBlocks;
    private ArrayList<BasicBlock> backBlocks;

    public BasicBlock() {
        super("" + BuilderAttribute.register, new TypeIdent());
        this.frontBlocks = new ArrayList<>();
        this.backBlocks = new ArrayList<>();
    }

    public ArrayList<BasicBlock> getFrontBlocks() {
        return frontBlocks;
    }

    public ArrayList<BasicBlock> getBackBlocks() {
        return backBlocks;
    }

    public void addFrontBlock(BasicBlock frontBlock) {
        this.frontBlocks.add(frontBlock);
    }

    public void addBackBlock(BasicBlock backBlock) {
        this.backBlocks.add(backBlock);
    }
}