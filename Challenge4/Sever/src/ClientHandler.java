import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

public class ClientHandler extends Thread {
    ArrayList<ClientConnection> clientConnections = new ArrayList<ClientConnection>();
    public void run() {
        while (true) {
            ArrayList<String> newMessages = new ArrayList<String>();
            ArrayList<String> oldMessages = new ArrayList<String>();
            for (ClientConnection client: clientConnections) {
                String msg = client.checkForUpdates(oldMessages);
                if (msg != null) {
                    System.out.println("MSG RECIVED");
                    newMessages.add(msg);
                }
            }
            Iterator<ClientConnection> it2 = clientConnections.iterator();
            for (String msg: newMessages) {
                System.out.println(msg);
                while (it2.hasNext()) {
                    it2.next().sendNewMessage(msg);
                }
            }
            while (oldMessages.size() != 0) {
                oldMessages.remove(0);
            }
            while (newMessages.size() != 0) {
                oldMessages.add(newMessages.get(0));
                newMessages.remove(0);
            }
        }
    }
    public void addNewClientToClientList(Socket socket) {
        clientConnections.add(new ClientConnection(socket));
        System.out.println("Added new client");
    }
}
