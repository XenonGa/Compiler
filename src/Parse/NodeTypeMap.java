package Parse;

import Node.NodeType;

import java.util.HashMap;
import java.util.Map;

public class NodeTypeMap {
    public static Map<NodeType, String> nodeTypeMap = new HashMap<NodeType, String>(){{
        put(NodeType.CompUnit, "<CompUnit>");
        put(NodeType.Decl, "<Decl>");
        put(NodeType.ConstDecl, "<ConstDecl>");
        put(NodeType.BType, "<BType>");
        put(NodeType.ConstDef, "<ConstDef>");
        put(NodeType.ConstInitVal, "<ConstInitVal>");
        put(NodeType.VarDecl, "<VarDecl>");
        put(NodeType.VarDef, "<VarDef>");
        put(NodeType.InitVal, "<InitVal>");
        put(NodeType.FuncDef, "<FuncDef>");
        put(NodeType.MainFuncDef, "<MainFuncDef>");
        put(NodeType.FuncType, "<FuncType>");
        put(NodeType.FuncFParams, "<FuncFParams>");
        put(NodeType.FuncFParam, "<FuncFParam>");
        put(NodeType.Block, "<Block>");
        put(NodeType.BlockItem, "<BlockItem>");
        put(NodeType.Stmt, "<Stmt>");
        put(NodeType.Exp, "<Exp>");
        put(NodeType.Cond, "<Cond>");
        put(NodeType.LVal, "<LVal>");
        put(NodeType.PrimaryExp, "<PrimaryExp>");
        put(NodeType.Number, "<Number>");
        put(NodeType.UnaryExp, "<UnaryExp>");
        put(NodeType.UnaryOp, "<UnaryOp>");
        put(NodeType.FuncRParams, "<FuncRParams>");
        put(NodeType.MulExp, "<MulExp>");
        put(NodeType.AddExp, "<AddExp>");
        put(NodeType.RelExp, "<RelExp>");
        put(NodeType.EqExp, "<EqExp>");
        put(NodeType.LAndExp, "<LAndExp>");
        put(NodeType.LOrExp, "<LOrExp>");
        put(NodeType.ConstExp, "<ConstExp>");
    }};
}
