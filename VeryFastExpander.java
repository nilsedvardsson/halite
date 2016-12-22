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

    public void execute(Board board) {

        directOvertakeNoHelp(board);
        directOvertakeHelp(board);
        twoStepsOvertake(board);

    }

    private void twoStepsOvertake(Board board) {
        for (Cell cell : board.getMyCells()) {

            if (cell.isMoved()) {
                continue;
            }

            if (cell.hasMoveTo()) {
                continue;
            }

            List<Cell> targetCells = getAdjacent(board, cell, c -> c.isNeutral() && !c.hasMoveTo());

            if (targetCells.isEmpty()) {
                continue;
            }

            // TODO sort lowest to highest
            int needed = targetCells.get(0).getStrength() - cell.getStrength();

            List<Cell> helpers = getAdjacent(board, cell, c -> c.isMy() && !c.isMoved() && !c.hasMoveTo() && c.getStrength() > needed);

            if (helpers.size() > 0) {
                Cell helper = helpers.get(0);

                board.move(helper, cell);
            }
        }
    }

    private void directOvertakeNoHelp(Board board) {
        for (Cell cell : board.getMyCells()) {

            if (cell.isMoved()) {
                continue;
            }

            List<Cell> targetCells = getAdjacent(board, cell, c -> c.isNeutral() && c.getStrength() < cell.getStrength());

            if (targetCells.isEmpty()) {
                continue;
            }

            if (targetCells.size() > 1) {
                targetCells.sort((c1, c2) -> Integer.compare(c1.getProduction(), c2.getProduction()));
            }

            board.move(cell, targetCells.get(0));

        }    
    }

    private void directOvertakeHelp(Board board) {
        for (Cell cell : board.getMyCells()) {

            if (cell.isMoved()) {
                continue;
            }

            for (Direction direction : Direction.CARDINALS) {
                Cell target = board.getCell(cell, direction);

                // TODO neutral ? enemy ?
                if (target.isNeutral()) {

                    List<Cell> helpers = getAdjacent(board, target, c -> (c.isMy() && !c.isMoved() && !c.hasMoveTo() && (c != cell)));

                    if (helpers.isEmpty()) {
                        continue;
                    }

                    helpers.sort((c1, c2) -> Integer.compare(c1.getStrength(), c2.getStrength()));

                    if (cell.getStrength() + helpers.get(0).getStrength() > target.getStrength()) {
                        board.move(cell, target);
                        board.move(helpers.get(0), target);
                        break;
                    }

                    if (helpers.size() > 1) {

                        if (cell.getStrength() + helpers.get(0).getStrength() + helpers.get(1).getStrength() > target.getStrength()) {
                            board.move(cell, target);
                            board.move(helpers.get(0), target);
                            board.move(helpers.get(1), target);
                            break;
                        }

                    }

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

    private Direction getDirection(Board board,  Cell from, Cell to) {
        for (Direction direction : Direction.CARDINALS) {
            if (board.getCell(from, direction) == to) {
                return direction;
            }
        }
        return null;
    }
}
