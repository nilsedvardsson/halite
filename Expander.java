public class Expander {

    private int myID;
    private GameMap gameMap;
    private MoveHandler moveHandler;

    public Expander(int myID, GameMap gameMap, MoveHandler moveHandler) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.moveHandler = moveHandler;
    }

    public void expand() {
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {

                Location loc = new Location(x, y);
                Site site = gameMap.getSite(loc);

                if (site.owner == myID) {

                    Direction dir = Direction.STILL;

                    Site northSite = northSite(loc);
                    Site eastSite = eastSite(loc);
                    Site southSite = southSite(loc);
                    Site westSite = westSite(loc);

                    if (northSite.owner != myID && site.strength > northSite.strength) {
                        dir = Direction.NORTH;
                    } else if (eastSite.owner != myID && site.strength > eastSite.strength) {
                        dir = Direction.EAST;
                    } else if (southSite.owner != myID && site.strength > southSite.strength) {
                        dir = Direction.SOUTH;
                    } else if (westSite.owner != myID && site.strength > westSite.strength) {
                        dir = Direction.WEST;
                    }

                    if (dir != Direction.STILL) {

                        Move move = new Move(loc, dir);

                        if (moveHandler.canBeAdded(move)) {
                            moveHandler.add(move);
                        }

                    }

                }

            }
        }
    }

    private Site northSite(Location loc) {
        return gameMap.getSite(loc, Direction.NORTH);
    }

    private Site eastSite(Location loc) {
        return gameMap.getSite(loc, Direction.EAST);
    }

    private Site southSite(Location loc) {
        return gameMap.getSite(loc, Direction.SOUTH);
    }

    private Site westSite(Location loc) {
        return gameMap.getSite(loc, Direction.WEST);
    }
}
