package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

// 函数形参 FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }]
public class FuncFParam extends Node{
    private BType bType;
    private Token ident;
    private ArrayList<Token> leftBracketArrayList;
    private ArrayList<Token> rightBracketArrayList;
    private ConstExp constExp;

    public FuncFParam(BType bType, Token ident, ArrayList<Token> leftBracketArrayList, ArrayList<Token> rightBracketArrayList, ConstExp constExp) {
        this.bType = bType;
        this.ident = ident;
        this.leftBracketArrayList = leftBracketArrayList;
        this.rightBracketArrayList = rightBracketArrayList;
        this.constExp = constExp;
    }

    public BType getBType() {
        return bType;
    }

    public Token getIdent() {
        return ident;
    }

    public ConstExp getConstExp() {
        return constExp;
    }

    public void writeNode() {
        bType.writeNode();
        MyFileWriter.write(ident.getWholeToken());
        if(!leftBracketArrayList.isEmpty()) {
            MyFileWriter.write(leftBracketArrayList.get(0).getWholeToken());
            MyFileWriter.write(rightBracketArrayList.get(0).getWholeToken());
            if(constExp!= null) {
                MyFileWriter.write(leftBracketArrayList.get(1).getWholeToken());
                constExp.writeNode();
                MyFileWriter.write(rightBracketArrayList.get(1).getWholeToken());
            }
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.FuncFParam));
    }

    public static FuncFParam makeFuncFParam() {
        ArrayList<Token> leftBrackets = new ArrayList<>();
        ArrayList<Token> rightBrackets = new ArrayList<>();
        ConstExp constExp1 = null;

        BType bType1 = BType.makeBType();
        Token identifier = Parser.checkCategory("IDENFR");
        if(Objects.equals(Parser.currentToken.getCategory(), "LBRACK")){
            leftBrackets.add(Parser.checkCategory("LBRACK"));
            rightBrackets.add(Parser.checkCategory("RBRACK"));
            if(Objects.equals(Parser.currentToken.getCategory(), "LBRACK")){
                leftBrackets.add(Parser.checkCategory("LBRACK"));
                constExp1 = ConstExp.makeConstExp();
                rightBrackets.add(Parser.checkCategory("RBRACK"));
            }
        }
        return new FuncFParam(bType1, identifier, leftBrackets, rightBrackets, constExp1);

    }
}
