package LLVM_IR;

import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeInt;
import LLVM_IR.LLVMType.TypeVoid;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.Structure.ConstNum;
import LLVM_IR.Structure.Function;
import LLVM_IR.Structure.Value;

public class BuilderAttribute {

    public static TypeInt i1 = new TypeInt(1);
    public static TypeInt i8 = new TypeInt(8);
    public static TypeInt i32 = new TypeInt(32);
    public static ConstNum zero = new ConstNum(0);
    public static TypeVoid typeVoid = new TypeVoid();
    public static Type curType;
    public static boolean isAtGlobal = false;
    public static boolean isConstant = false;
    public static Function curentFunction;
    public static BasicBlock currentBlock;

    // 使用的寄存器编号
    public static int register = 0;

    public static Value curTempValue;
    public static Integer curSaveValue;
    public static String curTempOperator;
    public static String curSaveOperator;
}
