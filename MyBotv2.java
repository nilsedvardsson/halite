import java.util.*;

public class MyBotv2 {

    private static int i = 0;

    private static Location loc;
    private static GameMap gameMap;
    private static int myID;

    public static void main(String[] args) throws java.io.IOException {
        InitPackage iPackage = Networking.getInit();
        myID = iPackage.myID;
        gameMap = iPackage.map;

        Networking.sendInit("MyJavaBot");

        Set<Point> alreadyHandled = null;

        int loop = 0;

        while (true) {
            loop++;
            ArrayList<Move> moves = new ArrayList<Move>();

            gameMap = Networking.getFrame();

            alreadyHandled = new HashSet<>();

            List<DataPoint> strongest = findStrongest();
            Collections.sort(strongest);

            Location strongestLocation = findSuitableStrong(strongest);

            if (strongestLocation != null) {

                Direction d = getNearestToOvertake(strongestLocation);

                Point p = new Point(strongestLocation.x, strongestLocation.y);
                alreadyHandled.add(p);

                Location tmpLocation = gameMap.getLocation(strongestLocation, d);
                Site tmpSite = gameMap.getSite(tmpLocation);

                while (tmpSite.owner == myID) {
                    alreadyHandled.add(new Point(tmpLocation.x, tmpLocation.y));
                    tmpLocation = gameMap.getLocation(tmpLocation, d);
                    tmpSite = gameMap.getSite(tmpLocation);
                }

                for (Point point : alreadyHandled) {
                    moves.add(new Move(new Location(point.getX(), point.getY()), d));
                }
            }


            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {

                    if (alreadyHandled.contains(new Point(x, y))) {
                        continue;
                    }

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
                        }

                        moves.add(new Move(loc, dir));
                    }
                }
            }

            Networking.sendFrame(moves);
        }
    }

    private static Location findSuitableStrong(List<DataPoint> dataPoints) {
        for (DataPoint dataPoint : dataPoints) {

            Location l = new Location(dataPoint.getPoint().getX(), dataPoint.getPoint().getY());
            Direction d = getNearestToOvertake(l);
             if (d != Direction.STILL) {
                 return l;
             }
        }
        return null;
    }

    private static List<DataPoint> findStrongest() {

        List<DataPoint> result = new ArrayList<>();

        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Location l = new Location(x, y);

                Site site = gameMap.getSite(l);

                if (site.owner != myID) {
                    continue;
                }

                if (onlyBuddies(l)) {
                    result.add(new DataPoint(new Point(x, y), site.strength));
                }
            }
        }

        return result;
    }

    private static int howManyToMove(int ownedByMe) {
        if (ownedByMe < 20) {
            return 0;
        }
        else if (ownedByMe < 50) {
            return 1;
        }
        else if (ownedByMe < 100) {
            return 2;
        }
        else if (ownedByMe < 250) {
            return 4;
        }
        else {
            return 10;
        }
    }

    private static int ownedByMe() {
        int num = 0;
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Site site = gameMap.getSite(new Location(x, y));
                if (site.owner == myID) {
                    num++;
                }
            }
        }
        return num;
    }

    private static boolean onlyBuddies(Location l) {
        return
                gameMap.getSite(l, Direction.NORTH).owner == myID &&
                        gameMap.getSite(l, Direction.EAST).owner == myID &&
                        gameMap.getSite(l, Direction.SOUTH).owner == myID &&
                        gameMap.getSite(l, Direction.WEST).owner == myID;
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

    private static Direction getNearestToOvertake(Location startLocation) {
        int n = 0;
        int e = 0;
        int s = 0;
        int w = 0;

        Location l = startLocation;

        while ((gameMap.getSite(l, Direction.NORTH).owner == myID) && (n < 50)) {
            n++;
            l = getLocation(l, Direction.NORTH);
        }

        l = startLocation;

        while ((gameMap.getSite(l, Direction.EAST).owner == myID) && (e < 50)) {
            e++;
            l = getLocation(l, Direction.EAST);
        }

        l = startLocation;

        while ((gameMap.getSite(l, Direction.SOUTH).owner == myID) && (s < 50)) {
            s++;
            l = getLocation(l, Direction.SOUTH);
        }

        l = startLocation;

        while ((gameMap.getSite(l, Direction.WEST).owner == myID) && (w < 50)) {
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

        if (currentBest == 50) {
            dir = Direction.STILL;
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
