import java.util.ArrayList;

public class MyBot {

    private static int i = 0;

    private static Location loc;
    private static GameMap gameMap;
    private static int myID;

    public static void main(String[] args) {

        InitPackage iPackage = Networking.getInit();
        myID = iPackage.myID;
        gameMap = iPackage.map;

        Networking.sendInit("MyJavaBot");

        Location startLocation = null;

        int loop = 0;

        while (true) {
            gameMap = Networking.getFrame();

            loop++;


            GameHelper gameHelper = new GameHelper(myID, gameMap);
            MoveHandler moveHandler = new MoveHandler(gameMap);

            /*
            Attacker attacker = new Attacker(myID, gameMap, gameHelper, moveHandler);
            boolean underAttack = attacker.execute();

            if (!underAttack) {

                FastExpander fastExpander = new FastExpander(myID, gameMap, gameHelper, moveHandler);
                fastExpander.execute();

                Radial radial = new Radial(myID, gameMap, gameHelper, moveHandler);
                radial.execute();

            }
            */

            FastExpander fastExpander = new FastExpander(myID, gameMap, gameHelper, moveHandler);
            fastExpander.execute();



            for (int y = 0; y < gameMap.height; y++) {
                for (int x = 0; x < gameMap.width; x++) {

                    Location loc = new Location(x, y);
                    Site site = gameMap.getSite(loc);

                    if (site.owner == myID) {
                        Move move = new Move(loc, Direction.STILL);

                        if (moveHandler.canBeAdded(move)) {
                            moveHandler.add(move);
                        }
                    }

                }
            }

            ArrayList<Move> ms = moveHandler.getMoves();

            Networking.sendFrame(ms);
        }

    }
}