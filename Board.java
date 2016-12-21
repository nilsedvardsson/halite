import java.util.*;

public class Board {

    private Map<Point, Cell> cells;
    private GameMap gameMap;

    private Set<Cell> myCells;

    public Board(int myID, GameMap gameMap) {
        this.gameMap = gameMap;
        cells = new HashMap<>();
        myCells = new HashSet<>();

        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {

                Location loc = new Location(x, y);
                Site site = gameMap.getSite(loc);

                Point point = new Point(x, y);
                Cell cell = new Cell(x, y, site.owner == myID, site.strength, site.production, site.owner);

                cells.put(point, cell);

                if (cell.isMy()) {
                    myCells.add(cell);
                }
            }
        }
    }

    public List<Cell> getMyCells() {
        return new ArrayList<>(myCells);
    }

    public Board move(Point point, Direction direction) {
        Cell c = getCell(point);
        c.move(direction);
        return this;
    }

    public void move(Cell cell, Direction direction) {
        cell.move(direction);
        Cell target = getCell(cell, direction);
        target.registerMoveTo();
    }

    public void move(Cell from, Cell to) {
        move(from, getDirection(from, to));
    }

    public Cell getCell(Point point, Direction direction) {
        return getCell(getPoint(point, direction));
    }

    public Cell getCell(Cell cell, Direction direction) {
        return getCell(cell.getPoint(), direction);
    }

    public Point getPoint(Point point, Direction direction) {
        int x = point.getX();
        int y = point.getY();

        if (direction == Direction.NORTH) {
            y = (y == 0 ? gameMap.height - 1 : y - 1);
        } else if (direction == Direction.SOUTH) {
            y = (y == gameMap.height - 1 ? 0 : y + 1);
        } else if (direction == Direction.EAST) {
            x = (x == gameMap.width - 1 ? 0 : x + 1);
        } else if (direction == Direction.WEST) {
            x = (x == 0 ? gameMap.width - 1 : x - 1);
        }

        return new Point(x, y);
    }

    private Cell getCell(Point point) {
        return cells.get(point);
    }

    public int getProduction() {
        return myCells.stream().mapToInt(c -> c.getProduction()).sum();
    }

    public int getStrength() {
        return myCells.stream().mapToInt(c -> c.getStrength()).sum();
    }

    public Direction getDirection(Cell from, Cell to) {
        for (Direction direction : Direction.CARDINALS) {
            if (getCell(from, direction) == to) {
                return direction;
            }
        }
        return null;
    }
}
