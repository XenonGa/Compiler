import ErrorHandler.ErrorHandler;
import FileProcess.MyFileWriter;
import LLVM_IR.Builder;
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
//            MyFileWriter.setWriter("error.txt");
//            ErrorHandler errorHandler = new ErrorHandler(parser.getCompUnit());
//            errorHandler.writeErrorArrayList();

            Builder LLVMBuilder = new Builder();
            LLVMBuilder.BuildLLVM(parser.getCompUnit());
            MyFileWriter.setWriter("llvm_ir.txt");
            String LLVM = LLVMBuilder.writeLLVM();
            MyFileWriter.write(LLVM);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}