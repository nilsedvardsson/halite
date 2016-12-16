import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Board {

    private Map<Point, Cell> cells;

    public Board(int myID, GameMap gameMap) {
        cells = new HashMap<>();

        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {

                Location loc = new Location(x, y);
                Site site = gameMap.getSite(loc);

                Point point = new Point(x, y);
                Cell cell = new Cell(x, y, site.owner == myID, site.strength, site.production);

                cells.put(point, cell);
            }
        }
    }

    private Board() {
        cells = new HashMap<>();
    }

    public Board copy() {
        Board copy = new Board();

        for (Point p : cells.keySet()) {
            Cell cellCopy = cells.get(p).copy();
            copy.cells.put(p, cellCopy);
        }

        return copy;
    }

    public List<Cell> getMyCells() {
        return cells.values().stream().filter(c -> c.isMy()).collect(Collectors.toList());
    }

    public List<Cell> getUnmovedCells() {
        return cells.values().stream().filter(c -> c.isMy() && !c.isMoved()).collect(Collectors.toList());
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
}
