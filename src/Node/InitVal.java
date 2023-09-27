package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

// 变量初值 InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}'
public class InitVal extends Node {
    private Exp exp;
    private Token leftBrace;
    private ArrayList<InitVal> initValArrayList;
    private ArrayList<Token> commaArrayList;
    private Token rightBrace;

    public InitVal(Exp exp) {
        this.exp = exp;
    }

    public InitVal(Token leftBrace, ArrayList<InitVal> initValArrayList, ArrayList<Token> commaArrayList, Token rightBrace) {
        this.leftBrace = leftBrace;
        this.initValArrayList = initValArrayList;
        this.commaArrayList = commaArrayList;
        this.rightBrace = rightBrace;
    }

    public Exp getExp() {
        return exp;
    }

    public ArrayList<InitVal> getInitValArrayList() {
        return initValArrayList;
    }

    public void writeNode() {
        if(exp != null) exp.writeNode();
        else {
            MyFileWriter.write(leftBrace.getWholeToken());
            if(!initValArrayList.isEmpty()) {
                for(int i = 0; i < initValArrayList.size(); i++)
                {
                    initValArrayList.get(i).writeNode();
                    if(i < initValArrayList.size() - 1) MyFileWriter.write(commaArrayList.get(i).getWholeToken());
                }
            }
            MyFileWriter.write(rightBrace.getWholeToken());
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.InitVal));
    }

    public static InitVal makeInitVal() {
        ArrayList<InitVal> initVals = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();

        if(!checkCurrentTokenCategory("LBRACE")) {
            Exp exp1 = Exp.makeExp();
            return new InitVal(exp1);
        }
        else {
            Token leftBrace1 = Parser.checkCategory("LBRACE");
            if(!checkCurrentTokenCategory("RBRACE")) {
                initVals.add(InitVal.makeInitVal());
                while(checkCurrentTokenCategory("COMMA")) {
                    commas.add(Parser.checkCategory("COMMA"));
                    initVals.add(InitVal.makeInitVal());
                }
            }
            Token rightBrace1 = Parser.checkCategory("RBRACE");
            return new InitVal(leftBrace1, initVals, commas, rightBrace1);
        }
    }
}
