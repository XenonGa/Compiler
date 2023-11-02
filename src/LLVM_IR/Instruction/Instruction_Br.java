package LLVM_IR.Instruction;

import LLVM_IR.BuilderAttribute;

public class Instruction_Br extends Instruction{
    public Instruction_Br() {
        super(BuilderAttribute.typeVoid, "br");
    }
}
