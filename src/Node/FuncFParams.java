package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

// 函数形参表 FuncFParams → FuncFParam { ',' FuncFParam }
public class FuncFParams extends Node {
    private ArrayList<FuncFParam> funcFParamArrayList;
    private ArrayList<Token> commaArrayList;

    public FuncFParams(ArrayList<FuncFParam> funcFParamArrayList, ArrayList<Token> commaArrayList) {
        this.funcFParamArrayList = funcFParamArrayList;
        this.commaArrayList = commaArrayList;
    }

    public ArrayList<FuncFParam> getFuncFParamArrayList() {
        return funcFParamArrayList;
    }

    public void writeNode() {
        funcFParamArrayList.get(0).writeNode();
        for(int i = 1; i < funcFParamArrayList.size(); i++) {
            MyFileWriter.write(commaArrayList.get(i - 1).getWholeToken());
            funcFParamArrayList.get(i).writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.FuncFParams));
    }

    public static FuncFParams makeFuncFParams() {
        ArrayList<FuncFParam> funcFParams = new ArrayList<>();
        ArrayList<Token> commas = new ArrayList<>();

        funcFParams.add(FuncFParam.makeFuncFParam());
        while(checkCurrentTokenCategory("COMMA")) {
            commas.add(Parser.checkCategory("COMMA"));
            funcFParams.add(FuncFParam.makeFuncFParam());
        }
        return new FuncFParams(funcFParams, commas);
    }

    public static void funcFParamsErrorHandler(FuncFParams funcFParams) {
        for(FuncFParam param : funcFParams.funcFParamArrayList) {
            FuncFParam.funcFParamErrorHandler(param);
        }
    }
}
