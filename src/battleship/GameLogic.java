package battleship;

import java.util.Scanner;

import battleship.Player;

public class GameLogic {
    Player p1 = new Player(1);
    Player p2 = new Player(2);

    boolean isP1Move = true;

    public Player getPlayerWithId(int id){
        if(id == 1){
            return p1;
        }
        else if(id ==2 ){
            return p2;
        }
        return null;
    }


    public void initGame(){
        System.out.println("==========start=============\n");
        // take turns to place ship
        Scanner myInput = new Scanner(System.in);
        int shipid = 0;
        // can be abstracted to be a function
        while (isP1Move) {
            // player 1 place ships
            int x = -1, y = -1;
            do {
                p1.displayBoard();
                if (x != -1 && y != -1) {
                    System.out.println("(" + x + ", " + y + ") is invalid position");
                }
                System.out.println("Player 1's shipid = " + shipid + " wait to be placed: ");
                x = myInput.nextInt();
                y = myInput.nextInt();
                int setHorizontal = myInput.nextInt();
                if (setHorizontal == 1) {
                    p1.setHorizontal(shipid, true);
                }
                else {
                    p1.setHorizontal(shipid, false);
                }

            } while (p1.placeShip(shipid, x, y) == false);
            shipid ++;
            if (shipid >= 5) {
                shipid = 0;
                isP1Move = !isP1Move;
            }
        }
        p1.displayBoard();
        while (isP1Move == false) {
            int x = -1, y = -1;
            do {
                p2.displayBoard();
                if (x != -1 && y != -1) {
                    System.out.println("(" + x + ", " + y + ") is invalid position");
                }
                System.out.println("Player 2's shipid = " + shipid + " wait to be placed: ");
                x = myInput.nextInt();
                y = myInput.nextInt();
                int setHorizontal = myInput.nextInt();
                if (setHorizontal == 1) {
                    p2.setHorizontal(shipid, true);
                }
                else {
                    p2.setHorizontal(shipid, false);
                }

            } while (p2.placeShip(shipid, x, y) == false);
            shipid ++;
            if (shipid >= 5) {
                shipid = 0;
                isP1Move = !isP1Move;
            }
        }
        p2.displayBoard();
        gameStateMachine();
    }
    public void takeMove(Player self, Player oppo) {
        System.out.println("==========Round : " + self.getId() + "=============\n");
        // take turns to place ship
        Scanner myInput = new Scanner(System.in);
        self.displayBoard();
        System.out.println("Player " + self.getId() + " shot wait to be placed: ");
        int x = myInput.nextInt();
        int y = myInput.nextInt();
        // assume currently player will only fire at cell (status == 0 at oppoView, which means it's either ocean or ship)
        // status = 1(miss) or 2(hit) or 3(one ship sinks)
        int status = oppo.updateSelfByOppoMove(x, y);
        // when miss or one ship sinks switch the turn
        if (status == 1 || status == 3) {
            isP1Move = !isP1Move;
        }
        // set sink to be hit to display
        if (status == 3) status = 2;
        // update opponent view in current player
        self.updateOppoView(x, y, status);
        self.displayBoard();
    }
    public void gameStateMachine() {
        boolean init_player = isP1Move;
        Player self = p1;
        Player oppo = p2;
        while (isEndGame() == false) {
            takeMove(self, oppo);
            if (isP1Move == false) {
                self = p2;
                oppo = p1;
            }
            else {
                self = p1;
                oppo = p2;
            }
        }
    }

    public boolean isEndGame() {
        return p1.getLife() == 0 || p2.getLife() == 0;
    }
}
