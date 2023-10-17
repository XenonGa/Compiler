package LLVM_IR.Structure;

import LLVM_IR.LLVMType.Type;

import java.util.ArrayList;

public class Value {
    private String value;
    private Type type;
    private static int idNum = 0;
    private String id;
    private ArrayList<Use> uses;

    public Value(String value, Type type) {
        this.value = value;
        this.type = type;
        this.id = "unique" + idNum++;
        this.uses = new ArrayList<>();
    }

    public Type getType() {
        return type;
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
