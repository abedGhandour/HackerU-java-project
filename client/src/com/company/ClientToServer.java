package com.company;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientToServer {

    public static final int OKAY = 200;
    public static final String HOST = "127.0.0.1";
    public static final int PORT = 3000;

    public static void getTips(final int numOfShifts, final String username,final int userNameLength){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                outputStream.write(1);
                System.out.println("num of shifts: " + numOfShifts);
                outputStream.write(numOfShifts);
                byte[] usernameBytes = username.getBytes();
                outputStream.write(usernameBytes.length);
                outputStream.write(usernameBytes);
                int response=inputStream.read();
                if(response==OKAY) {
                    System.out.println("saved data to file");
                }
                int userTips=inputStream.read();
                System.out.println(username+" got: "+userTips+" in shekels");
                userTips=inputStream.read();
                System.out.println("and youve got: "+userTips+" in dolars");
            }
        });
    }
    public static void connect(ConnectionListener listener){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Socket socket = null;
        try{
            socket = new Socket(HOST, PORT);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
            listener.onConnect(inputStream,outputStream);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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
    public static void readContentFromFile(){

        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {

                outputStream.write(2);

                int stringLength = inputStream.read();

                if (stringLength == -1)
                    throw new IOException("it has to get some string!!");
                System.out.println("the file contains: " );
                do{
                    byte[] buffer = new byte[stringLength];
                    int actuallyRead = inputStream.read(buffer);
                    if (actuallyRead != stringLength)
                        throw new IOException("Wrong length of string");
                    String response = new String(buffer);


                } while ((stringLength = inputStream.read()) !=-1);
            }
        });
    }

    public static void addToList(){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                outputStream.write(3);
                Scanner scanner=new Scanner(System.in);
                System.out.println("enter user name length above 0:");
                int userNameLength;
                do{
                    userNameLength = scanner.nextInt();
                    if(userNameLength<=0) {
                        System.out.println("illegal please try again");
                    }
                }while(userNameLength < 0);
                System.out.println("enter username:");


                scanner = new Scanner(System.in);
                String username = scanner.nextLine();
                byte[] usernameBytes = username.getBytes();
                outputStream.write(usernameBytes.length);
                outputStream.write(usernameBytes);

               scanner=new Scanner(System.in);
                System.out.println("enter password length above 0:");
                int passwordLength;
                do{
                    passwordLength = scanner.nextInt();
                    if(passwordLength<=0) {
                        System.out.println("illegal please try again");
                    }
                } while(passwordLength < 0) ;

                System.out.println("enter password:");
                scanner = new Scanner(System.in);
                String password = scanner.nextLine();

                byte[] passwordBytes = password.getBytes();
                outputStream.write(passwordBytes.length);
                outputStream.write(passwordBytes);
            }
        });
    }
    public static void readFromList(){
        connect(new ConnectionListener() {
            @Override
            public void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException {
                outputStream.write(4);

                int stringLength = inputStream.read();
                if (stringLength == -1)
                    throw new IOException("it has to get some string!!");
                System.out.println("the list contains: ");
                do{
                    byte[] buffer = new byte[stringLength];
                    int actuallyRead = inputStream.read(buffer);
                    if (actuallyRead != stringLength)
                        throw new IOException("Wrong length of string");
                    String response = new String(buffer);
                } while ((stringLength = inputStream.read()) !=-1);
            }
        });
    }
    public interface ConnectionListener{
        void onConnect(InputStream inputStream, OutputStream outputStream) throws IOException;

    }

}