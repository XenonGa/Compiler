package Parse;

import Node.NodeType;

import java.util.HashMap;
import java.util.Map;

public class NodeTypeMap {
    public static Map<NodeType, String> nodeTypeMap = new HashMap<NodeType, String>(){{
        put(NodeType.CompUnit, "<CompUnit>\n");
        put(NodeType.Decl, "<Decl>\n");
        put(NodeType.ConstDecl, "<ConstDecl>\n");
        put(NodeType.BType, "<BType>\n");
        put(NodeType.ConstDef, "<ConstDef>\n");
        put(NodeType.ConstInitVal, "<ConstInitVal>\n");
        put(NodeType.VarDecl, "<VarDecl>\n");
        put(NodeType.VarDef, "<VarDef>\n");
        put(NodeType.InitVal, "<InitVal>\n");
        put(NodeType.FuncDef, "<FuncDef>\n");
        put(NodeType.MainFuncDef, "<MainFuncDef>\n");
        put(NodeType.FuncType, "<FuncType>\n");
        put(NodeType.FuncFParams, "<FuncFParams>\n");
        put(NodeType.FuncFParam, "<FuncFParam>\n");
        put(NodeType.Block, "<Block>\n");
        put(NodeType.BlockItem, "<BlockItem>\n");
        put(NodeType.Stmt, "<Stmt>\n");
        put(NodeType.Exp, "<Exp>\n");
        put(NodeType.Cond, "<Cond>\n");
        put(NodeType.LVal, "<LVal>\n");
        put(NodeType.PrimaryExp, "<PrimaryExp>\n");
        put(NodeType.Number, "<Number>\n");
        put(NodeType.UnaryExp, "<UnaryExp>\n");
        put(NodeType.UnaryOp, "<UnaryOp>\n");
        put(NodeType.FuncRParams, "<FuncRParams>\n");
        put(NodeType.MulExp, "<MulExp>\n");
        put(NodeType.AddExp, "<AddExp>\n");
        put(NodeType.RelExp, "<RelExp>\n");
        put(NodeType.EqExp, "<EqExp>\n");
        put(NodeType.LAndExp, "<LAndExp>\n");
        put(NodeType.LOrExp, "<LOrExp>\n");
        put(NodeType.ConstExp, "<ConstExp>\n");
        put(NodeType.ForStmt, "<ForStmt>\n");
    }};
}
