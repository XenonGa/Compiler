package Node;

import FileProcess.MyFileWriter;
import Identifier.FuncParam;
import LLVM_IR.BuilderAttribute;
import Parse.NodeTypeMap;

// 表达式 Exp → AddExp
public class Exp extends Node {
    private AddExp addExp;

    public Exp(AddExp addExp) {
        this.addExp = addExp;
    }

    public AddExp getAddExp() {
        return addExp;
    }

    public void writeNode() {
        addExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.Exp));
    }

    public static Exp makeExp() {
        AddExp addExp1 = AddExp.makeAddExp();
        return new Exp(addExp1);
    }

    public static void expErrorHandler(Exp exp) {
        AddExp.addExpErrorHandler(exp.addExp);
    }

    public static FuncParam getFuncParamFromExp(Exp exp) {
        return AddExp.getFuncParamFromAddExp(exp.addExp);
    }

    public static void expLLVMBuilder(Exp exp) {
        BuilderAttribute.curTempValue = null;
        BuilderAttribute.curSaveValue = null;
        AddExp.AddExpLLVMBuilder(exp.addExp);
    }
}
