package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

// 函数实参表 FuncRParams → Exp { ',' Exp }
public class FuncRParams extends Node{
    private ArrayList<Exp> expArrayList;
    private ArrayList<Token> commaArrayList;

    public FuncRParams(ArrayList<Exp> expArrayList, ArrayList<Token> commaArrayList) {
        this.expArrayList = expArrayList;
        this.commaArrayList = commaArrayList;
    }

    public ArrayList<Exp> getExpArrayList() {
        return expArrayList;
    }

    public void writeNode() {
        expArrayList.get(0).writeNode();
        for(int i = 1; i < expArrayList.size(); i++) {
            MyFileWriter.write(commaArrayList.get(i - 1).getWholeToken());
            expArrayList.get(i).writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.FuncRParams));
    }

    public static FuncRParams makeFuncParams() {
        ArrayList<Exp> exps = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();

        exps.add(Exp.makeExp());
        while(checkCurrentTokenCategory("COMMA")) {
            commas.add(Parser.checkCategory("COMMA"));
            exps.add(Exp.makeExp());
        }
        return new FuncRParams(exps, commas);
    }

    public static void funcRParamsErrorHandler(FuncRParams funcRParams) {
        for(Exp exp : funcRParams.expArrayList) {
            Exp.expErrorHandler(exp);
        }
    }
}
