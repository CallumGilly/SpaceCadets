import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientConnection{
    protected Socket socket;
    protected ArrayList<String> newMessages = new ArrayList<String>();
    private ObjectInputStream inp = null;
    private ObjectOutputStream out = null;
    private Reader reader = null;
    private ArrayList<String> msgsToSend;

    public ClientConnection(Socket clientSocket) {
        socket = clientSocket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            inp = new ObjectInputStream(socket.getInputStream());
            reader = new Reader(inp);
            out.writeObject("Hello, Welcome to the chat room!");
        } catch (IOException e) {
            return;
        }
    }

    public String checkForUpdates(ArrayList<String> msgsToSend) {
        try {
            for (String msg: msgsToSend) {
                out.writeObject(msg);
                System.out.println("MSG Sent");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            if (reader.newMessages.size() > 0) {
                String newMsg = reader.newMessages.get(0);
                reader.newMessages.remove(0);
                return newMsg;
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }

    public void sendNewMessage(String msg) {
        try {
            out.writeObject(msg);
            System.out.println("MSG Sent");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

//    //https://stackoverflow.com/questions/10131377/socket-programming-multiple-client-to-one-server
//    public void run() {
//        while (true) {
//            try {
//                newMessages.add((String) inp.readObject());
//                System.out.println("MSG Recived");
//            } catch (Exception e) {
//                e.printStackTrace();
//                return;
//            }
//        }
//    }
}
