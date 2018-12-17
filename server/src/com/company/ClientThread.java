package com.company;

import java.io.*;
import java.net.Socket;
import java.util.List;

public class ClientThread extends Thread {

    private static final double THIS_WEEKS_TIPS_SHEKELS=2800.0;
    private static final double THIS_WEEKS_TIPS_DOLARS=360.0;
    private static final double THIS_WEEKS_SHIFTS=31.5;

    public static final int ACTION1 = 1;
    public static final int ACTION2 = 2;
    public static final int ACTION3 = 3;
    public static final int ACTION4 = 4;
    public static final int OKAY = 200;
    private List<String> listPasswords;
    private List<String> listUsers;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public ClientThread(Socket socket, List<String> listUsers,List<String> listPasswords) {
        this.socket = socket;
        this.listUsers=listUsers;
        this.listPasswords=listPasswords;
    }

    @Override
    public void run() {
        try {

            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            int action = inputStream.read();
            switch (action){
                case ACTION1:
                    getTips();
                    break;
                case ACTION2:
                    readContentFromFile();
                    break;
                case ACTION3:
                    addToList();
                    break;
                case ACTION4:
                    readFromList();
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private void getTips() throws IOException{

        String username;
        int numOfShifts;
        numOfShifts = inputStream.read();
        int stringLength = inputStream.read();
        int actuallyRead;
        byte[] stringBytes = new byte[stringLength];
        actuallyRead = inputStream.read(stringBytes);
        if(actuallyRead != stringLength)
            return;
        username = new String(stringBytes);

        ReadAndWriteToFile.exampleOfWritingStringToFiles(username);

        outputStream.write(OKAY);
        outputStream.write((int) (THIS_WEEKS_TIPS_SHEKELS*(numOfShifts/THIS_WEEKS_SHIFTS)));
        outputStream.write((int) (THIS_WEEKS_TIPS_DOLARS*(numOfShifts/THIS_WEEKS_SHIFTS)));
    }
    private void readContentFromFile() throws IOException {
            String path=("src\\com\\company\\file.txt");

            File file = new File(path);
            if(!file.exists())
                return;
            InputStream inputStream = null;
            try{
                inputStream = new FileInputStream(file);

                byte[] buffer = new byte[4];
                int actuallyRead ;

                while ((actuallyRead= inputStream.read(buffer)) != -1){
                    String content = new String(buffer, 0, actuallyRead);
                    byte[] fileBytes = content.getBytes();
                    outputStream.write(fileBytes.length);
                    outputStream.write(fileBytes);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(inputStream != null)
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
        }
    private void addToList ()throws IOException{
        String username;
        int usernameLength = inputStream.read();
        byte[] stringBytes = new byte[usernameLength];
        int actuallyRead = inputStream.read(stringBytes);
        if(actuallyRead != usernameLength)
            return;
        username = new String(stringBytes);

        String password;
        int passwordLength = inputStream.read();
        stringBytes = new byte[passwordLength];
        actuallyRead = inputStream.read(stringBytes);
        if(actuallyRead != passwordLength)
            return;
        password = new String(stringBytes);

        listUsers.add(username);
        listPasswords.add(password);
    }
    private void readFromList()throws IOException{
            for (String users : listUsers) {
                byte[] contentBytes = users.getBytes();
                outputStream.write(contentBytes.length);
                outputStream.write(contentBytes);
            }
        }
}