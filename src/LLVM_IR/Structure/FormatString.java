package LLVM_IR.Structure;

import LLVM_IR.BuilderAttribute;
import LLVM_IR.LLVMType.Type;
import LLVM_IR.LLVMType.TypeArray;
import LLVM_IR.LLVMType.TypePointer;
import LLVM_IR.SymbolTable;

public class FormatString extends Value {
    private String formatString;
    private int length;

    public FormatString(String formatString) {
        super("\"" + formatString.replace("\n", "\\n") + "\"", new TypePointer(BuilderAttribute.i8));
        this.length = formatString.length() + 1;
        this.formatString = formatString.replace("\n", "\\0a") + "\\00";
    }

    public String toString() {
        return "[" + this.length + " x " + ((TypePointer) this.getType()).getType() + "] c\"" + this.formatString + "\"";
    }

    public static String getFormatStringName(String str) {
        int index = getFormatStringIndex(str);
        return getFormatStringNameByIndex(index);
    }

    public static String getFormatStringNameByIndex(int index) {
        return "_str_" + index;
    }

    public static int getFormatStringIndex(String str) {
        for (int i = 0; i < BuilderAttribute.formatStringArrayList.size(); i++) {
            if(str.equals(BuilderAttribute.formatStringArrayList.get(i))) {
                return i;
            }
        }
        BuilderAttribute.formatStringArrayList.add(str);
        Type type = new TypeArray(BuilderAttribute.i8, str.length() + 1);
        Value value = new GlobalVariable(getFormatStringName(str), type,
               new FormatString(str) ,true);
        SymbolTable.addGlobalValSymbol(getFormatStringName(str), value);
        return BuilderAttribute.formatStringArrayList.size() - 1;
    }
}
