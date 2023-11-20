package Node;

import FileProcess.MyFileWriter;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_GEP;
import LLVM_IR.Instruction.Instruction_Load;
import LLVM_IR.Instruction.Instruction_Store;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypePointer;
import LLVM_IR.Structure.Value;
import LLVM_IR.SymbolTable;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;

// 语句 ForStmt → LVal '=' Exp
public class ForStmt extends Node{
    private LVal lVal;
    private Token assign;
    private Exp exp;

    public ForStmt(LVal lVal, Token assign, Exp exp) {
        this.lVal = lVal;
        this.assign = assign;
        this.exp = exp;
    }

    public LVal getlVal() {
        return lVal;
    }

    public Exp getExp() {
        return exp;
    }

    public void writeNode() {
        lVal.writeNode();
        MyFileWriter.write(assign.getWholeToken());
        exp.writeNode();
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.ForStmt));
    }

    public static ForStmt makeForStmt() {
        LVal lVal = LVal.makeLVal();
        Token assign = Parser.checkCategory("ASSIGN");
        Exp exp = Exp.makeExp();
        return new ForStmt(lVal, assign, exp);
    }

    public static void forStmtErrorHandler(ForStmt forStmt) {
        LVal.lValErrorHandler(forStmt.lVal);
        Exp.expErrorHandler(forStmt.exp);
    }

    public static void forStmtLLVMBuilder(ForStmt forStmt) {
        if(forStmt.lVal.getExpArrayList().isEmpty()) {
            Value lVal = SymbolTable.getValSymbol(forStmt.lVal.getIdent().getToken());
            Exp.expLLVMBuilder(forStmt.exp);
            Instruction_Store store = new Instruction_Store(BuilderAttribute.curTempValue, lVal);
            store.addInstructionInBlock(BuilderAttribute.currentBlock);
            BuilderAttribute.curTempValue = store;
        }
        else {
            // ARRAY
            ArrayList<Value> list = new ArrayList<>();
            for(Exp exp : forStmt.lVal.getExpArrayList()) {
                Exp.expLLVMBuilder(exp);
                list.add(BuilderAttribute.curTempValue);
            }
            String ident = forStmt.lVal.getIdent().getToken();
            BuilderAttribute.curTempValue = SymbolTable.getValSymbol(ident);
            Type type = BuilderAttribute.curTempValue.getType();
            Type targetType = ((TypePointer) type).getType();
            if(targetType instanceof TypePointer) {
                // func f param like a[][1]
                Instruction_Load load = new Instruction_Load(BuilderAttribute.curTempValue);
                load.addInstructionInBlock(BuilderAttribute.currentBlock);
                BuilderAttribute.curTempValue = load;
            }
            else {
                // a[1][1]
                list.add(0, BuilderAttribute.zero);
            }
            Instruction_GEP gep = new Instruction_GEP(BuilderAttribute.curTempValue, list);
            gep.addInstructionInBlock(BuilderAttribute.currentBlock);
            Exp.expLLVMBuilder(forStmt.exp);
            Instruction_Store store = new Instruction_Store(BuilderAttribute.curTempValue, gep);
            store.addInstructionInBlock(BuilderAttribute.currentBlock);
            BuilderAttribute.curTempValue = store;
        }
    }
}
