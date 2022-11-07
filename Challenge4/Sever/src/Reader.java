import java.io.ObjectInputStream;
import java.util.ArrayList;

public class Reader extends Thread {
    public ArrayList<String> newMessages;
    ObjectInputStream inp;
    public Reader(ObjectInputStream inp) {
            this.inp = inp;
    }

    public void run() {
        while (true) {
            try {
                newMessages.add((String) inp.readObject());
                System.out.println("RECIVED: ");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
