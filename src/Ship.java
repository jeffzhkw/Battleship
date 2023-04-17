public class Ship {
    private int length;
    private boolean isHorizontal;
    private int x;
    private int y;
    private boolean sunk;

    public Ship(int l){
        this.length = l; //2,3,4,5
        this.isHorizontal = true;
        this.x = -1; //range 0-9, -1 means unplaced
        this.y = -1; //range 0-9, -1 means unplaced
        this.sunk = false;
    }

    public void changeOrientation(){
        isHorizontal = !isHorizontal;
    }
    public boolean isValidPos(){
        if(isHorizontal){
            return x + length <= 10;
        }
        else { //isVertical
            return y + length <= 10;
        }
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
}
