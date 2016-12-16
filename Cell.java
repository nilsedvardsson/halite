public class Cell {
    private int x;
    private int y;
    private Point point;

    private boolean isMy;

    private int strength;
    private int production;

    private Direction moveDirection;

    public Cell(int x, int y, boolean isMy, int strength, int production) {
        this.x = x;
        this.y = y;
        this.point = new Point(x, y);
        this.isMy = isMy;
        this.strength = strength;
        this.production = production;
    }

    public Cell copy() {
        return new Cell(x, y, isMy, strength, production);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point getPoint() {
        return point;
    }

    public boolean isMy() {
        return isMy;
    }

    public int getStrength() {
        return strength;
    }

    public int getProduction() {
        return production;
    }

    public boolean isMoved() {
        return moveDirection != null;
    }

    public void move(Direction direction) {
        this.moveDirection = direction;
    }
}
