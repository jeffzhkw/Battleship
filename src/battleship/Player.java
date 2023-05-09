package battleship;

import battleship.Grid;
import battleship.Ship;
import java.io.Serializable;

public class Player implements  Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int life;
    public Grid self = new Grid(); // actual
    public Grid oppo = new Grid(); // view
    private Ship[] shipLst = new Ship[5];
    private int actionX;
    private int actionY;
    private boolean ableToMove;

    public Player(int id) {
        this.id = id;
        life = 5;
        int[] length = {2, 3, 3, 4, 5};
        for (int i = 0; i < 5; i ++) {
            shipLst[i] = new Ship(i, length[i]);
        }
        actionX = -1;
        actionY = -1;
        ableToMove = false;
    }


    public int setId(int id) {
        this.id = id;
        return id;
    }
    public int getId() {
        return id;
    }
    public int getLife() {
        return life;
    }
    // for debugging purpose
    public void displayBoard() {
        System.out.println("===================" + id + "====================\n");
        System.out.println("self: \n");
        self.displayGrid();
        System.out.println("oppo: \n");
        oppo.displayGrid();
    }
    public boolean isAbleToMove(){return ableToMove;}
    public void setAbleToMove(boolean isAbleToMove){this.ableToMove = isAbleToMove;}

    public int getGridStatusAt(int x, int y){
        return self.getStatus(x, y);
    }

    public Grid getSelfGrid(){
        return self;
    }

    public void replaceShipLstWith(Ship[] sLst){
        shipLst = sLst;
    }

    public Grid getOppoView(){
        return oppo;
    }
    public void setHorizontal(int shipid, boolean horizontal) {
        shipLst[shipid].setHorizontal(horizontal);
    }

    public boolean placeShip(int shipid, int x, int y) {
        boolean isValidPos = self.isValidPos(shipLst[shipid], x, y);
        if (isValidPos) {
            System.out.println("place ship");
            return self.setShip(shipLst[shipid], x, y);
        }
        return isValidPos;
    }

    public int takeX(int x) {
        actionX = x;
        return actionX;
    }

    public int getX() {
        return actionX;
    }

    public int takeY(int y) {
        actionY = y;
        return actionY;
    }

    public int getY() {
        return actionY;
    }

    public int updateSelfByOppoMove(int x, int y) {
        int status = self.getStatus(x, y);
        // users are only allowed to fire at ocean (0 or 1) 
        if (status == 0) {
            status = self.setMiss(x, y);
            // -1
        }
        else if (status == 1) {

            // deduct ship life
            int shipid = self.whichShip(x, y);
            shipLst[shipid].takeHit();
            status = self.setHit(x, y);
            System.out.println(shipid + " => " + status);
            // check sunk ?
            // sink => status = 3;
            if (shipLst[shipid].getLife() == 0) {
                life -= 1;
                // turn current to status 3
                self.setSunk(x, y);
                return self.setSunk(x, y);
            }
        }
        return status;
    }

    public boolean updateOppoView(int x, int y, int status) {
        if (status == -1) {
            oppo.setMiss(x, y);
        }
        else if (status == 2) {
            oppo.setHit(x, y);
        }
        return true;
    }

    public boolean updateOppoSunk(Cell[][] oppoBoard, int id) {
        oppo.setOppoSunk(oppoBoard, id);
        return true;
    }

    public void printShipList() {
        for (int i = 0; i < 5; i ++) {
            System.out.println("id: " + shipLst[i].getShipid() + ", x: " + shipLst[i].getX() + ", y : " + shipLst[i].getY() + " , length : " + shipLst[i].getLength());
        }
    }

}