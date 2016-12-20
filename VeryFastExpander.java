import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class VeryFastExpander {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;

    public VeryFastExpander(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
    }

    public void execute() {

        Board board = new Board(myID, gameMap);
        
        directOvertakeNoHelp(board);

    }

    private void directOvertakeNoHelp(Board board) {
        for (Cell cell : board.getMyCells()) {

            if (cell.isMoved()) {
                continue;
            }

            for (Direction direction : Direction.CARDINALS) {
                Cell target = board.getCell(cell, direction);

                if (target.isNeutral()) {
                    if (cell.getStrength() > target.getStrength()) {
                        cell.move(direction);
                    }
                }
            }
        }    
    }

    private void directOvertakeHelp(Board board) {
        for (Cell cell : board.getMyCells()) {

            if (cell.isMoved()) {
                continue;
            }

            for (Direction direction : Direction.CARDINALS) {
                Cell target = board.getCell(cell, direction);

                if (target.isNeutral()) {

                    List<Cell> helpers = getAdjacent(board, target, c -> (c.isMy() && !c.isMoved() && !c.hasMoveTo()));

                }
            }
        }
    }

    private List<Cell> getAdjacent(Board board, Cell cell, Predicate<Cell> predicate) {
        List result = new ArrayList();
        for (Direction direction : Direction.CARDINALS) {
            Cell target = board.getCell(cell, direction);

            if (predicate.test(target)) {
                result.add(target);
            }
        }
        return result;
    }

    private Direction opposite(Direction direction) {
        if (direction == Direction.NORTH) {
            return Direction.SOUTH;
        }

        if (direction == Direction.SOUTH) {
            return Direction.NORTH;
        }

        if (direction == Direction.WEST) {
            return Direction.EAST;
        }

        if (direction == Direction.EAST) {
            return Direction.WEST;
        }

        return direction;
    }
}
