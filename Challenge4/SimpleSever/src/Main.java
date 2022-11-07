import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        int port = 7070;
//        try {
//            port = Integer.parseInt(args[0]);
//        } catch (Exception exception) {
//            System.out.println("Enter a port number:");
//            Scanner scanner = new Scanner(System.in);
//            port = scanner.nextInt();
//        }
        try {
            ServerSocket server = new ServerSocket(port);
            while(true){
                System.out.println("Waiting for the client request");
                //creating socket and waiting for client connection
                Socket socket = server.accept();
                //read from socket to ObjectInputStream object
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                //convert ObjectInputStream object to String
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                String message = (String) ois.readObject();
                System.out.println("Message Received: " + message);
                //create ObjectOutputStream object
                //write object to Socket
                oos.writeObject("Hi Client "+message);
                //close resources
                ois.close();
                oos.close();
                socket.close();
                //terminate the server if client sends exit request
                if(message.equalsIgnoreCase("exit")) break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println(ex);
        }
    }
}