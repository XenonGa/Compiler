package FileProcess;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyFileReader {
    private static BufferedReader reader;

    public static void setReader(String fileName) throws IOException {
        reader = new BufferedReader(new FileReader(fileName));
    }

    public static char readSingleChar() throws IOException {
        return (char) reader.read();
    }

    public static String readLine() throws IOException {
        return reader.readLine();
    }
    public static void close() {
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
