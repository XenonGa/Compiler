package Parse;

import ErrorHandler.ErrorHandler;
import ErrorHandler.MyError;
import LexicalAnalysis.Token;
import Node.CompUnit;

import java.util.ArrayList;
import java.util.Objects;

public class Parser {
    private CompUnit compUnit;
    public static Token currentToken;
    public static int index = 0;
    public static ArrayList<Token> tokenArrayList;

    public Parser(ArrayList<Token> tokenArray) {
        tokenArrayList = tokenArray;
        currentToken = tokenArrayList.get(0);
        this.compUnit = CompUnit.makeCompUnit();
    }

    public CompUnit getCompUnit() {
        return compUnit;
    }

    public static Token checkCategory(String category) {
        if(Objects.equals(currentToken.getCategory(), category)) {
            Token temp = currentToken;
            if(index < tokenArrayList.size() - 1) {
                index += 1;
                currentToken = tokenArrayList.get(index);
            }
            return temp;
        }
        else if(Objects.equals(category, "SEMICN")) {
            addError("i");
            return new Token("SEMICN", ";", tokenArrayList.get(index - 1).getLineNumber());
        }
        else if(Objects.equals(category, "RPARENT")) {
            addError("j");
            return new Token("RPARENT", ")", tokenArrayList.get(index - 1).getLineNumber());
        }
        else if(Objects.equals(category, "RBRACK")) {
            addError("k");
            return new Token("RBRACK", "]", tokenArrayList.get(index - 1).getLineNumber());
        }
        else return null;
    }

    public void writeNodeParser() {
        compUnit.writeNode();
    }

    public static void addError(String errorType) {
        MyError newError = new MyError(errorType, tokenArrayList.get(index - 1).getLineNumber());
        ErrorHandler.addNewError(newError);
    }
}
