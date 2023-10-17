package Node;

import Parse.Parser;

import java.util.Objects;

// 声明 Decl → ConstDecl | VarDecl
public class Decl extends Node {
    private ConstDecl constDecl;
    private VarDecl varDecl;

    public Decl(ConstDecl constDecl) {
        this.constDecl = constDecl;
    }

    public Decl(VarDecl varDecl) {
        this.varDecl = varDecl;
    }

    public ConstDecl getConstDecl() {
        return constDecl;
    }

    public VarDecl getVarDecl() {
        return varDecl;
    }

    public void writeNode() {
        if (constDecl != null) constDecl.writeNode();
        else varDecl.writeNode();
    }

    public static Decl makeDecl() {
        if(checkCurrentTokenCategory("CONSTTK")){
            ConstDecl constDecl = ConstDecl.makeConstDecl();
            return new Decl(constDecl);
        }
        else {
            VarDecl varDecl = VarDecl.makeVarDecl();
            return new Decl(varDecl);
        }
    }

    public static void DeclErrorHandler(Decl decl) {
        if(decl.constDecl != null) {
            ConstDecl.constDeclErrorHandler(decl.constDecl);
        }
        else {
            VarDecl.varDeclErrorHandler(decl.varDecl);
        }
    }

    public static void declLLVMBuilder(Decl decl) {
        if(decl.constDecl != null) {
            ConstDecl.constDeclLLVMBuilder(decl.constDecl);
        }
        else {
            VarDecl.varDeclLLVMBuilder(decl.varDecl);
        }
    }
}
