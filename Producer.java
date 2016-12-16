import java.util.HashSet;
import java.util.Set;

public class Producer {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;

    public Producer(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler) {
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

                if (site.owner == myID) {

                    Set<Location> meAndMyGoodNeighbours = getGoodNeighbours(loc);
                    meAndMyGoodNeighbours.add(loc);

                    Location best = getLocationForMaxReachableProduction(meAndMyGoodNeighbours);

                    if (best == null) {
                        // not a boundary site
                        continue;
                    }

                    if (same(loc, best)) {
                        Location target = getLocationForMaxProduction(getNeutralNeighbours(loc));
                        Site targetSite = gameMap.getSite(target);

                        if (site.strength > targetSite.strength) {

                            Move move = new Move(loc, gameHelper.getDirection(loc, target));
                            if (moveHandler.canBeAdded(move)) {
                                moveHandler.add(move);
                            }
                        }

                    }
                    else {

                        if (site.strength > 40) {

                            Move move = new Move(loc, gameHelper.getDirection(loc, best));

                            if (moveHandler.canBeAdded(move)) {
                                moveHandler.add(move);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean same(Location location1, Location location2) {
        if (location1 == null || location2 == null) {
            return false;
        }

        return location1.x == location2.x && location1.y == location2.y;
    }

    private Location getLocationForMaxReachableProduction(Set<Location> locations) {
        Location bestLocation = null;
        int maxProduction = 0;

        for (Location location : locations) {
            int tmp = getMaxProductionForNeutralNeighbours(location);

            if (tmp > maxProduction) {
                tmp = maxProduction;
                bestLocation = location;
            }
        }

        return bestLocation;
    }

    private int getMaxProductionForNeutralNeighbours(Location location) {
        Location loc = getLocationForMaxProduction(getNeutralNeighbours(location));
        if (loc == null) {
            return 0;
        }
        return gameMap.getSite(loc).production;
    }

    private Location getLocationForMaxProduction(Set<Location> locations) {
        int maxProduction = 0;
        Location locationForMaxProduction = null;

        for (Location location : locations) {
            Site site = gameMap.getSite(location);

            if (site.production > maxProduction) {
                maxProduction = site.production;
                locationForMaxProduction = location;
            }
        }

        return locationForMaxProduction;
    }



    private int production(Location location) {
        return gameMap.getSite(location).production;
    }

    private Set<Location> getGoodNeighbours(Location location) {
        Set<Location> result = new HashSet<>();

        if (gameHelper.isBuddy(gameMap.getSite(location, Direction.NORTH))) {
            result.add(gameMap.getLocation(location, Direction.NORTH));
        }

        if (gameHelper.isBuddy(gameMap.getSite(location, Direction.EAST))) {
            result.add(gameMap.getLocation(location, Direction.EAST));
        }

        if (gameHelper.isBuddy(gameMap.getSite(location, Direction.SOUTH))) {
            result.add(gameMap.getLocation(location, Direction.SOUTH));
        }

        if (gameHelper.isBuddy(gameMap.getSite(location, Direction.WEST))) {
            result.add(gameMap.getLocation(location, Direction.WEST));
        }

        return result;
    }

    private Set<Location> getNeutralNeighbours(Location location) {
        Set<Location> result = new HashSet<>();

        if (!gameHelper.isBuddy(gameMap.getSite(location, Direction.NORTH))) {
            result.add(gameMap.getLocation(location, Direction.NORTH));
        }

        if (!gameHelper.isBuddy(gameMap.getSite(location, Direction.EAST))) {
            result.add(gameMap.getLocation(location, Direction.EAST));
        }

        if (!gameHelper.isBuddy(gameMap.getSite(location, Direction.SOUTH))) {
            result.add(gameMap.getLocation(location, Direction.SOUTH));
        }

        if (!gameHelper.isBuddy(gameMap.getSite(location, Direction.WEST))) {
            result.add(gameMap.getLocation(location, Direction.WEST));
        }

        return result;
    }
}
