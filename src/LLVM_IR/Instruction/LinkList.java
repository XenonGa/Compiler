package LLVM_IR.Instruction;

import LLVM_IR.Structure.Value;

public class LinkList<F, S> {
    private LinkListNode<F, S> firstNode;
    private LinkListNode<F, S> lastNode;
    private S value;
    private int length;
    public LinkList(S value) {
        this.value = value;
        this.length = 0;
    }

    public LinkListNode<F, S> getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(LinkListNode<F, S> firstNode) {
        this.firstNode = firstNode;
    }

    public LinkListNode<F, S> getLastNode() {
        return lastNode;
    }

    public void setLastNode(LinkListNode<F, S> lastNode) {
        this.lastNode = lastNode;
    }

    public S getValue() {
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
        if (this.firstNode == null && this.length == 0) {
            return true;
        }
        return false;
    }
}
