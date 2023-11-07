package Node;

import ErrorHandler.*;
import FileProcess.MyFileWriter;
import Identifier.*;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.*;
import LLVM_IR.Structure.ConstNum;
import LLVM_IR.Structure.Function;
import LLVM_IR.Structure.FunctionParameter;
import LLVM_IR.Structure.Value;
import LLVM_IR.SymbolTable;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

//语句 Stmt → LVal '=' Exp ';' // 每种类型的语句都要覆盖
//        | [Exp] ';' //有无Exp两种情况
//        | Block
//        | 'if' '(' Cond ')' Stmt [ 'else' Stmt ] // 1.有else 2.无else
//        | 'for' '(' [ForStmt] ';' [Cond] ';' [forStmt] ')' Stmt
//        | 'break' ';' | 'continue' ';'
//        | 'return' [Exp] ';' // 1.有Exp 2.无Exp
//        | LVal '=' 'getint''('')'';'
//        | 'printf''('FormatString{','Exp}')'';' // 1.有Exp 2.无Exp
public class Stmt extends Node{
    private StmtType stmtType;
    private LVal lVal;
    private Token assign;
    private Exp exp;
    private Token semicolon;
    private ArrayList<Token> semicolonArrayList;
    private Block block;
    private Token ifTK;
    private Token leftParent;
    private Cond cond;
    private Token rightParent;
    private ArrayList<Stmt> stmtArrayList;
    private Token elseTK;
    private Token forTK;
    private ForStmt forStmt1;
    private ForStmt forStmt2;

    private Token breakTK;
    private Token continueTK;
    private Token returnTK;
    private Token getintTK;
    private Token printfTK;
    private Token formatString;
    private ArrayList<Token> commaArrayList;
    private ArrayList<Exp> expArrayList;

    public Stmt(StmtType stmtType, LVal lVal, Token assign, Exp exp, Token semicolon) {
        this.stmtType = stmtType;
        this.lVal = lVal;
        this.assign = assign;
        this.exp = exp;
        this.semicolon = semicolon;
    }

    public Stmt(StmtType stmtType, Exp exp, Token semicolon) {
        this.stmtType = stmtType;
        this.exp = exp;
        this.semicolon = semicolon;
    }

    public Stmt(StmtType stmtType, Block block) {
        this.stmtType = stmtType;
        this.block = block;
    }

    public Stmt(StmtType stmtType, Token ifTK, Token leftParent, Cond cond, Token rightParent, ArrayList<Stmt> stmtArrayList, Token elseTK) {
        this.stmtType = stmtType;
        this.ifTK = ifTK;
        this.leftParent = leftParent;
        this.cond = cond;
        this.rightParent = rightParent;
        this.stmtArrayList = stmtArrayList;
        this.elseTK = elseTK;
    }

    public Stmt(StmtType stmtType, ArrayList<Token> semicolonArrayList, Token leftParent, Cond cond, Token rightParent, Token forTK, ForStmt forStmt1, ForStmt forStmt2, ArrayList<Stmt> stmtArrayList) {
        this.stmtType = stmtType;
        this.semicolonArrayList = semicolonArrayList;
        this.leftParent = leftParent;
        this.cond = cond;
        this.rightParent = rightParent;
        this.forTK = forTK;
        this.forStmt1 = forStmt1;
        this.forStmt2 = forStmt2;
        this.stmtArrayList = stmtArrayList;
    }

    public Stmt(StmtType stmtType, Token semicolon, Token breakOrContinueTK) {
        if(stmtType == StmtType.Break) {
            this.stmtType = stmtType;
            this.semicolon = semicolon;
            this.breakTK = breakOrContinueTK;
        }
        else if(stmtType == StmtType.Continue) {
            this.stmtType = stmtType;
            this.semicolon = semicolon;
            this.continueTK = breakOrContinueTK;
        }
    }

    public Stmt(StmtType stmtType, Exp exp, Token semicolon, Token returnTK) {
        this.stmtType = stmtType;
        this.exp = exp;
        this.semicolon = semicolon;
        this.returnTK = returnTK;
    }

    public Stmt(StmtType stmtType, LVal lVal, Token assign, Token semicolon, Token leftParent, Token rightParent, Token getintTK) {
        this.stmtType = stmtType;
        this.lVal = lVal;
        this.assign = assign;
        this.semicolon = semicolon;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.getintTK = getintTK;
    }

