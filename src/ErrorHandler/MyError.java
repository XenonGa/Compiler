package ErrorHandler;

public class MyError {
    private String error_type;
    private int error_line_num;

    public MyError(String error_type, int error_line_num) {
        this.error_type = error_type;
        this.error_line_num = error_line_num;
    }

    public String getError_type() {
        return error_type;
    }

    public int getError_line_num() {
        return error_line_num;
    }

    public String writeError() {
        return error_line_num + " " + error_type + "\n";
    }

    public boolean isSameLine(int lineNum) {
        return lineNum == error_line_num;
    }
}
