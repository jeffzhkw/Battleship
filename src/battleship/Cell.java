package battleship;
public class Cell {
    private int status;
    private int shipid;

    public Cell() {
        status = 0;
        shipid = -1;
    }

    public boolean setStatus(int status) {
        this.status = status;
        return true;
    }

    public int getStatus() {
        return status;
    }

    public boolean setShipid(int id) {
        shipid = id;
        return true;
    }

    public int getShipid() {
        return shipid;
    }
}