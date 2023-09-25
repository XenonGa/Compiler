package Parse;

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
        return null;
    }
}
