package Node;

import Parse.Parser;

import java.util.Objects;

// 语句块项 BlockItem → Decl | Stmt
public class BlockItem extends Node {
    private Decl decl;
    private Stmt stmt;

    public BlockItem(Decl decl) {
        this.decl = decl;
    }

    public BlockItem(Stmt stmt) {
        this.stmt = stmt;
    }

    public Decl getDecl() {
        return decl;
    }

    public Stmt getStmt() {
        return stmt;
    }

    public void writeNode() {
        if (decl != null) {
            decl.writeNode();
        } else {
            stmt.writeNode();
        }
    }

    public static BlockItem makeBlockItem() {
        if (checkCurrentTokenCategory("CONSTTK") || checkCurrentTokenCategory("INTTK")) {
            Decl decl = Decl.makeDecl();
            return new BlockItem(decl);
        } else {
            Stmt stmt = Stmt.makeStmt();
            return new BlockItem(stmt);
        }
    }

    public static void blockItemErrorHandler(BlockItem blockItem) {
        if(blockItem.decl != null) {
            Decl.DeclErrorHandler(blockItem.decl);
        }
        else {
            Stmt.stmtErrorHandler(blockItem.stmt);
        }
    }
}
