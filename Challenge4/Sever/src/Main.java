import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.net.*;
public class Main {
    public static void main(String[] args) {
        int port = -1;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception exception) {
            System.out.println("Enter a port number:");
            Scanner scanner = new Scanner(System.in);
            port = scanner.nextInt();
        }
        try {
            //Create a socket on the port defined by the user
            ServerSocket socket = new ServerSocket(port);

            //Create a new handler thread for each client which connects
            Socket clientSocket;
            ClientHandler handler = new ClientHandler();
            handler.start();
            while (true) {
                clientSocket = socket.accept();
                System.out.println("A New client has joined");
                handler.addNewClientToClientList(clientSocket);
            }

        } catch (Exception exception) {
            System.out.println("Sever has encountered an error, halting");
            System.exit(1);
        }


    }
}