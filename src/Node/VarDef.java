package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

// 变量定义 VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal
public class VarDef extends Node {
    private Token ident;
    private ArrayList<Token> leftBracketArrayList;
    private ArrayList<ConstExp> constExpArrayList;
    private ArrayList<Token> rightBracketArrayList;
    private Token assign;
    private InitVal initVal;

    public VarDef(Token ident, ArrayList<Token> leftBracketArrayList, ArrayList<ConstExp> constExpArrayList, ArrayList<Token> rightBracketArrayList) {
        this.ident = ident;
        this.leftBracketArrayList = leftBracketArrayList;
        this.constExpArrayList = constExpArrayList;
        this.rightBracketArrayList = rightBracketArrayList;
    }

    public VarDef(Token ident, ArrayList<Token> leftBracketArrayList, ArrayList<ConstExp> constExpArrayList, ArrayList<Token> rightBracketArrayList, Token assign, InitVal initVal) {
        this.ident = ident;
        this.leftBracketArrayList = leftBracketArrayList;
        this.constExpArrayList = constExpArrayList;
        this.rightBracketArrayList = rightBracketArrayList;
        this.assign = assign;
        this.initVal = initVal;
    }

    public Token getIdent() {
        return ident;
    }

    public ArrayList<ConstExp> getConstExpArrayList() {
        return constExpArrayList;
    }

    public InitVal getInitVal() {
        return initVal;
    }

    public void writeNode() {
        MyFileWriter.write(ident.getWholeToken());
        for (ConstExp constExp : constExpArrayList) {
            MyFileWriter.write(leftBracketArrayList.get(0).getWholeToken());
            constExp.writeNode();
            MyFileWriter.write(rightBracketArrayList.get(0).getWholeToken());
        }
        if(initVal != null) {
            MyFileWriter.write(assign.getWholeToken());
            initVal.writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.VarDef));
    }

    public static VarDef makeVarDef() {
        ArrayList<Token> leftBrackets = new ArrayList<>();
        ArrayList<ConstExp> constExps = new ArrayList<>();
        ArrayList<Token> rightBrackets = new ArrayList<>();

        Token identifier = Parser.checkCategory("IDENFR");
        while(checkCurrentTokenCategory("LBRACK")) {
            leftBrackets.add(Parser.checkCategory("LBRACK"));
            constExps.add(ConstExp.makeConstExp());
            rightBrackets.add(Parser.checkCategory("RBRACK"));
        }
        if(checkCurrentTokenCategory("ASSIGN")) {
            Token assign1 = Parser.checkCategory("ASSIGN");
            InitVal initVal1 = InitVal.makeInitVal();
            return new VarDef(identifier, leftBrackets, constExps, rightBrackets, assign1, initVal1);
        }
        return new VarDef(identifier, leftBrackets, constExps, rightBrackets);
    }
}
