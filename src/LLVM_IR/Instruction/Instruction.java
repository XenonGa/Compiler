package LLVM_IR.Instruction;

import LLVM_IR.LLVMType.Type;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.Structure.User;

public class Instruction extends User {
    private String operator;
    private InstructionLinkNode node;
    private int index;
    private static int globalIndex = 0;

    public Instruction(Type type, String operator) {
        super("", type);
        this.operator = operator;
        this.node = new InstructionLinkNode(this);
        this.index = globalIndex;
        globalIndex += 1;
    }

    public String getOperator() {
        return operator;
    }

    public InstructionLinkNode getNode() {
        return node;
    }

    public void addInstruction(BasicBlock block) {

    }
}
