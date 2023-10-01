package ErrorHandler;

import FileProcess.MyFileWriter;

import java.util.ArrayList;
import java.util.Comparator;

public class ErrorHandler {
    public static ArrayList<MyError> errorArrayList = new ArrayList<>();


    public static ArrayList<MyError> getErrorArrayList() {
        return errorArrayList;
    }

    public static void addNewError(MyError error) {
        for (MyError value : errorArrayList) {
            if (value.equals(error)) return;
        }
        errorArrayList.add(error);
    }
    public void writeErrorArrayList() {
        errorArrayList.sort(Comparator.comparingInt((MyError a) -> a.getError_line_num()));
        for(MyError a : errorArrayList) {
            MyFileWriter.write(a.writeError());
        }
    }
}
