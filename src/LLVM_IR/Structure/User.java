package LLVM_IR.Structure;

import java.util.ArrayList;
import LLVM_IR.LLVMType.*;

public class User extends Value {
    private ArrayList<Value> ops;
    public User(String ident, Type type) {
        super(ident, type);
        this.ops = new ArrayList<>();
    }

    public ArrayList<Value> getOpList() {
        return ops;
    }

    public Value getOp(int index) {
        return ops.get(index);
    }

    public void setOp(int index, Value value) {
        if(index > ops.size()) return;
        this.ops.set(index, value);
        if(value != null) {
            // add use
            Use use = new Use(value, this, index);
            value.addUse(use);
        }
    }

    public void addOp(Value value) {
        this.ops.add(value);
        if(value != null) {
            // add use
            Use use = new Use(value, this, this.ops.size() - 1);
            value.addUse(use);
        }
    }

    public void replaceOp(int index, Value value) {
        Value oldValue = ops.get(index);
        this.setOp(index, value);
        if(oldValue != null) {
            if(!this.ops.contains(value)) {
                // remove use
                oldValue.removeUse(this);
            }
        }
    }

    public void removeUse() {
        if(this.ops == null) return;
        for (Value op : this.ops) {
            if (op != null) {
                // remove use
                op.removeUse(this);
            }
        }
    }

    public void removeUseByIndex(ArrayList<Integer> indexList) {
        this.removeUse();
        ArrayList<Value> values = new ArrayList<>();
        values.addAll(this.ops);
        for(int i = 0; i < values.size(); i++) {
            if(!indexList.contains(i))  {
                Use use = new Use(values.get(i), this, this.ops.size());
                values.get(i).addUse(use);
                this.ops.add(values.get(i));
            }
        }
    }
}
