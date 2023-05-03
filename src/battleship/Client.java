package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends JFrame implements Runnable{
    private Socket socket = null;
    //GUIs
    private static int WIDTH = 1440;
    private static int HEIGHT = 800;
    private JPanel selfBoard;
    private JPanel oppoBoard;
    private JPanel mainPanel;
    private JPanel west;
    private JPanel east;
    private JPanel center;
    private JPanel placeControl;
    private JPanel attackControl;
    private JScrollPane status;
    //Hold reference of BoardCells to change color
    private ArrayList<BoardCell> selfBoardCells = new ArrayList<BoardCell>();
    private ArrayList<BoardCell> oppoBoardCells = new ArrayList<BoardCell>();
    public Client(){
        super("Battleship");
        initGUI();
        getContentPane().repaint();
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
        center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
        initControl();
        initBothBoard();
        initStatus();

        mainPanel.add(west, BorderLayout.WEST);
        mainPanel.add(east, BorderLayout.EAST);
        mainPanel.add(center, BorderLayout.CENTER);
        this.add(mainPanel);

        //general setting
        this.setSize(Client.WIDTH, Client.HEIGHT);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
    }
    private void initBothBoard(){
        JPanel selfWrapper = new JPanel();
        selfWrapper.setLayout(new BoxLayout(selfWrapper, BoxLayout.Y_AXIS));

        selfBoard = new JPanel();
        selfBoard.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        selfWrapper.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        selfBoard.setLayout(new GridLayout(11, 11, 0,0 ));

        JPanel oppoWrapper = new JPanel();
        oppoWrapper.setLayout(new BoxLayout(oppoWrapper, BoxLayout.Y_AXIS));

        oppoBoard = new JPanel();
        oppoBoard.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        oppoWrapper.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
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

        selfWrapper.add(new JLabel("Your Board"));
        selfWrapper.add(selfBoard);
        selfWrapper.add(placeControl);
        oppoWrapper.add(new JLabel("Opponent Board"));
        oppoWrapper.add(oppoBoard);
        oppoWrapper.add(attackControl);
        west.add(selfWrapper);
        east.add(oppoWrapper);
    }
    private void initStatus(){
        status = new JScrollPane(new JTextArea());
        //status.setMaximumSize(new Dimension(250, 580));
        center.add(status);
        center.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
    }
    private void initControl(){
        placeControl = new JPanel();
        JPanel placeCoord = new JPanel();
        placeControl.setLayout(new BorderLayout());

        placeCoord.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
        JPanel placeX = new JPanel();
        placeX.setLayout(new BorderLayout());
        placeX.add(new JLabel("Place at Row"), BorderLayout.NORTH);
        placeX.add(new JTextField(),BorderLayout.SOUTH);
        JPanel placeY = new JPanel();
        placeY.setLayout(new BorderLayout());
        placeY.add(new JLabel("Place at Column"), BorderLayout.NORTH);
        placeY.add(new JTextField(),BorderLayout.SOUTH);
        placeCoord.add(placeX);
        placeCoord.add(placeY);
        placeCoord.add(new JCheckBox("is Horizontal?"));
        placeControl.add(placeCoord, BorderLayout.NORTH);

        JButton placeBtn = new JButton("Place");
        placeControl.add(placeBtn, BorderLayout.SOUTH);

        //---------------------------------------
        attackControl = new JPanel();
        JPanel attackCoord = new JPanel();
        attackControl.setLayout(new BorderLayout());
        attackCoord.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
        JPanel attackX = new JPanel();
        attackX.setLayout(new BorderLayout());
        attackX.add(new JLabel("Attack at Row"),BorderLayout.NORTH);
        attackX.add(new JTextField(),BorderLayout.SOUTH);
        JPanel attackY = new JPanel();
        attackY.setLayout(new BorderLayout());
        attackY.add(new JLabel("Attack at Column"),BorderLayout.NORTH);
        attackY.add(new JTextField(),BorderLayout.SOUTH);
        attackCoord.add(attackX);
        attackCoord.add(attackY);
        attackControl.add(attackCoord, BorderLayout.CENTER);

        JButton attackBtn = new JButton("Attack");
        attackControl.add(attackBtn, BorderLayout.SOUTH);
    }
    private void updateSelfBoard(){
        selfBoard.repaint();
    }

    private void updateOppoBoard(){
        selfBoard.repaint();
    }

    private void handleConnectServer(){
        System.out.println("Play clicked");
        selfBoardCells.get(15).setColor(-1);
        try{
            socket = new Socket("localhost", 1216);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        //status.append("Server connected");
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
