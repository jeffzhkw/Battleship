import java.util.ArrayList;
import java.util.Arrays;

public class Grid {
    private int[][] state = new int[10][10];
    private ArrayList<Ship> shipLst = new ArrayList<Ship>();


    public Grid(){
        for (int i =0; i < 10; i++){
            //init to empty board
            Arrays.fill(state[i], 0);
        }
    }

    public boolean placeShip(Ship s){
        //TODO: shipToGrid translation

        //TODO: check for ship collisions that's already on the board.

        //if success
        shipLst.add(s);
        return true;
    }



    public boolean checkWin(){
        for(Ship s : shipLst){
            if (!s.isSunk()){
                return false;
            }
        }
        return true;
    }
}
