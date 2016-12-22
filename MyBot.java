import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

        File f = new File("/Users/nils/Developer/halite/out.txt");
        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }


        int loop = 0;

        while (true) {
            gameMap = Networking.getFrame();

            loop++;

            GameHelper gameHelper = new GameHelper(myID, gameMap);
            MoveHandler moveHandler = new MoveHandler(gameMap);

            Board board = new Board(myID, gameMap);

            if (loop < 50) {
                try {
                    fw.write("LOOP #" + loop + "\n");
                }
                catch (IOException e) {
                }

                VeryFastExpander veryFastExpander = new VeryFastExpander(myID, gameMap, gameHelper, moveHandler, fw);
                try {
                    veryFastExpander.execute(board);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
            else if (loop < 100) {
                Radial2 radial = new Radial2(myID, gameMap, gameHelper, moveHandler, fw);
                try {
                    radial.execute(board);
                }
                catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else {
                if (fw != null) {
                    try {
                        fw.close();
                    }
                    catch (IOException e) {}
                    fw = null;
                }
            }

            for (Cell cell : board.getMyCells()) {
                if (cell.isMoved()) {
                    Move move = new Move(new Location(cell.getX(), cell.getY()), cell.getMoveDirection());
                    moveHandler.add(move);
                }
                else {
                    Move move = new Move(new Location(cell.getX(), cell.getY()), Direction.STILL);
                    moveHandler.add(move);
                }
            }

            ArrayList<Move> ms = moveHandler.getMoves();

            Networking.sendFrame(ms);
        }

    }
}