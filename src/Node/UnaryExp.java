package Node;

import ErrorHandler.*;
import FileProcess.MyFileWriter;
import Identifier.*;
import LLVM_IR.BuilderAttribute;
import LLVM_IR.Instruction.Instruction_Binary;
import LLVM_IR.Instruction.Instruction_Call;
import LLVM_IR.Structure.Function;
import LLVM_IR.SymbolTable;
import LexicalAnalysis.Token;
import Parse.NodeTypeMap;
import Parse.Parser;

import java.util.ArrayList;
import java.util.Objects;

// 一元表达式 UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
public class UnaryExp extends Node{
    private PrimaryExp primaryExp;
    private Token ident;
    private Token leftParent;
    private FuncRParams funcRParams;
    private Token rightParent;

    private UnaryOp unaryOp;
    private UnaryExp unaryExp;

    public UnaryExp(PrimaryExp primaryExp) {
        this.primaryExp = primaryExp;
    }

    public UnaryExp(Token ident, Token leftParent, Token rightParent) {
        this.ident = ident;
        this.leftParent = leftParent;
        this.rightParent = rightParent;
    }

    public UnaryExp(Token ident, Token leftParent, FuncRParams params, Token rightParent) {
        this.ident = ident;
        this.leftParent = leftParent;
        this.funcRParams = params;
        this.rightParent = rightParent;
    }

    public UnaryExp(UnaryOp unaryOp, UnaryExp unaryExp) {
        this.unaryOp = unaryOp;
        this.unaryExp = unaryExp;
    }

    public PrimaryExp getPrimaryExp() {
        return primaryExp;
    }

    public Token getIdent() {
        return ident;
    }

    public FuncRParams getFuncRParams() {
        return funcRParams;
    }

    public UnaryOp getUnaryOp() {
        return unaryOp;
    }

    public UnaryExp getUnaryExp() {
        return unaryExp;
    }

    public void writeNode() {
        if(primaryExp != null) {
            primaryExp.writeNode();
        }
        else if(ident != null) {
            MyFileWriter.write(ident.getWholeToken());
            MyFileWriter.write(leftParent.getWholeToken());
            if(funcRParams != null) {
                funcRParams.writeNode();
            }
            MyFileWriter.write(rightParent.getWholeToken());
        }
        else {
            unaryOp.writeNode();
            unaryExp.writeNode();
        }
        MyFileWriter.write(NodeTypeMap.nodeTypeMap.get(NodeType.UnaryExp));
    }

    public static UnaryExp makeUnaryExp() {
        if(checkCurrentTokenCategory("IDENFR") && Objects.equals(Parser.tokenArrayList.get(Parser.index + 1).getCategory(), "LPARENT")) {
            Token identifier = Parser.checkCategory("IDENFR");
            Token leftParent = Parser.checkCategory("LPARENT");
            FuncRParams funcRParams1 = null;
            if(!checkCurrentTokenCategory("RPARENT")) {
                funcRParams1 = FuncRParams.makeFuncParams();
            }
            Token rightParent = Parser.checkCategory("RPARENT");
            return new UnaryExp(identifier, leftParent, funcRParams1, rightParent);
        }
        else if(checkCurrentTokenCategory( "PLUS") || checkCurrentTokenCategory("MINU") || checkCurrentTokenCategory("NOT")) {
            UnaryOp unaryOp1 = UnaryOp.makeUnaryOp();
            UnaryExp unaryExp1 = UnaryExp.makeUnaryExp();
            return new UnaryExp(unaryOp1, unaryExp1);
        }
        else {
            PrimaryExp primaryExp1 = PrimaryExp.makePrimaryExp();
            return new UnaryExp(primaryExp1);
        }
    }

