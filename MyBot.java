import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Random;

public class MyBot {

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

        File f = new File("/Users/nilse/Developer/halite/out.txt");
        FileWriter fw = new FileWriter(f);

        while (true) {
            gameMap = Networking.getFrame();

            loop++;

            GameHelper gameHelper = new GameHelper(myID, gameMap);
            MoveHandler moveHandler = new MoveHandler(gameMap);

            if (loop < 10) {

                FastExpander fastExpander = new FastExpander(myID, gameMap, gameHelper, moveHandler, fw);
                fastExpander.execute(2);

            }
            else {
                if (f != null) {
                    fw.close();
                    f = null;
                }
            }

            ArrayList<Move> ms = moveHandler.getMoves();

            Networking.sendFrame(ms);
        }

    }
}