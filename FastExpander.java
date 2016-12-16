import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FastExpander {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;
    private FileWriter fw;

    public FastExpander(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler, FileWriter fw) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
        this.fw = fw;
    }

    public void execute(int steps) throws IOException{

        Board board = new Board(null, myID, gameMap);

        List<Board> out = new ArrayList<>();
        out.add(board);



        for (int i = 0; i < steps; i++) {

            List<Board> in = new ArrayList<>();

            while (in.size() != out.size()) {
                long t0 = System.currentTimeMillis();

                in = out;
                out = doIt(in);

                long t1 = System.currentTimeMillis();
                long elasped = t1 - t0;
                fw.write("e1: " + elasped + "\n");
            }

            long t2 = System.currentTimeMillis();

            List<Board> children = out.stream().map(b -> b.makeChild()).collect(Collectors.toList());

            long t3 = System.currentTimeMillis();
            long elapsed2 = t3-t2;

            fw.write("elsped2: " + elapsed2);


            children.stream().forEach(c -> c.simulateNextFrame());

            out = children;
        }


        Board bestBoard = out.stream().max(this::compareByProduction).get();

        Board tmp = bestBoard;

        while (!tmp.isRoot()) {
            tmp = tmp.getParent();
        }

        for (Cell cell : tmp.getMyCells()) {
            Move move = new Move(new Location(cell.getX(), cell.getY()), cell.getMoveDirection());
            moveHandler.add(move);
        }
    }

    private int compareByProduction(Board board1, Board board2) {
        return Integer.compare(board1.getProduction(), board2.getProduction());
    }

    private List<Board> doIt(List<Board> boards) throws IOException {
        List<Board> result = new ArrayList<>();

        for (Board board : boards) {
            if (board.hasUnmovedCells()) {
                result.addAll(handle(board));
            }
            else {
                result.add(board);
            }
        }

        return result;
    }

    private List<Board> handle(Board board) throws IOException {

        Cell unmoved = board.getAnyUnmovedCell();

        List<Board> boards = new ArrayList<>();
        for (Direction direction : Direction.DIRECTIONS) {
            boards.add(board.copy().move(unmoved.getPoint(), direction));
        }

        return boards;
    }
}
