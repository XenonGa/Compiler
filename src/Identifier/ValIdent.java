package Identifier;

public class ValIdent extends Identifier{
    private String valName;
    private boolean constFlag;
    private int valDimension;

    public ValIdent(String valName, boolean constFlag, int valDimension) {
        this.valName = valName;
        this.constFlag = constFlag;
        this.valDimension = valDimension;
    }

    public String getValName() {
        return valName;
    }

    public boolean isConstFlag() {
        return constFlag;
    }

    public int getValDimension() {
        return valDimension;
    }
}
