package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;

import java.util.ArrayList;

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
}
