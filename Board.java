import java.util.*;
import java.util.stream.Collectors;

public class Board {

    private Board parent;
    private Map<Point, Cell> cells;
    private GameMap gameMap;

    private Set<Cell> myCells;

    public Board(Board parent, int myID, GameMap gameMap) {
        this.gameMap = gameMap;
        cells = new HashMap<>();
        myCells = new HashSet<>();

        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {

                Location loc = new Location(x, y);
                Site site = gameMap.getSite(loc);

                Point point = new Point(x, y);
                Cell cell = new Cell(x, y, site.owner == myID, site.strength, site.production);

                cells.put(point, cell);

                if (cell.isMy()) {
                    myCells.add(cell);
                }
            }
        }
    }

    private Board() {
        cells = new HashMap<>();
        myCells = new HashSet<>();
    }

    public Board makeChild() {
        Board child = copy();
        child.parent = this;
        return child;
    }

    public Board copy() {
        Board copy = new Board();
        copy.parent = parent;
        copy.gameMap = gameMap;

        for (Point p : cells.keySet()) {
            Cell cellCopy = cells.get(p).copy();

            copy.cells.put(p, cellCopy);
            if (cellCopy.isMy()) {
                copy.myCells.add(cellCopy);
            }
        }

        return copy;
    }

    public List<Cell> getMyCells() {
        return new ArrayList<>(myCells);
    }

    public List<Cell> getUnmovedCells() {
        return getMyCells().stream().filter(c -> !c.isMoved()).collect(Collectors.toList());
    }

    public boolean hasUnmovedCells() {
        return !getUnmovedCells().isEmpty();
    }

    public Cell getAnyUnmovedCell() {
        return getUnmovedCells().get(0);
    }

    public Board move(Point point, Direction direction) {
        Cell c = cells.get(point);
        c.move(direction);
        return this;
    }

    public boolean isRoot() {
        return parent == null;
    }

    public void simulateNextFrame() {
        Set<Cell> toBeAdded = new HashSet<>();
        for (Cell cell : getMyCells()) {
            Cell target = getCell(cell.getPoint(), cell.getMoveDirection());

            boolean isMyBeforeMove = target.isMy();
            target.handleMoveTo(cell);
            boolean isMyAfterMove = target.isMy();

            if (!isMyBeforeMove && isMyAfterMove) {
                toBeAdded.add(target);
            }
        }
        myCells.addAll(toBeAdded);
    }

    private Cell getCell(Point point, Direction direction) {
        return getCell(getPoint(point, direction));
    }

    private Point getPoint(Point point, Direction direction) {
        int x = point.getX();
        int y = point.getY();

        if (direction == Direction.NORTH) {
            y = (y == 0 ? gameMap.height - 1 : y - 1);
        }
        else if (direction == Direction.SOUTH) {
            y = (y == gameMap.height - 1 ? 0 : y + 1);
        }
        else if (direction == Direction.EAST) {
            x = (x == gameMap.width - 1 ? 0 : x + 1);
        }
        else if (direction == Direction.WEST) {
            x = (x == 0 ? gameMap.width - 1 : x - 1);
        }

        return new Point(x, y);
    }

    private Cell getCell(Point point) {
        return cells.get(point);
    }

    public int getProduction() {
        return getMyCells().stream().mapToInt(c -> c.getProduction()).sum();
    }

    public int getStrength() {
        return getMyCells().stream().mapToInt(c -> c.getStrength()).sum();
    }

    public Board getParent() {
        return parent;
    }
}
