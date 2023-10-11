package LLVM_IR;

import LLVM_IR.LLVMType.*;
import Node.CompUnit;
import Parse.Parser;

public class Builder {

    public static TypeSpace i1 = new TypeSpace(1);
    public static TypeSpace i8 = new TypeSpace(8);
    public static TypeSpace i32 = new TypeSpace(32);
    public static TypeVoid typeVoid = new TypeVoid();
    public Builder(CompUnit compUnit) {
        CompUnit.compUnitBuildLLVM(compUnit);
    }
}
