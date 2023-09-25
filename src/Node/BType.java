package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.Parser;

// 基本类型 BType → 'int'
public class BType extends Node {
    private Token intTK;

    public BType(Token intTK) {
        this.intTK = intTK;
    }

    public void writeNode() {
        MyFileWriter.write(intTK.getWholeToken());
    }

    public static BType makeBType() {
        Token intTK = Parser.checkCategory("INTTK");
        return new BType(intTK);
    }
}
