package LLVM_IR.Structure;

import LLVM_IR.LLVMType.Type;

import java.util.ArrayList;

public class Value {
    private String valueName;
    private Type type;
    public static int registerIdNum = 0;
    public static int UID = 1;
    private String id;
    private ArrayList<Use> uses;

    public Value(String valueName, Type type) {
        this.valueName = valueName;
        this.type = type;
        this.id = "unique" + UID++;
        this.uses = new ArrayList<>();
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String toString() {
        return this.type.toString() + " " + this.valueName;
    }

    public void addUse(Use use) {
        this.uses.add(use);
    }
    public void replaceUse(Value value) {
        ArrayList<Use> usesList = new ArrayList<>(this.uses);
        for(Use use : usesList) {
            use.getUser().setOp(use.getIndex(), value);
        }
        this.uses.clear();
    }

    public void removeUse(User user) {
        ArrayList<Use> usesList = new ArrayList<>(this.uses);
        for(Use use : this.uses) {
            if(use.getUser().equals(user)) {
                usesList.remove(use);
            }
        }
        this.uses = usesList;
    }
}
