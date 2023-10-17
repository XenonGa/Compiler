package LLVM_IR.Instruction;

public class InstructionLinkNode {
    private InstructionLinkNode left;
    private InstructionLinkNode right;
    private Instruction instruction;
    private InstructionLinkList parentList;

    public InstructionLinkNode(Instruction inst) {
        this.instruction = inst;
    }

    public InstructionLinkNode(Instruction instruction, InstructionLinkList parentList) {
        this.instruction = instruction;
        this.parentList = parentList;
    }

    public InstructionLinkNode getLeft() {
        return left;
    }

    public void setLeft(InstructionLinkNode left) {
        this.left = left;
    }

    public InstructionLinkNode getRight() {
        return right;
    }

    public void setRight(InstructionLinkNode right) {
        this.right = right;
    }

    public Instruction getInstruction() {
        return instruction;
    }

    public InstructionLinkList getParentList() {
        return parentList;
    }

    public void insertBeforeNode(InstructionLinkNode node) {
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

    public void insertAfterNode(InstructionLinkNode node) {
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

    public void insertBeforeWholeList(InstructionLinkList nodeList) {
        this.parentList = nodeList;
        if(nodeList.listIsEmpty()) {
            this.parentList.setFirstNode(this);
            this.parentList.setLastNode(this);
            this.parentList.listAddNode();
            return;
        }
        insertBeforeNode(parentList.getFirstNode());
    }

    public void insertAfterWholeList(InstructionLinkList nodeList) {
        this.parentList = nodeList;
        if(nodeList.listIsEmpty()) {
            this.parentList.setFirstNode(this);
            this.parentList.setLastNode(this);
            this.parentList.listAddNode();
            return;
        }
        insertAfterNode(parentList.getFirstNode());
    }
}
