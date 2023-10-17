package LLVM_IR.Instruction;

import LLVM_IR.Structure.Value;

public class InstructionLinkList {
    private InstructionLinkNode firstNode;
    private InstructionLinkNode lastNode;
    private Value value;
    private int length;
    public InstructionLinkList(Value value) {
        this.value = value;
        this.length = 0;
    }

    public InstructionLinkNode getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(InstructionLinkNode firstNode) {
        this.firstNode = firstNode;
    }

    public InstructionLinkNode getLastNode() {
        return lastNode;
    }

    public void setLastNode(InstructionLinkNode lastNode) {
        this.lastNode = lastNode;
    }

    public Value getValue() {
        return value;
    }

    public int getLength() {
        return length;
    }

    public void listAddNode() {
        this.length += 1;
    }

    public void listRemoveNode() {
        this.length -= 1;
    }

    public Boolean listIsEmpty() {
        if(this.firstNode == null && this.length == 0) {
            return true;
        }
        return false;
    }


}
