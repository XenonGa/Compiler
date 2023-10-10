import ErrorHandler.ErrorHandler;
import FileProcess.MyFileWriter;
import LexicalAnalysis.Lexer;
import LexicalAnalysis.Token;
import Parse.Parser;

import java.io.IOException;

public class Compiler {
    public static void main(String[] args) {
        try { 
            new Lexer();
            Parser parser = new Parser(Lexer.tokenList);
//            parser.writeNodeParser();
            MyFileWriter.setWriter("error.txt");
            ErrorHandler errorHandler = new ErrorHandler(parser.getCompUnit());
            errorHandler.writeErrorArrayList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}