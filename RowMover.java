import java.util.HashSet;
import java.util.Set;

public class RowMover {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;
    private int threshold;
    private boolean op;

    public RowMover(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler, int threshold, boolean op) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
        this.threshold = threshold;
        this.op = op;
    }

    public void execute() {
        Set<Integer> rows = findRowsWithMaxStrength();

        for (Integer row : rows) {

            if (!hasFreeSiteInRow(row)) {
                continue;
            }

            Set<Location> locationsToMove = gameHelper.mySitesOnRow(row);

            int sum = 0;
            for (Location l : locationsToMove) {
                sum += l.x;
            }
            int average = sum / locationsToMove.size();

            for (Location location : locationsToMove) {
                Direction d = Direction.WEST;

                if (op) {
                    d = Direction.EAST;
                }

                /*
                if (location.x > average) {
                    d = Direction.EAST;
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

    private boolean hasFreeSiteInRow(int row) {
        for (int x = 0; x < gameMap.width; x++){
            Location loc = new Location(x, row);
            Site site = gameMap.getSite(loc);

            if (site.owner != myID) {
                return true;
            }
        }
        return false;
    }

    private Set<Integer> findRowsWithMaxStrength() {
        Set<Integer> rows = new HashSet<>();
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Location loc = new Location(x, y);
                Site site = gameMap.getSite(loc);

                if (site.owner == myID && site.strength >= threshold) {
                    rows.add(loc.y);
                }
            }
        }
        return rows;
    }
}
