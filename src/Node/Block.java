package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

// 语句块 Block → '{' { BlockItem } '}'
public class Block extends Node{
    private Token leftBrace;
    private ArrayList<BlockItem> blockItemArrayList;
    private Token rightBrace;

    public Block(Token leftBrace, ArrayList<BlockItem> blockItemArrayList, Token rightBrace) {
        this.leftBrace = leftBrace;
        this.blockItemArrayList = blockItemArrayList;
        this.rightBrace = rightBrace;
    }

    public ArrayList<BlockItem> getBlockItemArrayList() {
        return blockItemArrayList;
    }

    public void writeNode() {
        MyFileWriter.write(leftBrace.getWholeToken());
        for (BlockItem blockItem : blockItemArrayList) {
            blockItem.writeNode();
        }
        MyFileWriter.write(rightBrace.getWholeToken());
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.Block));
    }

    public static Block makeBlock() {
        ArrayList<BlockItem> blockItems = new ArrayList<>();

        Token leftBrace1 = Parser.checkCategory("LBRACE");
        while(!Objects.equals(Parser.currentToken.getCategory(), "RBRACE")) {
            blockItems.add(BlockItem.makeBlockItem());
        }
        Token rightBrace1 = Parser.checkCategory("RBRACE");
        return new Block(leftBrace1, blockItems, rightBrace1);
    }
}
