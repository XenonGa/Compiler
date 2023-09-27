package Node;

import Parse.Parser;

import java.util.Objects;

public abstract class Node {
    public abstract void writeNode();
    public static boolean checkCurrentTokenCategory(String category) {
        return Objects.equals(Parser.currentToken.getCategory(), category);
    }
}
