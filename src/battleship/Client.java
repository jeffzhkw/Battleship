package battleship;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client extends JFrame implements Runnable{
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
    private JTextArea status;
    private JTextField placeRowValue = new JTextField();
    private JTextField placeColValue =new JTextField();
    private JTextField attackRowValue =new JTextField();
    private JTextField attackColValue =new JTextField();
    private JButton placeBtn;
    private JButton attackBtn;
    private JCheckBox isHorizontal = new JCheckBox("Is Horizontal? ");
    private ArrayList<BoardCell> selfBoardCells = new ArrayList<BoardCell>();//Hold reference of BoardCells(GUI) to change color
    private ArrayList<BoardCell> oppoBoardCells = new ArrayList<BoardCell>();
    //Game Data
    private Player player = new Player(-1);
    private Ship[] shipLst = new Ship[5];
    private int placedShipNum = -1;
    //Networking
    private Socket socket = null;
    private ObjectOutputStream objectOutputStream = null;
    private ObjectInputStream objectInputStream = null;

    public Client(){
        super("Battleship");
        initGUI();
        getContentPane().repaint();
        status.append("~ Welcome, start placing ships by click Play from menu bar\n" +
                "~ Then, click Connect to play with others.\n");
    }
    //------------GUIs------------//
    private void initGUI(){
        //create menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        JMenuItem exitItem = new JMenuItem("Exit");
        JMenuItem connectItem = new JMenuItem("Connect");
        JMenuItem playItem = new JMenuItem("Play");
        exitItem.addActionListener((e) -> System.exit(0));
        connectItem.addActionListener((e)-> handleConnectServer());
        playItem.addActionListener((e)-> handleInitShipPlacement());
        menu.add(playItem);
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
        JButton clearBtn = new JButton("Clear History");
        clearBtn.addActionListener((e)->{status.setText("");});
        center.add(clearBtn);
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
        status = new JTextArea();
        //status.setMaximumSize(new Dimension(250, 580));
        status.setLineWrap(true);
        status.setWrapStyleWord(true);
        center.add(new JScrollPane(status));
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
        placeX.add(placeRowValue,BorderLayout.SOUTH);
        JPanel placeY = new JPanel();
        placeY.setLayout(new BorderLayout());
        placeY.add(new JLabel("Place at Column"), BorderLayout.NORTH);
        placeY.add(placeColValue,BorderLayout.SOUTH);
        placeCoord.add(placeX);
        placeCoord.add(placeY);
        placeCoord.add(isHorizontal);
        placeControl.add(placeCoord, BorderLayout.NORTH);

        placeBtn = new JButton("Place");
        placeBtn.addActionListener((e)->handleOnPlace());
        placeBtn.setEnabled(false);
        placeControl.add(placeBtn, BorderLayout.SOUTH);

        //---------------------------------------
        attackControl = new JPanel();
        JPanel attackCoord = new JPanel();
        attackControl.setLayout(new BorderLayout());
        attackCoord.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 0));
        JPanel attackX = new JPanel();
        attackX.setLayout(new BorderLayout());
        attackX.add(new JLabel("Attack at Row"),BorderLayout.NORTH);
        attackX.add(attackRowValue,BorderLayout.SOUTH);
        JPanel attackY = new JPanel();
        attackY.setLayout(new BorderLayout());
        attackY.add(new JLabel("Attack at Column"),BorderLayout.NORTH);
        attackY.add(attackColValue,BorderLayout.SOUTH);
        attackCoord.add(attackX);
        attackCoord.add(attackY);
        attackControl.add(attackCoord, BorderLayout.CENTER);

        attackBtn = new JButton("Attack");
        attackBtn.addActionListener((e)->handleOnAttack());
        attackBtn.setEnabled(false);
        attackControl.add(attackBtn, BorderLayout.SOUTH);
    }
    private void updateSelfBoard(){
        int i = 0;
        for (int [] arr : player.getSelfGrid().getAllStatus()){
            for (int val: arr){
                selfBoardCells.get(i).setColor(val);
                i++;
            }
        }
        selfBoard.repaint();
    }
    private void updateOppoBoard(){
        int i = 0;
        for (int [] arr : player.getOppoView().getAllStatus()){
            for (int val: arr){
                oppoBoardCells.get(i).setColor(val);
                i++;
            }
        }
       oppoBoard.repaint();
    }
    private void handleInitShipPlacement(){
    if (placedShipNum == -1){ placedShipNum = 0;}
    placeBtn.setEnabled(true);
    if(placedShipNum == 0){
        status.append("~ Place your length 2 ship\n");
        shipLst[placedShipNum] = new Ship(placedShipNum, 2);
    }
    else if(placedShipNum == 1){
        status.append("~ Place your first length 3 ship\n");
        shipLst[placedShipNum] = new Ship(placedShipNum, 3);
    }
    else if(placedShipNum == 2){
        status.append("~ Place your second length 3 ship\n");
        shipLst[placedShipNum] = new Ship(placedShipNum, 3);
    }
    else if(placedShipNum == 3){
        status.append("~ Place your length 4 ship\n");
        shipLst[placedShipNum] = new Ship(placedShipNum, 4);
    }
    else if(placedShipNum == 4){
        status.append("~ Place your length 5 ship\n");
        shipLst[placedShipNum] = new Ship(placedShipNum, 5);
    }
    else if(placedShipNum == 5){
        status.append("~ All Ship placed, you may connect! \n");
        placeBtn.setEnabled(false);
    }
}
    private void handleOnPlace(){
        int x = -1;
        int y = -1;
        boolean isH = isHorizontal.isSelected();;
        try{
            x= Integer.parseInt(placeRowValue.getText().trim())-1;
            y = Integer.parseInt(placeColValue.getText().trim())-1;
            placeRowValue.setText("");
            placeColValue.setText("");
        }
        catch (Exception e){
            status.append("Invalid Input: please enter 1-10. \n");
            return;
        }
        //if not in range
        if (x < 0 || x > 9 || y < 0 || y > 9){
            status.append("Invalid Input Range.\n");
            return;
        }
        Ship temp = shipLst[placedShipNum];
        temp.setHorizontal(isH);
        temp.setX(x);
        temp.setY(y);
        status.append("Placing shipId "+ temp.getShipid()+" at (" + (x+1) +", " +(y+1)+")...\n");

        if (!player.getSelfGrid().isValidPos(temp, x,y)) { //not valid Pos
            status.append("Invalid Position\n");
            return;
        }
        player.getSelfGrid().setShip(temp, x, y);
        placedShipNum++;
        updateSelfBoard();

        handleInitShipPlacement();

    }
    private void handleOnAttack(){
        System.out.println("Attack clicked");
        int x = -1; // 0-9 representation on Grid
        int y = -1;
        try{//check if integer
            x = Integer.parseInt(attackRowValue.getText().trim())-1;//entry value-1 = grid
            y = Integer.parseInt(attackColValue.getText().trim())-1;
            attackRowValue.setText("");
            attackColValue.setText("");
        }
        catch (Exception e){
            status.append("Invalid Input.\n");
            return;
        }
        //if not in range
        if (x < 0 || x > 9 || y < 0 || y > 9){
            status.append("Invalid Input Range.\n");
            return;
        }
        // if target cell is explored
        if (player.getGridStatusAt(x, y) == -1 || player.getGridStatusAt(x, y) == 2){
            status.append("Invalid attack position ("+ (x+1) +", " +(y+1)+ "): Already explored.\n");
            return;
        }
        //set actionX/Y using takeX/Y
        status.append("Attacking enemy grid at (" + (x+1) +", " +(y+1)+")...\n");
        player.takeX(x);
        player.takeY(y);
        try{//assume objectOutputStream is not null
            objectOutputStream.writeObject(player);
        }
        catch(IOException e){
            e.printStackTrace();
            status.append("Error Sending data to server...");
            return;
        }
        //After a successful attack,
        //disable button until updated Player object gets sent over and re-enable at that time
        attackBtn.setEnabled(false);
    }
    //------------Networking------------//
    private void handleConnectServer(){
        System.out.println("Connect clicked");
        if (placedShipNum !=5 ){
            status.append("~ Ships not yet finished placing.\n");
            return;
        }
        if (socket != null){
            status.append("~ Server already connected.");
            return;
        }
        player.replaceShipLstWith(shipLst);
        try{
            socket = new Socket("localhost", 1216);
            status.append("~ Success: Server connected.\n");
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            //TODO: the listening thread starts here after successful connection.
            new Thread(this).start();
        }
        catch(IOException e){
            e.printStackTrace();
            status.append("Failed: Server not connected. Make sure server is running\n");
            return;
        }
        try{//Send player with ready Grid and shipLst.
            objectOutputStream.writeObject(player);
        }
        catch (IOException e){
            status.append("Error sending game init\n");
        }
    }

    public void replacePlayerObj(Player p){
        //Overwrite whole player object
        attackBtn.setEnabled(p.isAbleToMove());
        updateSelfBoard();
        updateOppoBoard();
        player = p;
    }
    @Override
    public void run() {
        while(true){
            try {
                //TODO: confirm readObject blocks the operation.
                System.out.println("run");
                Player temp = (Player) objectInputStream.readObject();
                System.out.println(temp.getId());
                System.out.println(temp.isAbleToMove());
                temp.displayBoard();
                attackBtn.setEnabled(temp.isAbleToMove());

                replacePlayerObj(temp);
            }
            catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args){
        Client c = new Client(); // Threading start after Connect in the menu is hit.
    }



}
