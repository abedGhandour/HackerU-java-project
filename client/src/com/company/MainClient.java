package com.company;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class MainClient {

    public static void main(String[] args)throws IOException, EndOfStream {
        while (true){
            System.out.println("please choose: ");
            //comment
            System.out.println("1. getTips");
            System.out.println("2. to read all content from file");
            System.out.println("3. to save your name&password in the list");
            System.out.println("4. to read data from list (note: ONLY names)");
            System.out.println("5. exit");
            System.out.print("your choice: ");
            InputStream inputStream= new InputStream() {
                @Override
                public int read() throws IOException {
                    return 0;
                }
            };
            int choice = readInteger();
            if(choice == 5) {
                System.out.println("Bye!");
                return;
            }
            switch (choice){
                case 1:
                    System.out.println("enter user name length:");
                    int userNameLength = readInteger();
                    if(userNameLength == -1)
                        throw new EndOfStream();
                    byte[] userNameBytes = new byte[userNameLength];
                    int actuallyRead = inputStream.read(userNameBytes);
                    if(actuallyRead != userNameLength)
                        return;
                    System.out.println("enter username:");
                    Scanner scanner = new Scanner(System.in);
                    String userName = scanner.nextLine();

                    System.out.println("enter how many shifts you had:");
                    int numOfShifts = readInteger();
                    if(numOfShifts < 0)
                        throw new EndOfStream();
                    byte[] numOfShiftsBytes = new byte[numOfShifts];
                    actuallyRead = inputStream.read(numOfShiftsBytes);
                    if(actuallyRead != numOfShifts)
                        return;
                    ClientToServer.getTips(numOfShifts,userName,userNameLength);
                    break;
                case 2:
                    ClientToServer.readContentFromFile();
                    break;
                case 3:
                    ClientToServer.addToList();
                    break;
                case 4:
                    ClientToServer.readFromList();
                    break;

                default:
                    System.out.println("invalid choice");
            }
        }
    }
    private static int readInteger(){
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        return Integer.valueOf(input);
    }
}