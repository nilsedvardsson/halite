import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MoveHandler {

    private GameMap gameMap;

    private ArrayList<Move> moves;

    private Set<Point> from;
    private Set<Point> to;

    public MoveHandler(GameMap gameMap) {
        this.gameMap = gameMap;
        this.moves = new ArrayList<>();
        from = new HashSet<>();
        to = new HashSet<>();
    }

    public ArrayList<Move> getMoves() {
        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {
                Move move = new Move(new Location(x, y), Direction.STILL);
                if (canBeAdded(move)) {
                    add(move);
                }
            }
        }
        return moves;
    }

    public void add(Move move) {
        addLocation(from, move.loc);
        addLocation(to, newLocation(move));
        moves.add(move);
    }

    private void addLocation(Set<Point> points, Location location) {
        points.add(new Point(location.x, location.y));
    }

    public boolean canBeAdded(Move move) {
        if (contains(from, move.loc)) {
            return false;
        }

        /*
        if (contains(to, move.loc)) {
            return false;
        }
        */

        if (contains(to, newLocation(move))) {
            return false;
        }

        if (contains(from, newLocation(move))) {
            return false;
        }

        return true;
    }

    private Location newLocation(Move move) {
        return gameMap.getLocation(move.loc, move.dir);
    }

    private boolean contains(Set<Point> points, Location location) {
        return points.contains(new Point(location.x, location.y));
    }


}
