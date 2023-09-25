package FileProcess;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class MyFileWriter {
    private static BufferedWriter writer;
    public static void setWriter(String fileName) throws IOException {
        writer = new BufferedWriter(new FileWriter(fileName));
    }

    public static void writeLine(String line){
        try {
            writer.write(line);
            writer.write('\n');
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void write(String str){
        try {
            writer.write(str);
            writer.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
