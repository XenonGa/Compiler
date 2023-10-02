package Identifier;

import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Identifier> symbols;
    private boolean isFunction;
    private String functionType;

    public SymbolTable(Map<String, Identifier> symbols, boolean isFunction, String functionType) {
        this.symbols = symbols;
        this.isFunction = isFunction;
        this.functionType = functionType;
    }

    public Map<String, Identifier> getSymbols() {
        return symbols;
    }

    public boolean isFunction() {
        return isFunction;
    }

    public String getFunctionType() {
        return functionType;
    }
}
