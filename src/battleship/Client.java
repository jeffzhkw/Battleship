package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends JFrame implements Runnable{
    //GUIs
    private static int WIDTH = 1370;
    private static int HEIGHT = 768;
    private Socket socket = null;
    private JPanel selfBoard;
    private JPanel oppoBoard;
    private JPanel mainPanel;
    private JPanel west;
    private JPanel east;
    private JPanel center;
    private JPanel south;
    private JScrollPane status;
    //Hold reference of BoardCells to change color
    private ArrayList<BoardCell> selfBoardCells = new ArrayList<BoardCell>();
    private ArrayList<BoardCell> oppoBoardCells = new ArrayList<BoardCell>();
    public Client(){
        super("Battleship");
        initGUI();
        //getContentPane().repaint();

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
        //create mainPanel
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        west = new JPanel();
        east = new JPanel();
        center = new JPanel();
        center.setLayout(new BorderLayout());
        south = new JPanel();
        south.setLayout(new BorderLayout());
        initBothBoard();
        initStatus();
        initControl();
        mainPanel.add(west, BorderLayout.WEST);
        mainPanel.add(east, BorderLayout.EAST);
        mainPanel.add(center, BorderLayout.CENTER);
        mainPanel.add(south,BorderLayout.SOUTH);
        this.add(mainPanel);

        //general setting
        this.setSize(Client.WIDTH, Client.HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    private void initBothBoard(){
        selfBoard = new JPanel();
        selfBoard.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        selfBoard.setLayout(new GridLayout(11, 11, 0,0 ));

        oppoBoard = new JPanel();
        oppoBoard.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        oppoBoard.setLayout(new GridLayout(11, 11,0,0));

        for (int i = 0; i < 121; i++) {
            if (i == 0){
                selfBoard.add(new BoardCell());
                oppoBoard.add(new BoardCell());
            }
            else if (i < 11){
                selfBoard.add(new BoardCell(""+i));
                oppoBoard.add(new BoardCell(""+i));
            }
            else if (i %11 == 0){

                int rowNum = i /11;
                selfBoard.add(new BoardCell(""+rowNum));
                oppoBoard.add(new BoardCell(""+rowNum));
            }
            else{
                BoardCell tempSelfCell = new BoardCell(0);
                BoardCell tempOppoCell = new BoardCell(0);
                selfBoardCells.add(tempSelfCell);
                selfBoard.add(tempSelfCell);
                oppoBoardCells.add(tempOppoCell);
                oppoBoard.add(tempOppoCell);
            }
        }
        west.add(selfBoard);
        east.add(oppoBoard);
    }

    private void initStatus(){
        status = new JScrollPane(new JTextArea());
        center.add(status, BorderLayout.CENTER);
        center.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

    }

    private void initControl(){
        JPanel placeControl = new JPanel();
        JPanel placeCoord = new JPanel();

        placeControl.setLayout(new BorderLayout());
        placeControl.add(new JLabel("Your Board"), BorderLayout.NORTH);

        placeCoord.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
        JPanel placeX = new JPanel();
        placeX.add(new JLabel("Place at Row"));
        placeX.add(new JTextField());
        JPanel placeY = new JPanel();
        placeY.add(new JLabel("Place at Column"));
        placeY.add(new JTextField());
        placeCoord.add(placeX);
        placeCoord.add(placeY);
        placeControl.add(placeCoord, BorderLayout.CENTER);

        JButton placeBtn = new JButton("Place");
        placeControl.add(placeBtn, BorderLayout.SOUTH);

        //---------------------------------------
        JPanel attackControl = new JPanel();
        JPanel attackCoord = new JPanel();

        attackControl.setLayout(new BorderLayout());
        attackControl.add(new JLabel("Opponent Board"), BorderLayout.NORTH);

        attackCoord.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
        JPanel attackX = new JPanel();
        attackX.add(new JLabel("Attack at Row"));
        attackX.add(new JTextField());
        JPanel attackY = new JPanel();
        attackY.add(new JLabel("Attack at Column"));
        attackY.add(new JTextField());
        attackCoord.add(attackX);
        attackCoord.add(attackY);
        attackControl.add(attackCoord, BorderLayout.CENTER);

        JButton attackBtn = new JButton("Attack");
        attackControl.add(attackBtn, BorderLayout.SOUTH);

        south.add(placeControl, BorderLayout.WEST);
        south.add(attackControl, BorderLayout.EAST);
    }
    private void updateSelfBoard(){
        selfBoard.repaint();
    }

    private void updateOppoBoard(){
        selfBoard.repaint();
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
