import LexicalAnalysis.Lexer;
import LexicalAnalysis.Token;
import Parse.Parser;

import java.io.IOException;

public class Compiler {
    public static void main(String[] args) {
        try {
            new Lexer();
            Parser parser = new Parser(Lexer.tokenList);
            parser.writeNodeParser();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}