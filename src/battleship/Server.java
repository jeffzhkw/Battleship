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
                    System.out.println("create listenplayer1");
                } else if (player1 != null && player2 == null) {
                    player2 = new ListenPlayer(socket, 2);
                    new Thread(player2).start();
                    System.out.println("create listenplayer2");
                }
            }

            System.out.println("Both Player Connected.");
            if (player1 != null && player1.self != null) {
                System.out.println("player1 ok");
                System.out.println(player1.self.getId());
            }

            // initial setup
            // player1 moves first
            boolean isPlayer1AbleToMove = true;

            //Enters game init.
            //game.initGame();
            while (true) {
                if (player2.self == null) System.out.println("p2 null");
                if (player1.self == null) System.out.println("p1 null");
                if (player1.self == null || player2.self == null) continue;
                // wait for cur player to make actions
                //player1.self.takeX(-1);
                //player1.self.takeY(-1);
                //player2.self.takeX(-1);
                //player2.self.takeY(-1);
                player1.self.setAbleToMove(isPlayer1AbleToMove);
                player1.outputToClient.writeObject(player1.self);
                player2.self.setAbleToMove(!isPlayer1AbleToMove);
                System.out.println(player2.self.isAbleToMove());
                player2.outputToClient.writeObject(player2.self);
                System.out.println("sent");
                if (isPlayer1AbleToMove) {
                    System.out.println("in p1");
                    while (true) {
                        // wait for currentplayer to intput
                        int x = player1.self.getX();
                        int y = player1.self.getY();
                        System.out.println(x + ", " + y);
                        if (x < 0 || y < 0) {
                            continue;
                        }
                        //System.out.println("hi");
                        //break;
                        //}
                        int status = player2.self.updateSelfByOppoMove(x, y);
                        // when miss or one ship sinks switch the turn
                        System.out.println(status);
                        player1.self.updateOppoView(x, y, status);
                        //player1.self.takeX(-1);
                        //player1.self.takeY(-1);
                        if (status == -1 || status == 3) {
                            isPlayer1AbleToMove = !isPlayer1AbleToMove;
                            break;
                        }

                        //player1.self.displayBoard();
                        //System.out.println("P2===============");
                        //player2.self.displayBoard();
                        player1.outputToClient.writeObject(player1.self);
                        player2.outputToClient.writeObject(player2.self);
                    }
                }
                else {
                    System.out.println("in p2");
                    while (true) {
                        // wait for currentplayer to intput
                        int x = player2.self.getX();
                        int y = player2.self.getY();
                        if (x < 0 || y < 0) {
                            continue;
                        }
                        //System.out.println(x + ", " + y);
                        //break;
                        //}
                        int status = player1.self.updateSelfByOppoMove(x, y);
                        player2.self.updateOppoView(x, y, status);
                        // when miss or one ship sinks switch the turn
                        //player2.self.takeX(-1);
                        //player2.self.takeY(-1);
                        if (status == -1 || status == 3) {
                            isPlayer1AbleToMove = !isPlayer1AbleToMove;
                            break;
                        }

                        player1.outputToClient.writeObject(player1.self);
                        player2.outputToClient.writeObject(player2.self);
                    }
                }
                if (player1.self.getLife() == 0 || player2.self.getLife() == 0) {
                    System.out.println("end");
                    break;
                }

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
        public ObjectOutputStream outputToClient;
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
                outputToClient = new ObjectOutputStream(socket.getOutputStream());

                while(true){
                    try {
                        //TODO: confirm readObject blocks the operation.
                        self = (Player) inputFromClient.readObject();
                        if (self.getId() == -1) {
                            self.setId(id);
                        }
                        System.out.println(self.getId());
                        self.printShipList();
                        int x = player1.self.getX();
                        int y = player1.self.getY();
                        //if (x != -1 && y != -1) {
                        System.out.println("get = " + x + ", " + y);
                        //outputToClient.writeObject(self);
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