    public static void unaryExpErrorHandler(UnaryExp unaryExp) {
        if(unaryExp.primaryExp != null) {
            PrimaryExp.primaryExpErrorHandler(unaryExp.primaryExp);
        }
        else if(unaryExp.unaryExp != null) {
            unaryExpErrorHandler(unaryExp.unaryExp);
        }
        else {
            // define checking
            if(!ErrorHandler.isDeclared(unaryExp.ident.getToken())) {
                MyError error = new MyError("c", unaryExp.ident.getLineNumber());
                ErrorHandler.addNewError(error);
                return;
            }

            // is function checking
            Identifier ident = ErrorHandler.getIdentifier(unaryExp.ident.getToken());
            if(!(ident instanceof FuncIdent funcIdent)) {
                MyError error = new MyError("e", unaryExp.ident.getLineNumber());
                ErrorHandler.addNewError(error);
                return;
            }

            // params consistency checking
            if(unaryExp.funcRParams == null && !funcIdent.getFuncParams().isEmpty()) {
                // params num consistency checking
                MyError error = new MyError("d", unaryExp.ident.getLineNumber());
                ErrorHandler.addNewError(error);
            }
            if(unaryExp.funcRParams != null) {
                // params num consistency checking
                if(funcIdent.getFuncParams().size() != unaryExp.funcRParams.getExpArrayList().size()) {
                    MyError error = new MyError("d", unaryExp.ident.getLineNumber());
                    ErrorHandler.addNewError(error);
                }

                // params dimensions consistency checking
                ArrayList<Integer> funcFParamsDim = new ArrayList<>();
                ArrayList<Integer> funcRParamsDim = new ArrayList<>();

                ArrayList<FuncParam> funcParams = funcIdent.getFuncParams();
                for(FuncParam funcParam : funcParams) {
                    funcFParamsDim.add(funcParam.getFuncParamDimension());
                }
                if(unaryExp.funcRParams != null) {
                    FuncRParams.funcRParamsErrorHandler(unaryExp.funcRParams);
                    ArrayList<Exp> expList = unaryExp.funcRParams.getExpArrayList();
                    for(Exp exp : expList) {
                        FuncParam funcParam = Exp.getFuncParamFromExp(exp);
                        if(funcParam != null) {
                            if(funcParam.getFuncParamName() != null) {
                                Identifier identifier = ErrorHandler.getIdentifier(funcParam.getFuncParamName());
                                if(identifier instanceof ValIdent valIdent) {
                                    funcRParamsDim.add(valIdent.getValDimension() - funcParam.getFuncParamDimension());
                                }
                                else if(identifier instanceof FuncIdent funcIdent1) {
                                    if(Objects.equals(funcIdent1.getFuncType(), "void")) {
                                        funcRParamsDim.add(-1);
                                    }
                                    else {
                                        funcRParamsDim.add(0);
                                    }

                                }
                            }
                            else {
                                funcRParamsDim.add(funcParam.getFuncParamDimension());
                            }
                        }
                    }
                }
                if(!Objects.equals(funcFParamsDim, funcRParamsDim)) {
                    MyError error = new MyError("e", unaryExp.ident.getLineNumber());
                    ErrorHandler.addNewError(error);
                }
            }
        }
    }

    public static FuncParam getFuncParamFromUnaryExp(UnaryExp unaryExp) {
        if(unaryExp.primaryExp != null) {
            return PrimaryExp.getFuncParamFromPrimaryExp(unaryExp.primaryExp);
        }
        else if(unaryExp.ident != null) {
            Identifier ident = ErrorHandler.getIdentifier(unaryExp.ident.getToken());
            if(ident instanceof FuncIdent) {
                return new FuncParam(unaryExp.ident.getToken(), 0);
            }
            else return null;
        }
        else {
            return getFuncParamFromUnaryExp(unaryExp.unaryExp);
        }
    }

    // TODO UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp
    public static void unaryExpLLVMBuilder(UnaryExp unaryExp) {
        if(unaryExp.primaryExp != null) {
            PrimaryExp.primaryExpLLVMBuilder(unaryExp.primaryExp);
        }
        else if(unaryExp.ident != null) {
            // ident
            String ident = unaryExp.ident.getToken();
            Function func = (Function) SymbolTable.getValSymbol(ident);
            assert func != null;
            BuilderAttribute.tempParamArrayList = new ArrayList<>();
            if(unaryExp.funcRParams != null) {
                FuncRParams.funcRParamsLLVMBuilder(unaryExp.funcRParams);
            }
            Instruction_Call call = new Instruction_Call(func, BuilderAttribute.tempParamArrayList);
            call.addInstructionInBlock(BuilderAttribute.currentBlock);
            BuilderAttribute.curTempValue = call;
        }
        else if(unaryExp.unaryOp != null) {
            String operator = unaryExp.unaryOp.getSign().getCategory();
            if(operator.equals("PLUS")) {
                unaryExpLLVMBuilder(unaryExp.unaryExp);
            }
            else if(operator.equals("MINU")) {
                unaryExpLLVMBuilder(unaryExp.unaryExp);
                if(BuilderAttribute.isConstant) {
                    BuilderAttribute.curSaveValue = -1 * BuilderAttribute.curSaveValue;
                }
                else {
                    BuilderAttribute.curTempValue = Instruction_Binary.makeBinaryInst(
                            BuilderAttribute.currentBlock, "Sub", BuilderAttribute.zero, BuilderAttribute.curTempValue
                    );
                }
            }
            else if(operator.equals("NOT")) {
                unaryExpLLVMBuilder(unaryExp.unaryExp);
                // not
                BuilderAttribute.curTempValue = Instruction_Binary.makeBinaryInst(BuilderAttribute.currentBlock,
                        "Eq", BuilderAttribute.curTempValue, BuilderAttribute.zero);
            }
        }
    }
}
