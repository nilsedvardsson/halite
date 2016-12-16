import java.util.ArrayList;
import java.util.List;

public class FastExpander {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;

    public FastExpander(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
    }

    public void execute() {

        Board board = new Board(myID, gameMap);

        List<Board> out = new ArrayList<>();
        out.add(board);

        List<Board> in = new ArrayList<>();

        while (in.size() != out.size()) {
            in = out;
            out = doIt(in);
        }
    }

    private List<Board> doIt(List<Board> boards) {
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

    private List<Board> handle(Board board) {

        Cell unmoved = board.getAnyUnmovedCell();

        List<Board> boards = new ArrayList<>();
        for (Direction direction : Direction.DIRECTIONS) {
            boards.add(board.copy().move(unmoved.getPoint(), direction));
        }

        return boards;
    }
}
