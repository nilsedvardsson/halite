import java.util.ArrayList;

public class MyBotv1 {

    private static int i = 0;

    private static Location loc;
    private static GameMap gameMap;
    private static int myID;

    public static void main(String[] args) throws java.io.IOException {
        InitPackage iPackage = Networking.getInit();
        myID = iPackage.myID;
        gameMap = iPackage.map;

        Networking.sendInit("MyJavaBot");

        int loop = 0;

        while (true) {
            loop++;
            ArrayList<Move> moves = new ArrayList<Move>();

            gameMap = Networking.getFrame();

            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {

                    loc = new Location(x, y);

                    Site site = gameMap.getSite(loc);

                    if (site.owner == myID) {

                        Direction dir = Direction.STILL;

                        Site northSite = northSite();
                        Site eastSite = eastSite();
                        Site southSite = southhSite();
                        Site westSite = westSite();

                        if (northSite.owner != myID && site.strength > northSite.strength) {
                            dir = Direction.NORTH;
                        } else if (eastSite.owner != myID && site.strength > eastSite.strength) {
                            dir = Direction.EAST;
                        } else if (southSite.owner != myID && site.strength > southSite.strength) {
                            dir = Direction.SOUTH;
                        } else if (westSite.owner != myID && site.strength > westSite.strength) {
                            dir = Direction.WEST;
                        } else if (onlyBuddies()) {
                            dir = getNearestToOvertake();

                            Site s = gameMap.getSite(loc, dir);

                            if (site.strength < s.strength) {
                                dir = Direction.STILL;
                            }
                        }

                        moves.add(new Move(loc, dir));
                    }
                }
            }

            Networking.sendFrame(moves);
        }
    }

    private static boolean onlyBuddies() {
        return
                gameMap.getSite(loc, Direction.NORTH).owner == myID &&
                        gameMap.getSite(loc, Direction.EAST).owner == myID &&
                        gameMap.getSite(loc, Direction.SOUTH).owner == myID &&
                        gameMap.getSite(loc, Direction.WEST).owner == myID;
    }

    private static Site northSite() {
        return gameMap.getSite(loc, Direction.NORTH);
    }

    private static Site eastSite() {
        return gameMap.getSite(loc, Direction.EAST);
    }

    private static Site southhSite() {
        return gameMap.getSite(loc, Direction.SOUTH);
    }

    private static Site westSite() {
        return gameMap.getSite(loc, Direction.WEST);
    }

    private static Direction getNearestToOvertake() {
        int n = 0;
        int e = 0;
        int s = 0;
        int w = 0;

        Location l = loc;

        while ((gameMap.getSite(l, Direction.NORTH).owner == myID) && (n < 10)) {
            n++;
            l = getLocation(l, Direction.NORTH);
        }

        l = loc;

        while ((gameMap.getSite(l, Direction.EAST).owner == myID) && (e < 10)) {
            e++;
            l = getLocation(l, Direction.EAST);
        }

        l = loc;

        while ((gameMap.getSite(l, Direction.SOUTH).owner == myID) && (s < 10)) {
            s++;
            l = getLocation(l, Direction.SOUTH);
        }

        l = loc;

        while ((gameMap.getSite(l, Direction.WEST).owner == myID) && (w < 10)) {
            w++;
            l = getLocation(l, Direction.WEST);
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

    private static Location getLocation(Location location, Direction direction) {
        int x = location.x;
        int y = location.y;


        x += gameMap.width;
        y += gameMap.height;

        if (direction == Direction.NORTH) {
            y--;
        } else if (direction == Direction.EAST) {
            x++;
        } else if (direction == Direction.SOUTH) {
            y++;
        } else if (direction == Direction.WEST) {
            x--;
        }

        return new Location(x % gameMap.width, y % gameMap.height);
    }
}
