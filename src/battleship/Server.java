package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server extends JFrame implements Runnable{

    private static int WIDTH = 400;
    private static int HEIGHT = 300;
    private JTextArea console = null;

    public static ListenPlayer player1 = null;
    public static ListenPlayer player2 = null;

    public static boolean isPlayer1AbleToMove = true;

    public static ArrayList<ListenPlayer> clients = new ArrayList<>();
    public ExecutorService pool = Executors.newFixedThreadPool(2);
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
                    clients.add(player1);
                    pool.execute(player1);
                    System.out.println("create listenplayer1");
                } else if (player1 != null && player2 == null) {
                    player2 = new ListenPlayer(socket, 2);
                    clients.add(player2);
                    pool.execute(player2);
                    System.out.println("create listenplayer2");
                }
            }
            System.out.println("Both Player Connected.");
            if (player1 != null && player1.self != null) {
                System.out.println("player1 ok");
                System.out.println(player1.self.getId());
            }
        }
        catch(IOException e){
            System.err.println(e);
        }
    }

    class SeverPlayer {
        public static Player p1 = null;
        public static Player p2 = null;
    }
    class ListenPlayer implements Runnable{
        private Socket socket;
        private int id;
        public Player self = null;
        public Player oppo = null;
        public ObjectOutputStream outputToClient;
        public ListenPlayer(Socket s, int id){
            this.socket = s;
            this.id = id;
            console.append("Player" + this.id + "Connected.\n");
            //self = game.getPlayerWithId(id);
        }

        public void gameLogicHandler() {
            System.out.println("current running : " + id);
            if (Server.player1 == null || Server.player1.self == null) return;
            if (Server.player2 == null || Server.player2.self == null) return;
            Server.player1.self.setAbleToMove(Server.isPlayer1AbleToMove);
            Server.player2.self.setAbleToMove(!Server.isPlayer1AbleToMove);

            if (Server.isPlayer1AbleToMove) {
                    System.out.println("in p1");
                    // get x and y
                    int x = Server.player1.self.getX();
                    int y = Server.player1.self.getY();
                    if (x < 0 || y < 0) { // default, wait for valid input
                        try{
                            Player p1 = Server.player1.self;
                            Player p2 = Server.player2.self;
                            Server.player1.outputToClient.reset();
                            Server.player1.outputToClient.writeObject(p1);
                            Server.player2.outputToClient.reset();
                            Server.player2.outputToClient.writeObject(p2);
                            p2.displayBoard();

                        }
                        catch (IOException e){
                            System.err.println(e);
                        }
                        return;
                    }
                    // get status and update player2's self board
                    int status = Server.player2.self.updateSelfByOppoMove(x, y);
                    // update player1's oppo board
                    System.out.println("status : " + status);
                    if (status != 3) {
                        Server.player1.self.updateOppoView(x, y, status);
                    }
                    else {
                        Server.player1.self.updateOppoSunk(Server.player2.self.self.state, Server.player2.self.self.state[x][y].getShipid());
                    }
                    // when miss or one ship sinks switch the turn
                    if (status == -1 || status == 3) {
                        Server.isPlayer1AbleToMove = !Server.isPlayer1AbleToMove;
                        Server.player1.self.setAbleToMove(Server.isPlayer1AbleToMove);
                        Server.player2.self.setAbleToMove(!Server.isPlayer1AbleToMove);
                    }
                    try{
                        System.out.println("hi");
                        Player p1 = Server.player1.self;
                        Player p2 = Server.player2.self;
                        Server.player1.outputToClient.reset();
                        Server.player1.outputToClient.writeObject(p1);
                        Server.player2.outputToClient.reset();
                        Server.player2.outputToClient.writeObject(p2);
                        p2.displayBoard();
                    }
                    catch (IOException e){
                        System.err.println(e);
                    }

            }
            else {
                    System.out.println("in p2");
                    // get x and y
                    int x = Server.player2.self.getX();
                    int y = Server.player2.self.getY();
                    if (x < 0 || y < 0) { // default, wait for actual input
                        try{
                            Player p1 = Server.player1.self;
                            Player p2 = Server.player2.self;
                            Server.player1.outputToClient.reset();
                            Server.player1.outputToClient.writeObject(p1);
                            Server.player2.outputToClient.reset();
                            Server.player2.outputToClient.writeObject(p2);
                            p2.displayBoard();
                        }
                        catch (IOException e){
                            System.err.println(e);
                        }
                        return;
                    }
                    int status = Server.player1.self.updateSelfByOppoMove(x, y);
                    // when miss or one ship sinks switch the turn
                    if (status != 3) {
                        Server.player2.self.updateOppoView(x, y, status);
                    }
                    else {
                        Server.player2.self.updateOppoSunk(Server.player1.self.self.state, Server.player1.self.self.state[x][y].getShipid());
                    }
                    if (status == -1 || status == 3) {
                        Server.isPlayer1AbleToMove = !Server.isPlayer1AbleToMove;
                        Server.player1.self.setAbleToMove(Server.isPlayer1AbleToMove);
                        Server.player2.self.setAbleToMove(!Server.isPlayer1AbleToMove);
                    }
                    try{
                        Player p1 = Server.player1.self;
                        Player p2 = Server.player2.self;
                        Server.player1.outputToClient.reset();
                        Server.player1.outputToClient.writeObject(p1);
                        Server.player2.outputToClient.reset();
                        Server.player2.outputToClient.writeObject(p2);
                        p2.displayBoard();
                    }
                    catch (IOException e){
                        System.err.println(e);
                    }
            }
            if (Server.player1.self.getLife() == 0 || Server.player2.self.getLife() == 0) {
                System.out.println("end");
            }
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
                        int x = self.getX();
                        int y = self.getY();
                        System.out.println("get = " + x + ", " + y);
                        self.displayBoard();
                        // run game logic
                        gameLogicHandler();
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
