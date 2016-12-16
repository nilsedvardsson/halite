import java.util.HashSet;
import java.util.Set;

public class GameHelper {

    private int myID;
    private GameMap gameMap;

    public GameHelper(int myID, GameMap gameMap) {
        this.myID = myID;
        this.gameMap = gameMap;
    }

    public int ownedSites() {
        int num = 0;
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Location loc = new Location(x, y);
                Site site = gameMap.getSite(loc);

                if (site.owner == myID) {
                    num++;
                }
            }
        }
        return num;
    }

    public Set<Location> mySitesOnRow(int row) {
        Set<Location> mySites = new HashSet<>();
        for (int x = 0; x < gameMap.width; x++) {
            Location loc = new Location(x, row);
            Site site = gameMap.getSite(loc);

            if (site.owner == myID) {
                mySites.add(loc);
            }
        }
        return mySites;
    }

    public Set<Location> mySitesOnColumn(int column) {
        Set<Location> mySites = new HashSet<>();
        for (int y = 0; y < gameMap.height; y++) {
            Location loc = new Location(column, y);
            Site site = gameMap.getSite(loc);

            if (site.owner == myID) {
                mySites.add(loc);
            }
        }
        return mySites;
    }

    public Set<Direction> buddyNeighbours(Location location) {
        Set<Direction> buddies = new HashSet<>();

        if (isBuddy(northSite(location))) {
            buddies.add(Direction.NORTH);
        }

        if (isBuddy(eastSite(location))) {
            buddies.add(Direction.EAST);
        }

        if (isBuddy(southSite(location))) {
            buddies.add(Direction.SOUTH);
        }

        if (isBuddy(westSite(location))) {
            buddies.add(Direction.WEST);
        }

        return buddies;
    }

    public boolean isBuddy(Site site) {
        return site.owner == myID;
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

    public boolean neighbours(Location location1, Location location2) {

        if (sameLocation(gameMap.getLocation(location1, Direction.NORTH), location2)) {
            return true;
        }
        if (sameLocation(gameMap.getLocation(location1, Direction.EAST), location2)) {
            return true;
        }
        if (sameLocation(gameMap.getLocation(location1, Direction.SOUTH), location2)) {
            return true;
        }
        if (sameLocation(gameMap.getLocation(location1, Direction.WEST), location2)) {
            return true;
        }

        return false;
    }

    public boolean sameLocation(Location location1, Location location2) {
        return location1.x == location2.x && location1.y == location2.y;
    }

    public int weakestNonBuddyNeighbour(Location location) {
        int weakest = 1000;

        weakest = weakNonBuddyNeighbour(location, Direction.NORTH, weakest);
        weakest = weakNonBuddyNeighbour(location, Direction.EAST, weakest);
        weakest = weakNonBuddyNeighbour(location, Direction.SOUTH, weakest);
        weakest = weakNonBuddyNeighbour(location, Direction.WEST, weakest);

        return weakest;
    }

    private int weakNonBuddyNeighbour(Location location, Direction direction, int weakest) {
        Site site = gameMap.getSite(location, direction);

        if (site.owner == myID) {
            return weakest;
        }

        if (site.strength < weakest) {
            return site.strength;
        }

        return weakest;
    }

    public Direction getDirection(Location from, Location to) {
        if (from.x == to.x) {
            Location test = gameMap.getLocation(from, Direction.NORTH);
            if (test.y == to.y) {
                return Direction.NORTH;
            } else {
                return Direction.SOUTH;
            }
        } else {
            Location test = gameMap.getLocation(from, Direction.EAST);
            if (test.x == to.x) {
                return Direction.EAST;
            } else {
                return Direction.WEST;
            }

        }
    }

    public boolean isCoreBuddy(Location location) {
        return isBuddy(northSite(location)) &&
                isBuddy(eastSite(location)) &&
                isBuddy(southSite(location)) &&
                isBuddy(westSite(location));
    }

    public Location findStartLocation() {
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Location loc = new Location(x, y);
                Site site = gameMap.getSite(loc);

                if (site.owner == myID) {
                    return loc;
                }

            }
        }
        return null;
    }

    public int stepsToNonBuddy(Location location, Direction direction) {

        int steps = 0;
        Location test = location;

        while (gameMap.getSite(test).owner == myID && steps < 100) {
            steps++;
            test = gameMap.getLocation(test, direction);
        }

        return steps;

    }

}