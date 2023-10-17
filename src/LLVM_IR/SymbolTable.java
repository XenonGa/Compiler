package LLVM_IR;

import LLVM_IR.Structure.Value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SymbolTable {
    private Map<String, Value> valSymbolTable;
    private Map<String, Integer> constSymbolTable;

    public static ArrayList<SymbolTable> LLVMSymbolTableStack = new ArrayList<>();

    public SymbolTable() {
        valSymbolTable = new HashMap<String, Value>();
        constSymbolTable = new HashMap<String, Integer>();
    }

    public Map<String, Value> getValSymbolTable() {
        return valSymbolTable;
    }

    public Map<String, Integer> getConstSymbolTable() {
        return constSymbolTable;
    }

    public static void pushLLVMSymbolTable() {
        LLVMSymbolTableStack.add(new SymbolTable());
    }

    public static void popLLVMSymbolTable() {
        LLVMSymbolTableStack.remove(LLVMSymbolTableStack.size() - 1);
    }

    public static void addValSymbol(String ident, Value value) {
        LLVMSymbolTableStack.get(LLVMSymbolTableStack.size() - 1).valSymbolTable.put(ident, value);
    }

    public static void addGlobalValSymbol(String ident, Value value) {
        LLVMSymbolTableStack.get(0).valSymbolTable.put(ident, value);
    }

    public static Value getValSymbol(String ident) {
        for(int i = LLVMSymbolTableStack.size() - 1; i >= 0; i--) {
            if(LLVMSymbolTableStack.get(i).valSymbolTable.containsKey(ident)) {
                return LLVMSymbolTableStack.get(i).valSymbolTable.get(ident);
            }
        }
        return null;
    }

    public static void addConstSymbol(String ident, Integer value) {
        LLVMSymbolTableStack.get(LLVMSymbolTableStack.size() - 1).constSymbolTable.put(ident, value);
    }

    public static Integer getConstSymbol(String ident) {
        for(int i = LLVMSymbolTableStack.size() - 1; i >= 0; i--) {
            if(LLVMSymbolTableStack.get(i).constSymbolTable.containsKey(ident)) {
                return LLVMSymbolTableStack.get(i).constSymbolTable.get(ident);
            }
        }
        return null;
    }
}

