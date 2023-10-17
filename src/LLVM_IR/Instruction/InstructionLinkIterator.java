package LLVM_IR.Instruction;

import java.util.Iterator;

public class InstructionLinkIterator implements Iterator<InstructionLinkNode> {
    InstructionLinkNode curNode;
    InstructionLinkNode nextNode;

    InstructionLinkIterator(InstructionLinkNode node) {
        curNode = new InstructionLinkNode(null);
        curNode.setRight(node);
    }

    public boolean hasNext() {
        return nextNode.getRight() != null || nextNode != null;
    }

    public InstructionLinkNode next() {
        if(nextNode != null) {
            curNode = nextNode;
        }
        else {
            curNode = curNode.getRight();
        }
        nextNode = null;
        return curNode;
    }
}
