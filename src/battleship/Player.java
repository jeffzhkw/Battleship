package battleship;

import battleship.Grid;
import battleship.Ship;

public class Player {
    private int id;
    private int life;
    private Grid self = new Grid(); // actual
    private Grid oppo = new Grid(); // view
    private Ship[] shipLst = new Ship[5];
    private int actionX;
    private int actionY;

    public Player(int id) {
        this.id = id;
        life = 5;
        int[] length = {2, 3, 3, 4, 5};
        for (int i = 0; i < 5; i ++) {
            shipLst[i] = new Ship(i, length[i]);
        }
        actionX = -1;
        actionY = -1;
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

    public int takeY(int y) {
        actionY = y;
        return actionY;
    }

    public int updateSelfByOppoMove(int x, int y) {
        int status = self.getStatus(x, y);
        // users are only allowed to fire at ocean (0 or 1) 
        if (status == 0) {
            status = self.setMiss(x, y);
        }
        else if (status == 1) {

            // deduct ship life
            int shipid = self.whichShip(x, y);
            shipLst[shipid].takeHit();
            status = self.setHit(x, y);
            // check sunk ?
            // sink => status = 3;
            if (shipLst[shipid].getLife() == 0) {
                life -= 1;
                return 3; // sunk
            }
        }
        else {
            return status;
        }
        return status;
    }

    public boolean updateOppoView(int x, int y, int status) {
        if (status == 1) {
            oppo.setMiss(x, y);
        }
        else if (status == 2) {
            oppo.setHit(x, y);
        }
        return true;
    }

}