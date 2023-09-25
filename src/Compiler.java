import LexicalAnalysis.Lexer;
import LexicalAnalysis.Token;

import java.io.IOException;

public class Compiler {
    public static void main(String[] args) {
        try {
            new Lexer();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}