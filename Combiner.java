import java.util.Set;

public class Combiner {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;


    public Combiner(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
    }

    public void execute() {
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Location location = new Location(x, y);
                Site site = gameMap.getSite(location);

                if (site.owner == myID) {

                    Site nortSite = gameMap.getSite(location, Direction.NORTH);

                    if (nortSite.owner != myID) {

                        Site westSite = gameMap.getSite(location, Direction.WEST);
                        Site eastSite = gameMap.getSite(location, Direction.EAST);
                        Site southSite = gameMap.getSite(location, Direction.SOUTH);

                        if (westSite.owner == myID) {

                            Move move = new Move(gameMap.getLocation(location, Direction.WEST), Direction.EAST);
                            if (moveHandler.canBeAdded(move)) {
                                moveHandler.add(move);
                            }
                        }
                    }
                }
            }
        }
    }

    private Move reverse(Move move) {
        Location location = gameMap.getLocation(move.loc, move.dir);
        return new Move(location, opposite(move.dir));
    }

    private Direction opposite(Direction direction) {
        if (direction == Direction.NORTH) {
            return Direction.SOUTH;
        }
        else if (direction == Direction.SOUTH) {
            return Direction.NORTH;
        }
        else if (direction == Direction.EAST) {
            return Direction.WEST;
        }
        else if (direction == Direction.WEST) {
            return Direction.EAST;
        }
        else {
            return Direction.STILL;
        }
    }

}
