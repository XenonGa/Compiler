package LLVM_IR.Structure;

import LLVM_IR.Builder;
import LLVM_IR.Instruction.LinkList;
import LLVM_IR.Instruction.LinkListNode;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeFunction;

import java.util.ArrayList;

public class Function extends Value {
    private boolean isLib;
    private ArrayList<Function> frontFunctions;
    private ArrayList<Function> backFunctions;
    private LinkList<BasicBlock, Function> basicBlockList;
    private LinkListNode<Function, Builder> funcNode;
    private ArrayList<FunctionParameter> functionParameterArrayList;




    public Function(String name, Type type, boolean isLib) {
        super(name, type);
        Value.registerIdNum = 0;
        this.isLib = isLib;
        this.frontFunctions = new ArrayList<>();
        this.backFunctions = new ArrayList<>();
        this.basicBlockList = new LinkList<>(this);
        this.funcNode = new LinkListNode<>(this);
        this.functionParameterArrayList = new ArrayList<>();
        for(Type type1 : ((TypeFunction) type).getParamsType()) {
            FunctionParameter fp = new FunctionParameter(type1, isLib);
            this.functionParameterArrayList.add(fp);
        }

        this.funcNode.insertAfterWholeList(Builder.functionList);
    }

    public LinkList<BasicBlock, Function> getBasicBlockList() {
        return basicBlockList;
    }

    public boolean isLib() {
        return isLib;
    }

    public static Function createFunction(String name, Type returnType, ArrayList<Type> paramsType) {
        Type typeFunction = new TypeFunction(returnType, paramsType);
        return new Function(name, typeFunction, false);
    }

    public static Function createLibFunction(String name, Type returnType, ArrayList<Type> paramsType) {
        Type typeFunction = new TypeFunction(returnType, paramsType);
        return new Function(name, typeFunction, true);
    }

    public String writeFunction() {
        StringBuilder str = new StringBuilder();
        str.append(((TypeFunction) this.getType()).getReturnType());
        str.append(" @");
        str.append(this.getValueName());
        str.append("(");

        // Parameter
        for(int i = 0; i < this.functionParameterArrayList.size(); i++) {
            str.append(this.functionParameterArrayList.get(i).getType());
            if(i != this.functionParameterArrayList.size() - 1) {
                str.append(", ");
            }
        }

        str.append(")");
        return str.toString();
    }
}
