package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.Objects;

// 函数类型 FuncType → 'void' | 'int'
public class FuncType extends Node {
    private Token voidTK;
    private Token intTK;

    public FuncType(Token TK) {
        if(Objects.equals(TK.getCategory(), "VOIDTK")) {
            this.voidTK = TK;
        }
        else  this.intTK = TK;
    }

    public void writeNode() {
        if(voidTK != null) {
            MyFileWriter.write(voidTK.getWholeToken());
        }
        else {
            MyFileWriter.write(intTK.getWholeToken());
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.FuncType));
    }

    public static FuncType makeFuncType() {
        Token TK;
        if(Objects.equals(Parser.currentToken.getCategory(), "VOIDTK")) {
            TK = Parser.checkCategory("VOIDTK");
        }
        else {
            TK = Parser.checkCategory("INTTK");
        }
        return new FuncType(TK);
    }
}
