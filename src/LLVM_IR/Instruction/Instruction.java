package LLVM_IR.Instruction;

import LLVM_IR.Builder;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.Structure.User;

public class Instruction extends User {
    private String operator;
    private LinkListNode<Instruction, BasicBlock> node;
    private int index;
    private static int globalIndex = 0;

    public Instruction(Type type, String operator) {
        super("", type);
        this.operator = operator;
        this.node = new LinkListNode<Instruction, BasicBlock>(this);
        this.index = globalIndex;
        globalIndex += 1;
        Builder.instructions.put(globalIndex, this);
    }

    public String getOperator() {
        return operator;
    }

    public LinkListNode<Instruction, BasicBlock> getNode() {
        return node;
    }

    public void addInstructionInBlock(BasicBlock block) {
        if(block.getInstList().getLastNode() == null ||
                !(block.getInstList().getLastNode().getNodeValue() instanceof Instruction_Br
                        || block.getInstList().getLastNode().getNodeValue() instanceof Instruction_Ret)) {
                this.node.insertAfterWholeList(block.getInstList());
        }
        else this.removeUse();
    }
}
