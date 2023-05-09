package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends JFrame implements Runnable{

    private static int WIDTH = 400;
    private static int HEIGHT = 300;
    private JTextArea console = null;

    private Player p1;
    private Player p2;
    private ListenPlayer player1 = null;
    private ListenPlayer player2 = null;

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
            while((player1 == null || player2 == null)) {//are players connected?
                console.append("Waiting connections...\n");
                Socket socket = serverSocket.accept();
                if (player1 == null) { //The first connection would always be player1;
                    player1 = new ListenPlayer(socket, 1);
                    System.out.println("create listenplayer1");
                    new Thread(player1).start();
                } else if (player1 != null && player2 == null) {
                    player2 = new ListenPlayer(socket, 2);
                    System.out.println("create listenplayer2");
                    new Thread(player2).start();
                }
                if (player1 != null && player1.self != null) {
                    System.out.println("player1 ok");
                    System.out.println(player1.self.getId());
                }
            }
            System.out.println("Both Player Connected.");
            //Enters game init.
            //game.initGame();
            while (true) {

            }
        }
        catch(IOException e){
            System.err.println(e);
        }
    }

    class ListenPlayer implements Runnable{
        private Socket socket;
        private int id;
        public Player self = null;
        public ListenPlayer(Socket s, int id){
            this.socket = s;
            this.id = id;
            console.append("Player" + this.id + "Connected.\n");
            //self = game.getPlayerWithId(id);
        }
        @Override
        public void run() {
            //TODO: Constantly listen to client's action.
            try {
                ObjectInputStream inputFromClient = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputToClient = new ObjectOutputStream(socket.getOutputStream());

                while(true){
                    try {
                        //TODO: confirm readObject blocks the operation.
                        self = (Player) inputFromClient.readObject();
                        self.setId(id);
                        System.out.println(self.getId());
                        self.printShipList();
                    }
                    catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            catch(IOException ex) {
                ex.printStackTrace();
            }

        }
    }
    public static void main(String[] args){
        Server s = new Server(); // Threading start in constructor.
    }
}
