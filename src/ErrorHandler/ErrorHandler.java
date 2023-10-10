package ErrorHandler;

import FileProcess.MyFileWriter;
import Identifier.Identifier;
import Identifier.SymbolTable;
import Node.CompUnit;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ErrorHandler {
    public static ArrayList<MyError> errorArrayList = new ArrayList<>();
    public static ArrayList<SymbolTable> symbolTableStack = new ArrayList<>();
    public static int forFlag = 0;

    public static ArrayList<MyError> getErrorArrayList() {
        return errorArrayList;
    }

    public ErrorHandler(CompUnit compUnit) {
        CompUnit.CompUnitErrorHandler(compUnit);
    }
    public static void addNewError(MyError error) {
        for (MyError value : errorArrayList) {
            if (value.isSameLine(error.getError_line_num())) return;
        }
        errorArrayList.add(error);
    }
    public void writeErrorArrayList() {
        errorArrayList.sort(Comparator.comparingInt((MyError a) -> a.getError_line_num()));
        for(MyError a : errorArrayList) {
            MyFileWriter.write(a.writeError());
        }
    }

    public static void pushSymbolTable(boolean isFunction, String functionType) {
        HashMap<String, Identifier> symbolTable = new HashMap<>();
        SymbolTable table = new SymbolTable(symbolTable, isFunction, functionType);
        symbolTableStack.add(table);
    }

    public static void popSymbolTable() {
        symbolTableStack.remove(symbolTableStack.size() - 1);
    }

    public static boolean isIdentConflicted(String name) {
        return symbolTableStack.get(symbolTableStack.size() - 1).getSymbols().containsKey(name);
    }

    public static boolean isDeclared(String name) {
        for(int i = symbolTableStack.size() - 1; i >= 0; i--) {
            if(symbolTableStack.get(i).getSymbols().containsKey(name)) return true;
        }
        return false;
    }

    public static void addInSymbolTable(String name, Identifier identifier) {
        symbolTableStack.get(symbolTableStack.size() - 1).getSymbols().put(name, identifier);
    }

    public static Identifier getIdentifier(String name) {
        for (int i = symbolTableStack.size() - 1; i >= 0; i--) {
            if(symbolTableStack.get(i).getSymbols().containsKey(name)) return symbolTableStack.get(i).getSymbols().get(name);
        }
        return null;
    }

    public static boolean isInFunction() {
        for(int i = symbolTableStack.size() - 1; i >= 0; i--) {
            if(symbolTableStack.get(i).isFunction()) return true;
        }
        return false;
    }

    public static String getFunctionType() {
        for(int i = symbolTableStack.size() - 1; i >= 0; i--) {
            if(symbolTableStack.get(i).isFunction()) return symbolTableStack.get(i).getFunctionType();
        }
        return null;
    }
}

