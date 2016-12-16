import java.util.ArrayList;
import java.util.Set;

public class MyBotv4 {

    private static int i = 0;

    private static Location loc;
    private static GameMap gameMap;
    private static int myID;

    public static void main(String[] args) throws java.io.IOException {
        InitPackage iPackage = Networking.getInit();
        myID = iPackage.myID;
        gameMap = iPackage.map;

        Networking.sendInit("MyJavaBot");

        Set<Point> alreadyHandled = null;

        int loop = 0;

        while (true) {
            gameMap = Networking.getFrame();

            loop++;

            GameHelper gameHelper = new GameHelper(myID, gameMap);
            MoveHandler moveHandler = new MoveHandler(gameMap);


            Producer producer = new Producer(myID, gameMap, gameHelper, moveHandler);
            producer.execute();


            ArrayList<Move> ms = moveHandler.getMoves();

            Networking.sendFrame(ms);
        }
    }

}