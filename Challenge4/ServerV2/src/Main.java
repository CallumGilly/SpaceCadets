import java.net.ServerSocket;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Scanner;
//Credit, Debuging help given by Ben Hadlington
public class Main {
    private ArrayList<WrappedSocket> clients = new ArrayList<WrappedSocket>();
    ServerSocket sever;
    public static void main(String[] args) {
        int port = -1;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception exception) {
            System.out.println("Enter a port number:");
            Scanner scanner = new Scanner(System.in);
            port = scanner.nextInt();
        }
        Main runner = new Main();
        runner.run(port);
    }

    public void run(int port) {
        try {
            sever = new ServerSocket(port);
            while (true) {
                WrappedSocket temp = new WrappedSocket(sever.accept(), clients);
                temp.start();
                clients.add(temp);
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }
        System.out.println("Exiting");
    }
}