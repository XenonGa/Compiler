package LexicalAnalysis;

import java.util.HashMap;
import java.util.Map;

public class KeyWord {
    public final static Map<String, String> keyWordMap = new HashMap<String, String>(){{
        put("Ident", "IDENFR");
        put("IntConst", "INTCON");
        put("FormatString", "STRCON");
        put("main", "MAINTK");
        put("const", "CONSTTK");
        put("int", "INTTK");
        put("break", "BREAKTK");
        put("continue", "CONTINUETK");
        put("if", "IFTK");
        put("else", "ELSETK");
        put("!", "NOT");
        put("&&", "AND");
        put("||", "OR");
        put("for", "FORTK");
        put("getint", "GETINTTK");
        put("printf", "PRINTFTK");
        put("return", "RETURNTK");
        put("+", "PLUS");
        put("-", "MINU");
        put("void", "VOIDTK");
        put("*", "MULT");
        put("/", "DIV");
        put("%", "MOD");
        put("<", "LSS");
        put("<=", "LEQ");
        put(">", "GRE");
        put(">=", "GEQ");
        put("==", "EQL");
        put("!=", "NEQ");
        put("=", "ASSIGN");
        put(";", "SEMICN");
        put(",", "COMMA");
        put("(", "LPARENT");
        put(")", "RPARENT");
        put("[", "LBRACK");
        put("]", "RBRACK");
        put("{", "LBRACE");
        put("}", "RBRACE");
    }};

    public static boolean isKeyword(String word) {
        return keyWordMap.containsKey(word);
    }

    public static String getKeyword(String word) {
        return keyWordMap.get(word);
    }
}
