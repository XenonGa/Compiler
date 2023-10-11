package ErrorHandler;

import FileProcess.MyFileWriter;
import Identifier.Identifier;
import Identifier.SymbolTableForError;
import Node.CompUnit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

public class ErrorHandler {
    public static ArrayList<MyError> errorArrayList = new ArrayList<>();
    public static ArrayList<SymbolTableForError> symbolTableForErrorStack = new ArrayList<>();
    public static int forFlag = 0;

    public static ArrayList<MyError> getErrorArrayList() {
        return errorArrayList;
    }

    public ErrorHandler(CompUnit compUnit) {
        CompUnit.compUnitErrorHandler(compUnit);
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
        SymbolTableForError table = new SymbolTableForError(symbolTable, isFunction, functionType);
        symbolTableForErrorStack.add(table);
    }

    public static void popSymbolTable() {
        symbolTableForErrorStack.remove(symbolTableForErrorStack.size() - 1);
    }

    public static boolean isIdentConflicted(String name) {
        return symbolTableForErrorStack.get(symbolTableForErrorStack.size() - 1).getSymbols().containsKey(name);
    }

    public static boolean isDeclared(String name) {
        for(int i = symbolTableForErrorStack.size() - 1; i >= 0; i--) {
            if(symbolTableForErrorStack.get(i).getSymbols().containsKey(name)) return true;
        }
        return false;
    }

    public static void addInSymbolTable(String name, Identifier identifier) {
        symbolTableForErrorStack.get(symbolTableForErrorStack.size() - 1).getSymbols().put(name, identifier);
    }

    public static Identifier getIdentifier(String name) {
        for (int i = symbolTableForErrorStack.size() - 1; i >= 0; i--) {
            if(symbolTableForErrorStack.get(i).getSymbols().containsKey(name)) return symbolTableForErrorStack.get(i).getSymbols().get(name);
        }
        return null;
    }

    public static boolean isInFunction() {
        for(int i = symbolTableForErrorStack.size() - 1; i >= 0; i--) {
            if(symbolTableForErrorStack.get(i).isFunction()) return true;
        }
        return false;
    }

    public static String getFunctionType() {
        for(int i = symbolTableForErrorStack.size() - 1; i >= 0; i--) {
            if(symbolTableForErrorStack.get(i).isFunction()) return symbolTableForErrorStack.get(i).getFunctionType();
        }
        return null;
    }
}

