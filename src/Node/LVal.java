package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

// 左值表达式 LVal → Ident {'[' Exp ']'}
public class LVal extends Node {
    private Token ident;
    private ArrayList<Token> leftBracketArrayList;
    private ArrayList<Exp> expArrayList;
    private ArrayList<Token> rightBracketArrayList;

    public LVal(Token ident, ArrayList<Token> leftBracketArrayList, ArrayList<Exp> expArrayList, ArrayList<Token> rightBracketArrayList) {
        this.ident = ident;
        this.leftBracketArrayList = leftBracketArrayList;
        this.expArrayList = expArrayList;
        this.rightBracketArrayList = rightBracketArrayList;
    }

    public Token getIdent() {
        return ident;
    }

    public ArrayList<Exp> getExpArrayList() {
        return expArrayList;
    }

    public void writeNode() {
        MyFileWriter.write(ident.getWholeToken());
        if(!expArrayList.isEmpty()) {
            for(int i = 0; i < expArrayList.size(); i++) {
                MyFileWriter.write(leftBracketArrayList.get(i).getWholeToken());
                expArrayList.get(i).writeNode();
                MyFileWriter.write(rightBracketArrayList.get(i).getWholeToken());
            }
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.LVal));
    }

    public static LVal makeLVal() {
        ArrayList<Token> leftBrackets = new ArrayList<>();
        ArrayList<Exp> exps = new ArrayList<>();
        ArrayList<Token> rightBrackets = new ArrayList<>();

        Token identifier = Parser.checkCategory("IDENFR");
        while(checkCurrentTokenCategory("LBRACK")) {
            leftBrackets.add(Parser.checkCategory("LBRACK"));
            exps.add(Exp.makeExp());
            rightBrackets.add(Parser.checkCategory("RBRACK"));
        }
        return new LVal(identifier, leftBrackets, exps, rightBrackets);
    }
}
