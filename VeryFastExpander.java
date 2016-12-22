import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class VeryFastExpander {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;
    private FileWriter fw;

    public VeryFastExpander(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler, FileWriter fw) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
        this.fw = fw;
    }

    public void execute(Board board) throws IOException {

        fw.write("*\n");
        directOvertakeNoHelp(board);
        directOvertakeHelp(board);

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

    private void directOvertakeHelp(Board board) throws IOException {
        for (Cell cell : board.getMyCells()) {

            if (cell.isMoved()) {
                continue;
            }

            for (Direction direction : Direction.CARDINALS) {
                Cell target = board.getCell(cell, direction);

                // TODO neutral ? enemy ?
                if (target.isNeutral()) {

                    List<Cell> helpers = getAdjacent(board, target, c -> (c.isMy() && !c.isMoved() && !c.hasMoveTo() && (c != cell)));

                    fw.write("--\n");
                    printCellPos(cell);
                    fw.write("Num helpers for: " + helpers.size() + "\n");

                    for (Cell helper : helpers) {
                        printCellPos(helper);
                    }

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

    private void printCellPos(Cell cell) throws IOException {
        fw.write("x: " + cell.getX() + ", y: " + cell.getY() + "\n");
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
