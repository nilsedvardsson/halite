import java.util.*;

public class Board {

    private Board parent;
    // private Map<Point, Cell> rootCells;
    private Map<Point, Cell> cells;
    private GameMap gameMap;

    private Set<Cell> myCells;

    public Board(Board parent, int myID, GameMap gameMap) {
        this.gameMap = gameMap;
        cells = new HashMap<>();
        myCells = new HashSet<>();
        // rootCells = new HashMap<>();

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
        // copy.rootCells = rootCells;

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

    public boolean hasCellsToMove() {
        return getAnyCellToMove() == null ? false : true;
    }

    public Cell getAnyCellToMove() {
        for (Cell cell : myCells) {
            if (!cell.isMoved()) {
                return cell;
            }
        }
        return null;
    }

    public Board move(Point point, Direction direction) {
        Cell c = getCell(point);
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

    /*
    public Board getRoot() {
        if (isRoot()) {
            return this;
        } else {
            return getParent().getRoot();
        }
    }
    */

    public int getProduction() {
        return myCells.stream().mapToInt(c -> c.getProduction()).sum();
        // return getMyCells().stream().mapToInt(c -> c.getProduction()).sum();
    }

    public int getStrength() {
        return myCells.stream().mapToInt(c -> c.getStrength()).sum();
        // return getMyCells().stream().mapToInt(c -> c.getStrength()).sum();
    }

    public Board getParent() {
        return parent;
    }

    public void freeze() {
        for (Cell cell : myCells) {
            if (!cell.isMoved() && cell.getStrength() < 3 * cell.getProduction()) {
                cell.move(Direction.STILL);
            }
        }
    }
}
