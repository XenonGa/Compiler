package Identifier;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class FuncIdent extends Identifier {
    private String funcName;
    private String funcType;
    private ArrayList<FuncParam> FuncParams;

    public FuncIdent(String funcName, String funcType, ArrayList<FuncParam> funcParams) {
        this.funcName = funcName;
        this.funcType = funcType;
        FuncParams = funcParams;
    }

    public String getFuncName() {
        return funcName;
    }

    public String getFuncType() {
        return funcType;
    }

    public ArrayList<FuncParam> getFuncParams() {
        return FuncParams;
    }
}
