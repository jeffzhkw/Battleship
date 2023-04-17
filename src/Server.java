import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends JFrame implements Runnable{

    private static int WIDTH = 400;
    private static int HEIGHT = 300;
    private JTextArea console = null;

    Socket p1 = null;
    Socket p2 = null;

    public Server(){
        super("Game Server");
        initGUI();
        new Thread(this).start();
    }

    private void initGUI(){

        //Create Console GUI
        JPanel consolePanel = new JPanel(new BorderLayout());
        console = new JTextArea();
        consolePanel.add(new JScrollPane(console), BorderLayout.CENTER);
        this.add(consolePanel);
        //General Setting
        this.setSize(Server.WIDTH, Server.HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    //TODO:
    // 1. handle connections from 2 client using Sockets.
    // 2. running game logic.
    // 3. Define what needed to be send. Payload structure.

    @Override
    public void run() {
        try{
            ServerSocket serverSocket = new ServerSocket(1216);
            console.append("Server started at port 1216 at " + new Date() + "\n");
            while((p1 == null || p2 == null)){
                console.append("Waiting connections...");
                Socket socket = serverSocket.accept();
            }
        }
        catch(IOException e){
            System.err.println(e);
        }
    }

    class HandlePlayer implements Runnable{

        @Override
        public void run() {

        }
    }
    public static void main(String[] args){
        Server s = new Server(); // Threading start in constructor.
    }
}
