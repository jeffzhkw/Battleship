package battleship;

import javax.swing.*;
import java.io.IOException;
import java.net.Socket;

public class Client extends JFrame implements Runnable{
    private static int WIDTH = 1280;
    private static int HEIGHT = 600;
    Socket socket = null;

    public Client(){
        super("Battleship");
        initGUI();
    }

    private void initGUI(){
        //create menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem connectItem = new JMenuItem("Play");
        exitItem.addActionListener((e) -> System.exit(0));
        connectItem.addActionListener((e)-> handleConnectServer());
        menu.add(connectItem);
        menu.add(exitItem);
        menuBar.add(menu);
        this.setJMenuBar(menuBar);
        //general setting
        this.setSize(Client.WIDTH, Client.HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }

    private void handleConnectServer(){
        System.out.println("Play clicked");
        try{
            socket = new Socket("localhost", 1216);

        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {

    }

    //TODO:
    // 1. GUI
    // 1.1 GUI design
    // 1.2 GUI interaction.
    // 2. Start connection to server.

    public static void main(String[] args){
        Client c = new Client(); // Threading start in constructor.
    }


}
