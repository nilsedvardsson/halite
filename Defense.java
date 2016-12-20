import java.util.logging.XMLFormatter;

public class Defense {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;

    public Defense(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
    }

    public void execute() {

        Board board = new Board(null, myID, gameMap);

        for (Cell cell : board.getMyCells()) {
            if (cell.isMoved()) {
                Move move = new Move(new Location(cell.getX(), cell.getY()), cell.getMoveDirection());
                moveHandler.add(move);
            }
        }
    }
}
