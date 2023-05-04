package battleship;

import java.util.ArrayList;
import java.util.Arrays;

import battleship.Cell;
import battleship.Ship;

public class Grid {
    private Cell[][] state = new Cell[10][10];

    public Grid(){
        for (int i = 0; i < 10; i ++) {
            //init to empty board
            for (int j = 0; j < 10; j ++) {
                state[i][j] = new Cell();// init to status =0, shipid = -1
            }
        }
    }

    // set for debugging purpose
    public void displayGrid() {
        for (int i = 0; i < 10; i ++) {
            for (int j = 0; j < 10; j ++) {
                System.out.print(state[i][j].getStatus() + " ");
            }
            System.out.print("\n");
        }
    }

    public int getStatus(int x, int y) {
        return state[x][y].getStatus();
    }

    public boolean isValidPos(Ship s, int x, int y) {
        // exceed boundary
        if (x < 0 || x >= 10 || y < 0 || y >= 10) {
            return false;
        }
        boolean isHorizontal = s.isHorizontal();
        int length = s.getLength();
        boolean canPlaced = true;
        int i = 0;
        // check if it is a valid position
        if (isHorizontal) {
            // offset on y
            for (; i < length && y + i < 10; i ++) {
                if (state[x][y + i].getStatus() != 0) { // not ocean
                    canPlaced = false;
                    break;
                }
            }
        }
        else {
            // offset on x
            for (; i < length && x + i < 10; i ++) {
                if (state[x + i][y].getStatus() != 0) { // not ocean
                    canPlaced = false;
                    break;
                }
            }
        }
        if (i < length) { // encounter ship or exceed boundary
            canPlaced = false;
        }
        //if success
        return canPlaced;
    }
    public boolean setShip(Ship s, int x, int y) {
        s.setPos(x, y);
        int length = s.getLength();
        System.out.println("length = " + length);
        if (s.isHorizontal()) {
            // set horizontally
            for (int i = 0; i < length; i ++) {
                state[x][y + i].setStatus(1);
                state[x][y + i].setShipid(s.getShipid());
            }
        }
        else {
            // set vertically
            for (int i = 0; i < length; i ++) {
                state[x + i][y].setStatus(1);
                state[x + i][y].setShipid(s.getShipid());
            }
        }
        return true;
    }

    public int whichShip(int x, int y) {
        return state[x][y].getShipid();
    }
    // only allow users to fire on cell where grid status = 0 or 1
    public int setMiss(int x, int y) {
        state[x][y].setStatus(-1); // miss
        return 1;
    }

    public int setHit(int x, int y) {
        state[x][y].setStatus(2);
        state[x][y].setShipid(-1);
        return 2;
    }

    public int[][] getData(){
        int[][] result = new int[10][10];
        for (int i =0; i< 10; i++){
            for (int j = 0; j < 10; j++){
                result[i][j] = this.state[i][j].getStatus();
            }
        }
        return result;
    }

    public int[][] getShipId(){
        int[][] result = new int[10][10];
        for (int i =0; i< 10; i++){
            for (int j = 0; j < 10; j++){
                result[i][j] = this.state[i][j].getShipid();
            }
        }
        return result;
    }

}
