package Node;

import FileProcess.MyFileWriter;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.lang.reflect.Array;
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
    private ArrayList<ForStmt> forStmtArrayList;
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

    public Stmt(StmtType stmtType, ArrayList<Token> semicolonArrayList, Token leftParent, Cond cond, Token rightParent, Token forTK, ArrayList<ForStmt> forStmtArrayList) {
        this.stmtType = stmtType;
        this.semicolonArrayList = semicolonArrayList;
        this.leftParent = leftParent;
        this.cond = cond;
        this.rightParent = rightParent;
        this.forTK = forTK;
        this.forStmtArrayList = forStmtArrayList;
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

    public ArrayList<ForStmt> getForStmtArrayList() {
        return forStmtArrayList;
    }

    public ArrayList<Exp> getExpArrayList() {
        return expArrayList;
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
                if(!forStmtArrayList.isEmpty()) {
                    forStmtArrayList.get(0).writeNode();
                }
                MyFileWriter.write(semicolonArrayList.get(0).getWholeToken());
                if(cond != null) {
                    cond.writeNode();
                }
                MyFileWriter.write(semicolonArrayList.get(0).getWholeToken());
                if(forStmtArrayList.size() > 1) {
                    forStmtArrayList.get(1).writeNode();
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
        if(Objects.equals(Parser.currentToken.getCategory(), "LBRACE")) {
            // Block
            Block block = Block.makeBlock();
            return new Stmt(StmtType.Block, block);
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "IFTK")) {
            ArrayList<Stmt> stmtList = new ArrayList<>();

            Token ifTK = Parser.checkCategory("IFTK");
            Token leftParent = Parser.checkCategory("LPARENT");
            Cond cond = Cond.makeCond();
            Token rightParent = Parser.checkCategory("RPARENT");
            Token elseTK = null;
            stmtList.add(makeStmt());
            if(Objects.equals(Parser.currentToken.getCategory(), "ELSETK")) {
                elseTK = Parser.checkCategory("ELSETK");
                stmtList.add(makeStmt());
            }
            return new Stmt(StmtType.If, ifTK, leftParent, cond, rightParent, stmtList, elseTK);
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "FORTK")) {
            Token forTK = Parser.checkCategory("FORTK");
            Token leftParent = Parser.checkCategory("LPARENT");
            ArrayList<ForStmt> forStmts = new ArrayList<>();
            ArrayList<Token> semicolons = new ArrayList<>();
            Cond cond = null;
            if(!Objects.equals(Parser.currentToken.getCategory(), "SEMICN")) {
                forStmts.add(ForStmt.makeForStmt());
            }
            semicolons.add(Parser.checkCategory("SEMICN"));
            if(!Objects.equals(Parser.currentToken.getCategory(), "SEMICN")) {
                cond = Cond.makeCond();
            }
            semicolons.add(Parser.checkCategory("SEMICN"));
            if(!Objects.equals(Parser.currentToken.getCategory(), "RPARENT")) {
                forStmts.add(ForStmt.makeForStmt());
            }
            Token rightParent = Parser.checkCategory("RPARENT");
            return new Stmt(StmtType.For, semicolons, leftParent, cond, rightParent, forTK, forStmts);
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "BREAKTK")) {
            Token breakTK = Parser.checkCategory("BREAKTK");
            Token semicolon = Parser.checkCategory("SEMICN");
            return new Stmt(StmtType.Break, breakTK, semicolon);
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "CONTINUETK")) {
            Token continueTK = Parser.checkCategory("CONTINUETK");
            Token semicolon = Parser.checkCategory("SEMICN");
            return new Stmt(StmtType.Continue, continueTK, semicolon);
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "RETURNTK")) {
            Token returnTK = Parser.checkCategory("RETURNTK");
            Exp exp = null;
            if(!Objects.equals(Parser.currentToken.getCategory(), "SEMICN")) {
                exp = Exp.makeExp();
            }
            Token semicolon = Parser.checkCategory("SEMICN");
            return new Stmt(StmtType.Return, exp, semicolon, returnTK);
        }
        else if(Objects.equals(Parser.currentToken.getCategory(), "PRINTFTK")) {
            Token printfTK = Parser.checkCategory("PRINTFTK");
            Token leftParent = Parser.checkCategory("LPARENT");
            Token formatString = Parser.checkCategory("STRCON");

            ArrayList<Token> commas = new ArrayList<>();
            ArrayList<Exp> exps = new ArrayList<>();
            while(Objects.equals(Parser.currentToken.getCategory(), "COMMA")) {
                commas.add(Parser.checkCategory("COMMA"));
                exps.add(Exp.makeExp());
            }
            Token rightParent = Parser.checkCategory("RPARENT");
            Token semicolon = Parser.checkCategory("SEMICN");
            return new Stmt(StmtType.Printf, semicolon, leftParent, rightParent, printfTK, formatString, commas, exps);
        }
        else {
            int assignNum = Parser.index;
            for(int i  = Parser.index; i < Parser.tokenArrayList.size() &&
                    Parser.tokenArrayList.get(i).getLineNumber() ==
                    Parser.currentToken.getLineNumber(); i++) {
                if(Objects.equals(Parser.tokenArrayList.get(i).getCategory(), "ASSIGN")) {
                    assignNum = i;
                }
            }
            if(assignNum != Parser.index) {
                // LVal
                LVal lVal = LVal.makeLVal();
                Token assign = Parser.checkCategory("ASSIGN");
                if(!Objects.equals(Parser.currentToken.getCategory(), "GETINTTK")) {
                    // LVal '=' Exp ';'
                    Exp exp = Exp.makeExp();
                    Token semicolon = Parser.checkCategory("SEMICN");
                    return new Stmt(StmtType.LVal_Assign_Exp, lVal, assign, exp, semicolon);
                }
                else {
                    Token getInt = Parser.checkCategory("GETINTTK");
                    Token leftParent = Parser.checkCategory("LPARENT");
                    Token rightParent = Parser.checkCategory("RPARENT");
                    Token semicolon = Parser.checkCategory("SEMICN");
                    return new Stmt(StmtType.LVal_Assign_Getint, lVal, assign, getInt, leftParent, rightParent, semicolon);
                }
            }
            else {
                // [Exp] ';'
                Exp exp = null;
                if(!Objects.equals(Parser.currentToken.getCategory(), "SEMICN")) {
                    exp = Exp.makeExp();
                }
                Token semicolon = Parser.checkCategory("SEMICN");
                return new Stmt(StmtType.Exp, exp, semicolon);
            }
        }
        }
}
