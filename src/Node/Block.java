package Node;

import ErrorHandler.ErrorHandler;
import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;
import ErrorHandler.*;

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
        while(!checkCurrentTokenCategory("RBRACE")) {
            blockItems.add(BlockItem.makeBlockItem());
        }
        Token rightBrace1 = Parser.checkCategory("RBRACE");
        return new Block(leftBrace1, blockItems, rightBrace1);
    }

    public static void blockErrorHandler(Block block) {
        for(BlockItem item : block.blockItemArrayList) {
            BlockItem.blockItemErrorHandler(item);
        }
        if(ErrorHandler.symbolTableStack.get(ErrorHandler.symbolTableStack.size() - 1).isFunction()) {
            if(Objects.equals(ErrorHandler.symbolTableStack.get(ErrorHandler.symbolTableStack.size() - 1).getFunctionType(), "int")) {
                if(block.blockItemArrayList.isEmpty() ||
                    block.blockItemArrayList.get(block.blockItemArrayList.size() - 1).getStmt() == null ||
                    block.blockItemArrayList.get(block.blockItemArrayList.size() - 1).getStmt().getReturnTK() == null ||
                    block.blockItemArrayList.get(block.blockItemArrayList.size() - 1).getStmt().getExp() == null) {
                    MyError myError = new MyError("g", block.rightBrace.getLineNumber());
                    ErrorHandler.addNewError(myError);
                }
            }
        }
    }
}
