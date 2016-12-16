import java.util.HashSet;
import java.util.Set;

public class Early {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;

    public Early(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
    }

    public void execute() {

        Set<Location> myLocations = getMyLocations();

        for (Location loc1 : myLocations) {
            for (Location loc2 : myLocations) {

                if (!gameHelper.neighbours(loc1, loc2)) {
                    continue;
                }

                if (gameHelper.isCoreBuddy(loc1) && gameHelper.isCoreBuddy(loc2)) {
                    continue;
                }

                Site site1 = gameMap.getSite(loc1);
                Site site2 = gameMap.getSite(loc2);

                int sumStrength = site1.strength + site2.strength;

                int weakestNonBuddy1 = gameHelper.weakestNonBuddyNeighbour(loc1);
                int weakestNonBuddy2 = gameHelper.weakestNonBuddyNeighbour(loc2);

                if (weakestNonBuddy1 == 1000 && weakestNonBuddy2 == 1000) {
                    continue;
                }

                if (sumStrength < weakestNonBuddy1 && sumStrength < weakestNonBuddy2) {
                    continue;
                }

                Location from;
                Location to;
                if (weakestNonBuddy1 < weakestNonBuddy2) {
                    from = loc2;
                    to = loc1;
                }
                else {
                    from = loc1;
                    to = loc2;
                }

                Direction dir = gameHelper.getDirection(from, to);

                Move move = new Move(from, dir);
                if (moveHandler.canBeAdded(move)) {
                    moveHandler.add(move);
                    return;
                }

            }
        }
    }


    private Set<Location> getMyLocations() {
        Set<Location> result = new HashSet<>();
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Location loc = new Location(x, y);
                Site site = gameMap.getSite(loc);

                if (site.owner == myID) {
                    result.add(loc);
                }
            }
        }
        return result;
    }
}
