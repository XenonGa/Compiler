package LexicalAnalysis;

import FileProcess.MyFileReader;
import FileProcess.MyFileWriter;

import java.io.IOException;
import java.util.ArrayList;

public class Lexer {
    private String token;
    private int lineNumber;
    private char character;
    public static ArrayList<Token> tokenList = new ArrayList<Token>();
    private boolean lineAnnotation;
    private boolean blockAnnotation;
    public Lexer() throws IOException {
        this.token = "";
        this.lineNumber = 1;
        MyFileReader.setReader("testfile.txt");
        MyFileWriter.setWriter("output.txt");
        this.lineAnnotation = false;
        this.blockAnnotation = false;
        runLexer();
        // tokenList.forEach(Token::writeIntoFile);
    }

    public void runLexer() throws IOException {
        getSingleChar();
        while (character != (char) -1) {
            clearToken();
            if(Character.isSpaceChar(character)){
                getSingleChar();
            }
            else if(isAlpha(character) || character == '_') {
                while(isAlpha(character) || isNumber(character) || character == '_') {
                    stringConcatenate(character);
                    getSingleChar();
                }
                if(isInKeyWordMap()){
                    storeWord(token);
                }
                else {
                    storeWord("Ident");
                }
            }
            else if(isNumber(character)) {
                while(isNumber(character)) {
                    stringConcatenate(character);
                    getSingleChar();
                }
                storeWord("IntConst");
            }
            else if(character == '"') {
                stringConcatenate(character);
                getSingleChar();
                if(isInAnnotation()) {
                    continue;
                }
                while (character != '"') {
                    stringConcatenate(character);
                    getSingleChar();
                }
                stringConcatenate(character);
                getSingleChar();
                storeWord("FormatString");
            }
            else if(character == '<' || character == '>' || character == '!' || character == '=') {
                stringConcatenate(character);
                getSingleChar();
                if(character == '=') {
                    stringConcatenate(character);
                    getSingleChar();
                }
                storeWord(token);
            }
            else if(character == '&' || character == '|') {
                stringConcatenate(character);
                char tempCharacter = character;
                getSingleChar();
                if(character == tempCharacter) {
                    stringConcatenate(character);
                    getSingleChar();
                    storeWord(token);
                }
            }
            else if(character == '+' || character == '-' || character == '%' || character == ';' ||
                    character == ',' || character == '{' || character == '}' || character == '[' ||
                    character == ']' || character == '(' || character == ')') {
                stringConcatenate(character);
                getSingleChar();
                storeWord(token);
            }
            else if(character == '/') {
                stringConcatenate(character);
                getSingleChar();
                if(isInAnnotation()) continue;
                if(character == '/')
                {
                    lineAnnotation = true;
                    getSingleChar();
                }
                else if(character == '*') {
                    blockAnnotation = true;
                    getSingleChar();
                }
                else {
                    storeWord(token);
                }
            }
            else if(character == '*')
            {
                stringConcatenate(character);
                getSingleChar();
                if(character == '/') {
                    if(!lineAnnotation) {
                        blockAnnotation = false;
                    }
                    getSingleChar();
                }
                else {
                    storeWord(token);
                }
            }
            else if(character == '\n') {
                lineAnnotation = false;
                getSingleChar();
            }
            else {
                getSingleChar();
            }
        }
    }

    private void getSingleChar() throws IOException{
            this.character = MyFileReader.readSingleChar();
            if(this.character == '\n') {
                this.lineNumber++;
            }
            if(this.character == (char) -1) {
                MyFileReader.close();
            }
    }
    private void clearToken() {
        this.token = "";
    }
    private void stringConcatenate(char character) {
        this.token += character;
    }
    private boolean isInKeyWordMap() {
        return KeyWord.isKeyword(this.token);
    }
    private boolean isAlpha(char character){
        return (character <= 'z' && character >= 'a') || (character <= 'Z' && character >= 'A');
    }

    private boolean isNumber(char character){
        return (character <= '9' && character >= '0');
    }
    private void storeWord(String category) {
        if(!lineAnnotation && !blockAnnotation)
        {
            Token tokenForStore = new Token(KeyWord.getKeyword(category), this.token, this.lineNumber);
            tokenList.add(tokenForStore);
        }
    }
    private boolean isInAnnotation() {
        return lineAnnotation || blockAnnotation;
    }
}
