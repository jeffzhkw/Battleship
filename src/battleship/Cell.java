package battleship;
public class Cell {
    // -1: miss
    // 0: unexplored: empty
    // 1: unexplored: there
    // 2: hit
    // 3: sink
    private int status;

    private int shipid; //valid id can only be 0, 1, 2, 3, 4

    public Cell() {
        status = 0;
        shipid = -1; //no ship present at Cell
    }

    //setStatus() doesn't handle invalid input check.
    public boolean setStatus(int status) {
        this.status = status;
        return true;
    }

    public int getStatus() {
        return status;
    }

    public boolean setShipid(int id) {
        if (shipid == -1){
            shipid = id;
            return true;
        }
        return false;
    }

    public int getShipid() {
        return shipid;
    }
}