    public Stmt(StmtType stmtType, Token semicolon, Token leftParent, Token rightParent, Token printfTK, Token formatString, ArrayList<Token> commaArrayList, ArrayList<Exp> expArrayList) {
        this.stmtType = stmtType;
        this.semicolon = semicolon;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
        this.printfTK = printfTK;
        this.formatString = formatString;
        this.commaArrayList = commaArrayList;
        this.expArrayList = expArrayList;
    }

    public StmtType getStmtType() {
        return stmtType;
    }

    public LVal getlVal() {
        return lVal;
    }

    public Exp getExp() {
        return exp;
    }

    public Block getBlock() {
        return block;
    }

    public Cond getCond() {
        return cond;
    }

    public ArrayList<Stmt> getStmtArrayList() {
        return stmtArrayList;
    }

    public ForStmt getForStmt1() {
        return forStmt1;
    }
    public ForStmt getForStmt2() {
        return forStmt2;
    }

    public ArrayList<Exp> getExpArrayList() {
        return expArrayList;
    }

    public Token getReturnTK() {
        return returnTK;
    }

    public void writeNode() {
        switch (stmtType) {
            case LVal_Assign_Exp -> {
                lVal.writeNode();
                MyFileWriter.write(assign.getWholeToken());
                exp.writeNode();
                MyFileWriter.write(semicolon.getWholeToken());
            }
            case Exp -> {
                if(exp != null) exp.writeNode();
                MyFileWriter.write(semicolon.getWholeToken());
            }
            case Block -> block.writeNode();
            case If -> {
                MyFileWriter.write(ifTK.getWholeToken());
                MyFileWriter.write(leftParent.getWholeToken());
                cond.writeNode();
                MyFileWriter.write(rightParent.getWholeToken());
                stmtArrayList.get(0).writeNode();
                if(elseTK != null) {
                    MyFileWriter.write(elseTK.getWholeToken());
                    stmtArrayList.get(1).writeNode();
                }
            }
            case For -> {
                MyFileWriter.write(forTK.getWholeToken());
                MyFileWriter.write(leftParent.getWholeToken());
                if(forStmt1 != null) {
                    forStmt1.writeNode();
                }
                MyFileWriter.write(semicolonArrayList.get(0).getWholeToken());
                if(cond != null) {
                    cond.writeNode();
                }
                MyFileWriter.write(semicolonArrayList.get(0).getWholeToken());
                if(forStmt2 != null) {
                   forStmt2.writeNode();
                }
                MyFileWriter.write(rightParent.getWholeToken());
                stmtArrayList.get(0).writeNode();
            }
            case Break -> {
                MyFileWriter.write(breakTK.getWholeToken());
                MyFileWriter.write(semicolon.getWholeToken());
            }
            case Continue -> {
                MyFileWriter.write(continueTK.getWholeToken());
                MyFileWriter.write(semicolon.getWholeToken());
            }
            case Return -> {
                MyFileWriter.write(returnTK.getWholeToken());
                if(exp != null) {
                    exp.writeNode();
                }
                MyFileWriter.write(semicolon.getWholeToken());
            }
            case LVal_Assign_Getint -> {
                lVal.writeNode();
                MyFileWriter.write(assign.getWholeToken());
                MyFileWriter.write(getintTK.getWholeToken());
                MyFileWriter.write(leftParent.getWholeToken());
                MyFileWriter.write(rightParent.getWholeToken());
                MyFileWriter.write(semicolon.getWholeToken());
            }
            case Printf -> {
                MyFileWriter.write(printfTK.getWholeToken());
                MyFileWriter.write(leftParent.getWholeToken());
                MyFileWriter.write(formatString.getWholeToken());
                if(!expArrayList.isEmpty()) {
                    for(int i = 0; i < expArrayList.size(); i++) {
                        MyFileWriter.write(commaArrayList.get(i).getWholeToken());
                        expArrayList.get(i).writeNode();
                    }
                }
                MyFileWriter.write(rightParent.getWholeToken());
                MyFileWriter.write(semicolon.getWholeToken());
            }
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.Stmt));
    }

    public static Stmt makeStmt() {
        if(checkCurrentTokenCategory("IDENFR")) {
            int assignNum = Parser.index;
            for(int i  = Parser.index; i < Parser.tokenArrayList.size() &&
                    Parser.tokenArrayList.get(i).getLineNumber() ==
                            Parser.currentToken.getLineNumber(); i++) {
                if(Objects.equals(Parser.tokenArrayList.get(i).getCategory(), "ASSIGN")) {
                    assignNum = i;
                }
            }
            if(assignNum > Parser.index) {
                // LVal
                LVal lVal = LVal.makeLVal();
                Token assign = Parser.checkCategory("ASSIGN");
                if (!checkCurrentTokenCategory("GETINTTK")) {
                    // LVal '=' Exp ';'
                    Exp exp = Exp.makeExp();
                    Token semicolon = Parser.checkCategory("SEMICN");
                    return new Stmt(StmtType.LVal_Assign_Exp, lVal, assign, exp, semicolon);
                } else {
                    Token getInt = Parser.checkCategory("GETINTTK");
                    Token leftParent = Parser.checkCategory("LPARENT");
                    Token rightParent = Parser.checkCategory("RPARENT");
                    Token semicolon = Parser.checkCategory("SEMICN");
                    return new Stmt(StmtType.LVal_Assign_Getint, lVal, assign, semicolon, leftParent, rightParent, getInt);
                }
            }
            else {
                Exp exp = Exp.makeExp();;
                Token semicolon = Parser.checkCategory("SEMICN");
                return new Stmt(StmtType.Exp, exp, semicolon);
            }
        }
        else if(checkCurrentTokenCategory("LBRACE")) {
            // Block
            Block block = Block.makeBlock();
            return new Stmt(StmtType.Block, block);
        }
        else if(checkCurrentTokenCategory("IFTK")) {
            ArrayList<Stmt> stmtList = new ArrayList<>();

            Token ifTK = Parser.checkCategory("IFTK");
            Token leftParent = Parser.checkCategory("LPARENT");
            Cond cond = Cond.makeCond();
            Token rightParent = Parser.checkCategory("RPARENT");
            Token elseTK = null;
            stmtList.add(makeStmt());
            if(checkCurrentTokenCategory("ELSETK")) {
                elseTK = Parser.checkCategory("ELSETK");
                stmtList.add(makeStmt());
            }
            return new Stmt(StmtType.If, ifTK, leftParent, cond, rightParent, stmtList, elseTK);
        }
        else if(checkCurrentTokenCategory("FORTK")) {
            Token forTK = Parser.checkCategory("FORTK");
            Token leftParent = Parser.checkCategory("LPARENT");
            ForStmt forStmt1 = null;
            ForStmt forStmt2 = null;
            ArrayList<Token> semicolons = new ArrayList<>();
            ArrayList<Stmt> stmtArrayList1 = new ArrayList<>();
            Cond cond = null;
            if(!checkCurrentTokenCategory("SEMICN")) {
                forStmt1 = ForStmt.makeForStmt();
            }
            semicolons.add(Parser.checkCategory("SEMICN"));
            if(!checkCurrentTokenCategory("SEMICN")) {
                cond = Cond.makeCond();
            }
            semicolons.add(Parser.checkCategory("SEMICN"));
            if(!checkCurrentTokenCategory("RPARENT")) {
                forStmt2 = ForStmt.makeForStmt();
            }
            Token rightParent = Parser.checkCategory("RPARENT");
            stmtArrayList1.add(makeStmt());
            return new Stmt(StmtType.For, semicolons, leftParent, cond, rightParent, forTK, forStmt1, forStmt2, stmtArrayList1);
        }
        else if(checkCurrentTokenCategory("BREAKTK")) {
            Token breakTK = Parser.checkCategory("BREAKTK");
            Token semicolon = Parser.checkCategory("SEMICN");
            return new Stmt(StmtType.Break, semicolon, breakTK);
        }
        else if(checkCurrentTokenCategory("CONTINUETK")) {
            Token continueTK = Parser.checkCategory("CONTINUETK");
            Token semicolon = Parser.checkCategory("SEMICN");
            return new Stmt(StmtType.Continue, semicolon, continueTK);
        }
        else if(checkCurrentTokenCategory("RETURNTK")) {
            Token returnTK = Parser.checkCategory("RETURNTK");
            Exp exp = null;
            if(!checkCurrentTokenCategory("SEMICN")) {
                exp = Exp.makeExp();
            }
            Token semicolon = Parser.checkCategory("SEMICN");
            return new Stmt(StmtType.Return, exp, semicolon, returnTK);
        }
        else if(checkCurrentTokenCategory("PRINTFTK")) {
            Token printfTK = Parser.checkCategory("PRINTFTK");
            Token leftParent = Parser.checkCategory("LPARENT");
            Token formatString = Parser.checkCategory("STRCON");

            ArrayList<Token> commas = new ArrayList<>();
            ArrayList<Exp> exps = new ArrayList<>();
            while(checkCurrentTokenCategory("COMMA")) {
                commas.add(Parser.checkCategory("COMMA"));
                exps.add(Exp.makeExp());
            }
            Token rightParent = Parser.checkCategory("RPARENT");
            Token semicolon = Parser.checkCategory("SEMICN");
            return new Stmt(StmtType.Printf, semicolon, leftParent, rightParent, printfTK, formatString, commas, exps);
        }
        else {
                // [Exp] ';'
                Exp exp = null;
                if(!checkCurrentTokenCategory("SEMICN")) {
                    exp = Exp.makeExp();
                }
                Token semicolon = Parser.checkCategory("SEMICN");
                return new Stmt(StmtType.Exp, exp, semicolon);
        }
    }

    public static void stmtErrorHandler(Stmt stmt) {
        switch (stmt.stmtType) {
            case LVal_Assign_Exp -> {
                LVal.lValErrorHandler(stmt.lVal);
                Identifier lValIdent = ErrorHandler.getIdentifier(stmt.lVal.getIdent().getToken());
                if(lValIdent instanceof ValIdent val) {
                    if(val.isConstFlag()) {
                        MyError error = new MyError("h", stmt.lVal.getIdent().getLineNumber());
                        ErrorHandler.addNewError(error);
                    }
                }
                Exp.expErrorHandler(stmt.exp);
            }
            case Exp -> {
                if(stmt.exp != null) Exp.expErrorHandler(stmt.exp);
            }
            case Block -> {
                ErrorHandler.pushSymbolTable(false,null);
                Block.blockErrorHandler(stmt.block);
                ErrorHandler.popSymbolTable();
            }
            case If -> {
                Cond.condErrorHandler(stmt.cond);
                stmtErrorHandler(stmt.stmtArrayList.get(0));
                if(stmt.stmtArrayList.size() > 1) {
                    stmtErrorHandler(stmt.stmtArrayList.get(1));
                }
            }
            case For -> {
                if(stmt.forStmt1 != null) {
                    ForStmt.forStmtErrorHandler(stmt.forStmt1);
                }
                if(stmt.cond != null) {
                    Cond.condErrorHandler(stmt.cond);
                }
                if(stmt.forStmt2 != null) {
                    ForStmt.forStmtErrorHandler(stmt.forStmt2);
                }
                ErrorHandler.forFlag += 1;
                stmtErrorHandler(stmt.stmtArrayList.get(0));
                ErrorHandler.forFlag -= 1;
            }
            case Break -> {
                if(ErrorHandler.forFlag == 0) {
                    MyError error = new MyError("m", stmt.breakTK.getLineNumber());
                    ErrorHandler.addNewError(error);
                }
            }
            case Continue -> {
                if(ErrorHandler.forFlag == 0) {
                    MyError error = new MyError("m", stmt.continueTK.getLineNumber());
                    ErrorHandler.addNewError(error);
                }
            }
            case Return -> {
                if(ErrorHandler.isInFunction()) {
                    if(stmt.exp != null) {
                        if(Objects.equals(ErrorHandler.getFunctionType(), "void")) {
                            MyError error = new MyError("f", stmt.returnTK.getLineNumber());
                            ErrorHandler.addNewError(error);
                        }
                        Exp.expErrorHandler(stmt.exp);
                    }
                }
            }
            case LVal_Assign_Getint -> {
                LVal.lValErrorHandler(stmt.lVal);
                Identifier lValIdent = ErrorHandler.getIdentifier(stmt.lVal.getIdent().getToken());
                if(lValIdent instanceof ValIdent val) {
                    if(val.isConstFlag()) {
                        MyError error = new MyError("h", stmt.lVal.getIdent().getLineNumber());
                        ErrorHandler.addNewError(error);
                    }
                }
            }
            case Printf -> {
                int expNum = stmt.expArrayList.size();
                int valNum = 0;
                String formatString = stmt.formatString.getToken();
                for(int i = 0; i < formatString.length(); i++) {
                    if(formatString.charAt(i) == '%') {
                        if(formatString.charAt(i + 1) == 'd') {
                            valNum = valNum + 1;
                        }
                    }
                }
                if(expNum != valNum) {
                    MyError error = new MyError("l", stmt.printfTK.getLineNumber());
                    ErrorHandler.addNewError(error);
                }
                for(Exp exp : stmt.expArrayList) {
                    Exp.expErrorHandler(exp);
                }
            }
        }
    }

    public static void stmtLLVMBuilder(Stmt stmt) {
            switch (stmt.stmtType) {
                case Return -> {
                    if(stmt.exp != null) {
                        Exp.expLLVMBuilder(stmt.exp);
                        Instruction_Ret.makeReturnInst(BuilderAttribute.currentBlock, BuilderAttribute.curTempValue);
                    }
                    else {
                        Instruction_Ret.makeReturnInst(BuilderAttribute.currentBlock);
                    }
                }
                case LVal_Assign_Exp -> {
                    if(stmt.lVal.getExpArrayList().isEmpty()) {
                        Value lVal = SymbolTable.getValSymbol(stmt.lVal.getIdent().getToken());
                        Exp.expLLVMBuilder(stmt.exp);
                        BuilderAttribute.curTempValue = new Instruction_Store(BuilderAttribute.curTempValue, lVal);
                    }
                    else {
                        // TODO ARRAY
                    }
                }
                case Exp -> {
                    if(stmt.exp != null) {
                        Exp.expLLVMBuilder(stmt.exp);
                    }
                }
                case Block -> {
                    SymbolTable.pushLLVMSymbolTable();
                    Block.blockLLVMBuilder(stmt.block);
                    SymbolTable.popLLVMSymbolTable();
                }
                case LVal_Assign_Getint -> {
                    if(stmt.lVal.getExpArrayList().isEmpty()) {
                        Value lVal = SymbolTable.getValSymbol(stmt.lVal.getIdent().getToken());

                        Function getInt = (Function) SymbolTable.getValSymbol("getint");
                        Instruction_Call call = new Instruction_Call(getInt, new ArrayList<>());
                        call.addInstructionInBlock(BuilderAttribute.currentBlock);
                        BuilderAttribute.curTempValue = call;

                        Instruction_Store store = new Instruction_Store(BuilderAttribute.curTempValue, lVal);
                        store.addInstructionInBlock(BuilderAttribute.currentBlock);
                    }
                    else {
                        // TODO ARRAY
                    }
                }
                case Printf -> {
                    String formatString = stmt.formatString.getToken();
                    formatString = formatString.replace("\\n", "\n");
                    formatString = formatString.replace("\"", "");
                    ArrayList<Value> params = new ArrayList<>();
                    for(Exp exp : stmt.expArrayList) {
                        Exp.expLLVMBuilder(exp);
                        params.add(BuilderAttribute.curTempValue);
                    }
                    for(int i = 0; i < formatString.length(); i++) {
                        if(formatString.charAt(i) == '%') {

                            ArrayList<Value> callFuncParams = new ArrayList<>();
                            callFuncParams.add(params.remove(0));

                            Function getInt = (Function) SymbolTable.getValSymbol("putint");
                            assert getInt != null;

                            Instruction_Call call = new Instruction_Call(getInt, callFuncParams);
                            call.addInstructionInBlock(BuilderAttribute.currentBlock);
                            i = i + 1;
                        }
                        int index = i;
                        while(index < formatString.length() && formatString.charAt(index) == '%') {
                            index = index + 1;
                        }
                        String s = formatString.substring(i, index);
                        if(s.length() == 1) {
                            ArrayList<Value> callFuncParams = new ArrayList<>();
                            ConstNum charNum = new ConstNum(s.charAt(0));
                            callFuncParams.add(charNum);

                            Function putChar = (Function) SymbolTable.getValSymbol("putch");
                            assert putChar != null;

                            Instruction_Call call = new Instruction_Call(putChar, callFuncParams);
                            call.addInstructionInBlock(BuilderAttribute.currentBlock);
                        }
                        else {
                            SymbolTable.getValSymbol()
                            Value strAddress = new Instruction_GEP()
                        }
                    }
                }
            }
    }
}
