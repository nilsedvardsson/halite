import java.util.Random;

public class Attacker {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;

    public Attacker(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
    }

    public boolean execute(boolean flag) {

        Board board = new Board(null, myID, gameMap);

        for (Cell cell : board.getMyCells()) {

            if (cell.getStrength() > 30) {
                Direction dir = null;

                if (flag) {
                    if (cell.getX() % 2 == 0) {
                        dir = Direction.NORTH;
                    } else {
                        dir = Direction.SOUTH;
                    }
                } else {
                    if (cell.getY() % 2 == 0) {
                        dir = Direction.EAST;
                    } else {
                        dir = Direction.WEST;
                    }
                }

                cell.move(dir);
            }
        }

        for (Cell cell : board.getMyCells()) {
            if (cell.isMoved()) {
                Move move = new Move(new Location(cell.getX(), cell.getY()), cell.getMoveDirection());
                moveHandler.add(move);
            }
        }

        return true;
    }

}
