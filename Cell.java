public class Cell {
    private int x;
    private int y;
    private Point point;

    private boolean isMy;

    private int strength;
    private int production;

    private Direction moveDirection;
    private boolean movedTo;

    private int owner;

    public Cell(int x, int y, boolean isMy, int strength, int production, int owner) {
        this.x = x;
        this.y = y;
        this.point = new Point(x, y);
        this.isMy = isMy;
        this.strength = strength;
        this.production = production;
        this.owner = owner;
    }

    public Cell copy() {
        Cell copy = new Cell(x, y, isMy, strength, production, owner);
        copy.moveDirection = moveDirection;
        copy.movedTo = movedTo;
        return copy;
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

    public boolean isNeutral() {
        return owner == 0;
    }

    public boolean isEnemy() {
        return !isMy() && !isNeutral();
    }

    public int getOwner() {
        return owner;
    }

    public int getStrength() {
        return strength;
    }

    public int getProduction() {
        return production;
    }

    public Direction getMoveDirection() {
        return moveDirection;
    }

    public boolean isMoved() {
        return moveDirection != null;
    }

    public void registerMoveTo() {
        this.movedTo = true;
    }

    public boolean hasMoveTo() {
        return movedTo;
    }

    public void move(Direction direction) {
        this.moveDirection = direction;
    }

    public void handleMoveTo(Cell sourceCell) {

        if (point.equals(sourceCell.getPoint())) {
            strength += production;
            sourceCell.moveDirection = null;
        }
        else {

            if (isMy) {
                strength += sourceCell.strength;
            } else {
                int tmp = strength - sourceCell.strength;

                if (tmp > 0) {
                    strength = tmp;
                } else {
                    strength = -tmp;
                    isMy = true;
                }
            }

            sourceCell.strength = 0;
            sourceCell.moveDirection = null;
        }
    }
}
