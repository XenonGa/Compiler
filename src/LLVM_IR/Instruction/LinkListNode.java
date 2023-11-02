package LLVM_IR.Instruction;

public class LinkListNode<F, S> {
    private LinkListNode<F, S> left;
    private LinkListNode<F, S> right;
    private F nodeValue;
    private LinkList<F, S> parentList;

    public LinkListNode(F nodeValue) {
        this.nodeValue = nodeValue;
    }

    public LinkListNode(F nodeValue, LinkList<F, S> parentList) {
        this.nodeValue = nodeValue;
        this.parentList = parentList;
    }

    public LinkListNode<F, S> getLeft() {
        return left;
    }

    public void setLeft(LinkListNode<F, S> left) {
        this.left = left;
    }

    public LinkListNode<F, S> getRight() {
        return right;
    }

    public void setRight(LinkListNode<F, S> right) {
        this.right = right;
    }

    public F getNodeValue() {
        return this.nodeValue;
    }

    public LinkList<F, S> getParentList() {
        return parentList;
    }

    public void insertBeforeNode(LinkListNode<F, S> node) {
        this.right = node;
        this.left = node.left;
        node.left = this;
        if(this.left != null) {
            this.left.right = this;
        }
        this.parentList = node.parentList;
        this.parentList.listAddNode();
        if(this.parentList.getFirstNode() == node) {
            this.parentList.setFirstNode(this);
        }
    }

    public void insertAfterNode(LinkListNode<F, S> node) {
        this.right = node.right;
        this.left = node;
        node.right = this;
        if(this.right != null) {
            this.right.left = this;
        }
        this.parentList = node.parentList;
        this.parentList.listAddNode();
        if(this.parentList.getLastNode() == node) {
            this.parentList.setLastNode(this);
        }
    }

    public void insertBeforeWholeList(LinkList<F, S> nodeList) {
        this.parentList = nodeList;
        if(nodeList.listIsEmpty()) {
            this.parentList.setFirstNode(this);
            this.parentList.setLastNode(this);
            this.parentList.listAddNode();
            return;
        }
        insertBeforeNode(parentList.getFirstNode());
    }

    public void insertAfterWholeList(LinkList<F, S> nodeList) {
        this.parentList = nodeList;
        if(nodeList.listIsEmpty()) {
            this.parentList.setFirstNode(this);
            this.parentList.setLastNode(this);
            this.parentList.listAddNode();
            return;
        }
        insertAfterNode(parentList.getLastNode());
    }
}
