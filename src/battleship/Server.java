package battleship;

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

    private HandlePlayer player1 = null;
    private HandlePlayer player2 = null;

    private GameLogic game;

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
            while((player1 == null || player2 == null)) {
                console.append("Waiting connections...\n");
                Socket socket = serverSocket.accept();
                if (player1 == null) { //The first connection would always be player1;
                    player1 = new HandlePlayer(socket, 1);
                } else if (player1 != null && player2 == null) {
                    player2 = new HandlePlayer(socket, 2);
                }
            }
            System.out.println("Both Player Connected.");
            //Enters game init.
        }
        catch(IOException e){
            System.err.println(e);
        }
    }

    class HandlePlayer implements Runnable{
        private Socket socket;
        private int id;
        public HandlePlayer(Socket s, int id){
            this.socket = s;
            this.id = id;
            console.append("Player" + this.id + "Connected.\n");
        }
        @Override
        public void run() {
            //Constantly listen to client's action.
            while(true){

            }
        }
    }
    public static void main(String[] args){
        Server s = new Server(); // Threading start in constructor.
    }
}
