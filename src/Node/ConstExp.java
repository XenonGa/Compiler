package Node;

import FileProcess.MyFileWriter;
import LLVM_IR.Builder;
import LLVM_IR.BuilderAttribute;
import Parse.NodeTypeMap;

// 常量表达式 ConstExp → AddExp
public class ConstExp extends Node {
    private AddExp addExp;

    public ConstExp(AddExp addExp) {
        this.addExp = addExp;
    }

    public AddExp getAddExp() {
        return addExp;
    }

    public void writeNode() {
        addExp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.ConstExp));
    }

    public static ConstExp makeConstExp() {
        AddExp addExp = AddExp.makeAddExp();
        return new ConstExp(addExp);
    }

    public static void constExpErrorHandler(ConstExp constExp) {
        AddExp.addExpErrorHandler(constExp.addExp);
    }

    // TODO ConstExp -> AddExp
    public static void constExpLLVMBuilder(ConstExp constExp) {
        BuilderAttribute.curSaveValue = null;
        BuilderAttribute.isConstant = true;

        AddExp.AddExpLLVMBuilder(constExp.addExp);

        BuilderAttribute.isConstant = false;
    }
}
