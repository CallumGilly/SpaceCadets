import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class WrappedSocket extends Thread{
    private Socket socket;
    private BufferedReader reader;
    private PrintWriter output;
    private ArrayList<WrappedSocket> clients;

    public WrappedSocket(Socket socket, ArrayList<WrappedSocket> clients) {
        this.clients = clients;
        try {
            this.socket = socket;
            output = new PrintWriter(socket.getOutputStream());
            output.println("You are connected, welcome to the chat room!");
            output.flush();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public void run() {
        while (true) {
            try {
                String line = reader.readLine();
                System.out.println(line);
                for (WrappedSocket clientToSend : clients) {
                    clientToSend.output.println(line);
                    clientToSend.output.flush();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
