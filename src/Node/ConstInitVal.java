package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.awt.event.PaintEvent;
import java.util.ArrayList;
import java.util.Objects;

// 常量初值 ConstInitVal → ConstExp
// | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
public class ConstInitVal extends Node {
    private ConstExp constExp;
    private Token leftBrace;
    private ArrayList<ConstInitVal> constInitValArrayList;
    private ArrayList<Token> commaArrayList;
    private Token rightBrace;

    public ConstInitVal(ConstExp constExp) {
        this.constExp = constExp;
    }

    public ConstInitVal(Token leftBrace, ArrayList<ConstInitVal> constInitValArrayList, ArrayList<Token> commaArrayList, Token rightBrace) {
        this.leftBrace = leftBrace;
        this.constInitValArrayList = constInitValArrayList;
        this.commaArrayList = commaArrayList;
        this.rightBrace = rightBrace;
    }

    public ConstExp getConstExp() {
        return constExp;
    }

    public ArrayList<ConstInitVal> getConstInitValArrayList() {
        return constInitValArrayList;
    }

    public void writeNode() {
        if(constExp != null) constExp.writeNode();
        else {
            MyFileWriter.write(leftBrace.getWholeToken());
            if(!constInitValArrayList.isEmpty()) {
                constInitValArrayList.get(0).writeNode();
                for(int i = 1; i < constInitValArrayList.size(); i++)
                {
                    MyFileWriter.write(commaArrayList.get(i - 1).getWholeToken());
                    constInitValArrayList.get(i).writeNode();
                }
            }
            MyFileWriter.write(rightBrace.getWholeToken());
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.ConstInitVal));
    }

    public static ConstInitVal makeConstInitVal() {
        if(checkCurrentTokenCategory( "LBRACE")) {
            ArrayList<ConstInitVal> constInitVals = new ArrayList<>();
            ArrayList<Token> commas = new ArrayList<>();

            Token leftBrace = Parser.checkCategory("LBRACE");
            if(!checkCurrentTokenCategory("RBRACE")) {
                constInitVals.add(makeConstInitVal());
                while (!checkCurrentTokenCategory("RBRACE")) {
                    commas.add(Parser.checkCategory("COMMA"));
                    constInitVals.add(makeConstInitVal());
                }
            }
            Token rightBrace = Parser.checkCategory("RBRACE");
            return new ConstInitVal(leftBrace, constInitVals, commas, rightBrace);
        }
        else {
            ConstExp constExp1 = ConstExp.makeConstExp();
            return new ConstInitVal(constExp1);
        }
    }

    public static void constInitValErrorHandler(ConstInitVal constInitVal) {
        if(constInitVal.constExp != null) {
            ConstExp.constExpErrorHandler(constInitVal.constExp);
            return;
        }
        for(ConstInitVal constInitVal1 : constInitVal.constInitValArrayList) {
            constInitValErrorHandler(constInitVal1);
        }
    }

    // TODO ConstInitVal -> ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'
    public static void constInitValLLVMBuilder(ConstInitVal constInitVal) {
        if(constInitVal.constExp != null) {
            ConstExp.constExpLLVMBuilder(constInitVal.constExp);
        }
        else {
            // TODO ARRAY
        }
    }
}
