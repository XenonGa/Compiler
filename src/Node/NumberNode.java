package Node;

import FileProcess.MyFileWriter;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Structure.ConstNum;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

// 数值 Number → IntConst
public class NumberNode extends Node {
    private Token intConst;

    public NumberNode(Token intConst) {
        this.intConst = intConst;
    }

    public Token getIntConst() {
        return intConst;
    }

    public void writeNode() {
        MyFileWriter.write(intConst.getWholeToken());
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.Number));
    }

    public static NumberNode makeNumberNode() {
        Token intConst = Parser.checkCategory("INTCON");
        return new NumberNode(intConst);
    }

    public static void numberNodeLLVMBuilder(NumberNode numberNode) {
        if(BuilderAttribute.isConstant) {
            BuilderAttribute.curSaveValue = Integer.parseInt(numberNode.intConst.getToken());
        }
        else {
            BuilderAttribute.curTempValue = new ConstNum(Integer.parseInt(numberNode.intConst.getToken()));
        }
    }
}
