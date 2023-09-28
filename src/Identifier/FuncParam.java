package Identifier;

public class FuncParam {
    private String funcParamName;
    private int funcParamDimension;

    public FuncParam(String funcParamName, int funcParamDimension) {
        this.funcParamName = funcParamName;
        this.funcParamDimension = funcParamDimension;
    }

    public String getFuncParamName() {
        return funcParamName;
    }

    public int getFuncParamDimension() {
        return funcParamDimension;
    }

    public String writeFuncParam() {
        return null;
    }
}
