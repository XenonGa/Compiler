package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

// 常量声明 ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';'
public class ConstDecl extends Node {
    private Token constTK;
    private BType bType;
    private ArrayList<ConstDef> constDefArrayList;
    private ArrayList<Token> commaArrayList;
    private Token semicolon;

    public ConstDecl(Token constTK, BType bType, ArrayList<ConstDef> constDefArrayList, ArrayList<Token> commaArrayList, Token semicolon) {
        this.constTK = constTK;
        this.bType = bType;
        this.constDefArrayList = constDefArrayList;
        this.commaArrayList = commaArrayList;
        this.semicolon = semicolon;
    }

    public BType getbType() {
        return bType;
    }

    public ArrayList<ConstDef> getConstDefArrayList() {
        return constDefArrayList;
    }

    public void writeNode() {
        MyFileWriter.write(constTK.getWholeToken());
        bType.writeNode();
        constDefArrayList.get(0).writeNode();
        for(int i = 1; i < constDefArrayList.size(); i++) {
            MyFileWriter.write(commaArrayList.get(0).getWholeToken());
            constDefArrayList.get(i).writeNode();
        }
        MyFileWriter.write(semicolon.getWholeToken());
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.ConstDecl));
    }

    public static ConstDecl makeConstDecl() {
        ArrayList<ConstDef> constDefs = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();
        Token semicolon;

        Token constTK1 = Parser.checkCategory("CONSTTK");
        BType bType1 = BType.makeBType();
        constDefs.add(ConstDef.makeConstDef());
        while(checkCurrentTokenCategory("COMMA")) {
            commas.add(Parser.checkCategory("COMMA"));
            constDefs.add(ConstDef.makeConstDef());
        }
        semicolon = Parser.checkCategory("SEMICN");
        return new ConstDecl(constTK1, bType1, constDefs, commas, semicolon);
    }
}
