import java.util.ArrayList;
import java.util.Random;

public class MyBotv5 {

    private static int i = 0;

    private static Location loc;
    private static GameMap gameMap;
    private static int myID;

    public static void main(String[] args) throws java.io.IOException {

        InitPackage iPackage = Networking.getInit();
        myID = iPackage.myID;
        gameMap = iPackage.map;

        Networking.sendInit("MyJavaBot");

        Location startLocation = null;

        int loop = 0;
        boolean b = true;

        while (true) {
            gameMap = Networking.getFrame();

            loop++;

            GameHelper gameHelper = new GameHelper(myID, gameMap);
            MoveHandler moveHandler = new MoveHandler(gameMap);

            if (loop == 1) {
                startLocation = gameHelper.findStartLocation();
            }

            int j = 1;

            if (gameHelper.ownedSites() > 17) {
                j = new Random().nextInt(5);
            }


            if (b) {

                if (j == 0) {
                    Expander expander = new Expander(myID, gameMap, moveHandler);
                    expander.expand();

                    Radial radial = new Radial(myID, gameMap, gameHelper, moveHandler);
                    radial.execute();
                }
                else {
                    Cross cross = new Cross(myID, gameMap, gameHelper, moveHandler);
                    b = cross.execute(startLocation);
                }
            }


            if (!b) {
                Expander expander = new Expander(myID, gameMap, moveHandler);
                expander.expand();

                Radial radial = new Radial(myID, gameMap, gameHelper, moveHandler);
                radial.execute();
            }


            ArrayList<Move> ms = moveHandler.getMoves();

            Networking.sendFrame(ms);
        }
    }
}