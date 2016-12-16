public class Radial {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;

    public Radial(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
    }

    public void execute() {
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {

                Location loc = new Location(x, y);
                Site site = gameMap.getSite(loc);

                if (site.owner != myID) {
                    continue;
                }

                if (!gameHelper.isCoreBuddy(loc)) {
                    continue;
                }

                if (site.strength > 30) {
                    Direction dir = getNearestToOvertake(loc);

                    Move move = new Move(loc, dir);
                    if (moveHandler.canBeAdded(move)) {
                        moveHandler.add(move);
                    }
                }
            }
        }
    }

    private Direction getNearestToOvertake(Location loc) {
        int n = 0;
        int e = 0;
        int s = 0;
        int w = 0;

        Location l = loc;

        while ((gameMap.getSite(l, Direction.NORTH).owner == myID) && (n < 50)) {
            n++;
            l = gameMap.getLocation(l, Direction.NORTH);
        }

        l = loc;

        while ((gameMap.getSite(l, Direction.EAST).owner == myID) && (e < 50)) {
            e++;
            l = gameMap.getLocation(l, Direction.EAST);
        }

        l = loc;

        while ((gameMap.getSite(l, Direction.SOUTH).owner == myID) && (s < 50)) {
            s++;
            l = gameMap.getLocation(l, Direction.SOUTH);
        }

        l = loc;

        while ((gameMap.getSite(l, Direction.WEST).owner == myID) && (w < 50)) {
            w++;
            l = gameMap.getLocation(l, Direction.WEST);
        }

        Direction dir = Direction.NORTH;
        int currentBest = n;

        if (e < currentBest) {
            dir = Direction.EAST;
            currentBest = e;
        }

        if (s < currentBest) {
            dir = Direction.SOUTH;
            currentBest = s;
        }

        if (w < currentBest) {
            dir = Direction.WEST;
            currentBest = w;
        }

        return dir;
    }

}
