package com.company;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class MainServer {
    public static void main(String[] args) {
        startServerSocket();
    }
    public static void startServerSocket(){
        List<String> listUsers=new ArrayList();
        List<String> listPasswords=new ArrayList();
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(3000);
            while (true){
                Socket socket = serverSocket.accept();
                new ClientThread(socket,listUsers,listPasswords).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(serverSocket != null)
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }
}
