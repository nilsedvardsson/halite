import java.util.*;

public class Radial2 {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;

    public Radial2(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
    }

    public void execute(Board board) {

        for (Cell cell : board.getMyCells()) {

            if (cell.isMoved()) {
                continue;
            }

            Direction dir = getDirectionToOvertake(board, cell);

            int acc = accumulate(board, cell, dir, 0);

            if (acc > 230) {
                cell.move(dir);
            }
        }


    }

    private int accumulate(Board board, Cell cell, Direction direction, int steps) {
        if (!cell.isMy()) {
            return 0;
        }

        Cell neighbour = board.getCell(cell, direction);

        return steps * cell.getProduction() + cell.getStrength() + accumulate(board, neighbour, direction, steps + 1);
    }

    private Direction getDirectionToOvertake(Board board, Cell cell) {
        Map<Direction, Integer> data = new HashMap<>();
        Arrays.stream(Direction.CARDINALS).forEach(d -> data.put(d, getStepsToNonMy(board, cell, d, cell)));

        int min = 100;
        Direction minDir = null;

        for (Direction dir : Direction.CARDINALS) {
            if (data.get(dir) < min) {
                min = data.get(dir);
                minDir = dir;
            }
        }
        return minDir;
    }

    private int getStepsToNonMy(Board board, Cell cell, Direction direction, Cell origin) {
        if (cell == origin) {
            return 0;
        }

        if (cell.isMy()) {
            return 1 + getStepsToNonMy(board, board.getCell(cell, direction), direction, origin);
        } else {
            return 0;
        }
    }
}
