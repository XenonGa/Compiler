package LLVM_IR;

import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeInt;
import LLVM_IR.LLVMType.TypeVoid;
import LLVM_IR.Structure.BasicBlock;
import LLVM_IR.Structure.ConstNum;
import LLVM_IR.Structure.Function;
import LLVM_IR.Structure.Value;

import java.util.ArrayList;

public class BuilderAttribute {

    public static TypeInt i1 = new TypeInt(1);
    public static TypeInt i8 = new TypeInt(8);
    public static TypeInt i32 = new TypeInt(32);
    public static ConstNum zero = new ConstNum(0);
    public static TypeVoid typeVoid = new TypeVoid();
    public static Type curTempType;
    public static boolean isAtGlobal = true;
    public static boolean isConstant = false;
    public static Function curentFunction;
    public static BasicBlock currentBlock;

    public static Value curTempValue;
    public static Integer curSaveValue;
    public static String curTempOperator;
    public static String curSaveOperator;
    public static ArrayList<String> formatStringArrayList = new ArrayList<>();

    public static ArrayList<Type> paramTypeArrayList;

    public static ArrayList<Value> tempParamArrayList;

    public static ArrayList<Value> funcParamArrayList = new ArrayList<>();

    public static Boolean isCreatingFunction = false;

    public static int tempIndex = 0;

    public static ArrayList<Integer> arrayDimensionList = new ArrayList<>();
    public static int arrayDepth = 0;
    public static int arrayOffset = 0;
    public static Value currentArray = null;
    public static Boolean isCreatingArray = false;
    public static String curTempIdent = null;

    public static BasicBlock currentIfBlock = null;
    public static BasicBlock currentElseBlock = null;

    public static BasicBlock currentAfterForBlock = null;
    public static BasicBlock currentContinueBlock = null;
}
