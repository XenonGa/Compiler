package LLVM_IR;

import Node.CompUnit;

public class Builder {

    public Builder(CompUnit compUnit) {
        CompUnit.compUnitLLVMBuilder(compUnit);
    }
}
