import java.util.ArrayList;

public class MyBot {

    private static Location loc;
    private static GameMap gameMap;
    private static int myID;

    public static void main(String[] args) {

        InitPackage iPackage = Networking.getInit();
        myID = iPackage.myID;
        gameMap = iPackage.map;

        Networking.sendInit("MyJavaBot");

        int loop = 0;

        while (true) {
            gameMap = Networking.getFrame();

            loop++;

            GameHelper gameHelper = new GameHelper(myID, gameMap);
            MoveHandler moveHandler = new MoveHandler(gameMap);

            Board board = new Board(myID, gameMap);


            VeryFastExpander veryFastExpander = new VeryFastExpander(myID, gameMap, gameHelper, moveHandler);
            veryFastExpander.execute(board);

            Radial2 radial2 = new Radial2(myID, gameMap, gameHelper, moveHandler);
            radial2.execute(board);


            for (Cell cell : board.getMyCells()) {
                if (cell.isMoved()) {
                    Move move = new Move(new Location(cell.getX(), cell.getY()), cell.getMoveDirection());
                    moveHandler.add(move);
                } else {
                    Move move = new Move(new Location(cell.getX(), cell.getY()), Direction.STILL);
                    moveHandler.add(move);
                }
            }

            ArrayList<Move> ms = moveHandler.getMoves();

            Networking.sendFrame(ms);
        }

    }
}