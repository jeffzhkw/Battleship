package battleship;
public class Ship {
    private int shipid;
    private int length;
    private boolean isHorizontal;
    private int x;
    private int y;
    private int life;
    private boolean sunk;
    private boolean placed;

    public Ship(int id, int l) {
        this.shipid = id;
        this.length = l; //2,3,4,5
        this.isHorizontal = true;
        this.x = -1; //range 0-9, -1 means unplaced
        this.y = -1; //range 0-9, -1 means unplaced
        this.life = l;
        this.sunk = false;
        this.placed = false;
    }

    public int getShipid() {
        return shipid;
    }

    public void changeOrientation(){
        isHorizontal = !isHorizontal;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public void setHorizontal(boolean horizontal) {
        isHorizontal = horizontal;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isSunk() {
        return sunk;
    }

    public void setSunk(boolean sunk) {
        this.sunk = sunk;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    public int getLife() {
        return life;
    }
    public void takeHit() {
        if (--life == 0) {
            setSunk(true);
        }
    }
}
