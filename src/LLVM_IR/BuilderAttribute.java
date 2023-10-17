package LLVM_IR;

import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeSpace;
import LLVM_IR.LLVMType.TypeVoid;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.Structure.Function;

public class BuilderAttribute {

    public static TypeSpace i1 = new TypeSpace(1);
    public static TypeSpace i8 = new TypeSpace(8);
    public static TypeSpace i32 = new TypeSpace(32);
    public static TypeVoid typeVoid = new TypeVoid();
    public static Type curType;
    public static boolean isAtGlobal = false;
    public static Function curentFunction;
    public static BasicBlock currentBlock;

    // 使用的寄存器编号
    public static int register = 0;
}
