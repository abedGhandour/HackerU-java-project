package com.company;
import java.io.*;

public class ReadAndWriteToFile {
    static void exampleOfWritingStringToFiles(String username){
        String path=("src\\com\\company\\file.txt");
        File file = new File(path);
        OutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream(file);
            outputStream.write(username.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(outputStream != null)
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}