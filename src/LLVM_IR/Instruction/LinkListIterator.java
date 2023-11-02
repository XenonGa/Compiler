package LLVM_IR.Instruction;

import LLVM_IR.Instruction.LinkListNode;
import java.util.Iterator;

public class LinkListIterator<F, S> implements Iterator<LinkListNode<F, S>> {
    LinkListNode<F, S> curNode;
    LinkListNode<F, S> nextNode;

    LinkListIterator(LinkListNode<F, S> node) {
        curNode = new LinkListNode<F, S>(null);
        curNode.setRight(node);
    }

    public boolean hasNext() {
        return nextNode.getRight() != null || nextNode != null;
    }

    public LinkListNode<F, S> next() {
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
