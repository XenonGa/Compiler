package LexicalAnalysis;

import FileProcess.MyFileWriter;

public class Token {
    private final String category;
    private final String token;
    private final int lineNumber;

    public Token(String category, String token, int lineNumber) {
        this.category = category;
        this.token = token;
        this.lineNumber = lineNumber;
    }

    public String getCategory() {
        return category;
    }

    public String getToken() {
        return token;
    }

    public String getWholeToken() {
        return category + " " + token + "\n";
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public boolean equalsToCategory(String other) {
        return category.equals(other);
    }
    public boolean equalsToToken(String other) {
        return token.equals(other);
    }
    public void writeIntoFile() {
        MyFileWriter.writeLine(category + " " + token);
    }
}
