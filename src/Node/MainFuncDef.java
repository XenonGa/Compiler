package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

public class MainFuncDef extends Node{
    private Token intTK;
    private Token mainTK;
    private Token leftParent;
    private Token rightParent;
    private Block block;

    public MainFuncDef(Token intTK, Token mainTK, Token leftParent, Token rightParent, Block block) {
        this.intTK = intTK;
        this.mainTK = mainTK;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.block = block;
    }

    public Block getBlock() {
        return block;
    }

    public void writeNode() {
        MyFileWriter.write(intTK.getWholeToken());
        MyFileWriter.write(mainTK.getWholeToken());
        MyFileWriter.write(leftParent.getWholeToken());
        MyFileWriter.write(rightParent.getWholeToken());
        block.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.MainFuncDef));
    }

    public static MainFuncDef makeMainFuncDef() {
        Token intTK1 = Parser.checkCategory("INTTK");
        Token mainTK1 = Parser.checkCategory("MAINTK");
        Token leftParent1 = Parser.checkCategory("LPARENT");
        Token rightParent1 = Parser.checkCategory("RPARENT");
        Block block1 = Block.makeBlock();
        return new MainFuncDef(intTK1, mainTK1, leftParent1, rightParent1, block1);
    }
}
