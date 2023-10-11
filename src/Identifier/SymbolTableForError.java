package Identifier;

import java.util.Map;

public class SymbolTableForError {
    private Map<String, Identifier> symbols;
    private boolean isFunction;
    private String functionType;

    public SymbolTableForError(Map<String, Identifier> symbols, boolean isFunction, String functionType) {
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
