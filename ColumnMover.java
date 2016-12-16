import java.util.HashSet;
import java.util.Set;

public class ColumnMover {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;
    private int threshold;
    private boolean op;

    public ColumnMover(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler, int threshold, boolean op) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
        this.threshold = threshold;
        this.op = op;
    }

    public void execute() {
        Set<Integer> columns = findColumnsWithMaxStrength();

        for (Integer column : columns) {

            if (!hasFreeSiteInColumn(column)) {
                continue;
            }

            Set<Location> locationsToMove = gameHelper.mySitesOnColumn(column);

            int sum = 0;
            for (Location l : locationsToMove) {
                sum += l.y;
            }
            int average = sum / locationsToMove.size();

            for (Location location : locationsToMove) {
                Direction d = Direction.NORTH;

                if (op) {
                    d = Direction.SOUTH;
                }

                /*
                if (location.y > average) {
                    d = Direction.SOUTH;
                }
                */

                Move move = new Move(location, d);
                if (moveHandler.canBeAdded(move)) {
                    moveHandler.add(move);
                }
            }
        }
    }

    private boolean hasSite(int x, int y) {
        Location location = new Location(x, y);
        Site site = gameMap.getSite(location);
        return site.owner == myID;
    }

    private boolean hasFreeSiteInColumn(int column) {
        for (int y = 0; y < gameMap.height; y++){
            Location loc = new Location(column, y);
            Site site = gameMap.getSite(loc);

            if (site.owner != myID) {
                return true;
            }
        }
        return false;
    }

    private Set<Integer> findColumnsWithMaxStrength() {
        Set<Integer> columns = new HashSet<>();
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Location loc = new Location(x, y);
                Site site = gameMap.getSite(loc);

                if (site.owner == myID && site.strength >= threshold) {
                    columns.add(loc.x);
                }
            }
        }
        return columns;
    }

}
