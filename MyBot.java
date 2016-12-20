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

        Location startLocation = null;

        int loop = 0;
        boolean underAttack = false;

        Stat currentStat = new Stat();
        Stat oldStat = new Stat();

        int mode = 0;
        int modeCounter = 0;

        while (true) {
            gameMap = Networking.getFrame();

            loop++;

            GameHelper gameHelper = new GameHelper(myID, gameMap);
            MoveHandler moveHandler = new MoveHandler(gameMap);


            if (!underAttack) {
                oldStat = currentStat;
                currentStat = new Statistics(myID, gameMap, gameHelper, moveHandler).execute();

                if (oldStat != null) {

                    if ((currentStat.territory <= oldStat.territory) &&
                            (currentStat.strength <= oldStat.strength) &&
                            (currentStat.production <= oldStat.production)) {

                        underAttack = true;
                    }

                }
            }


            if (!underAttack) {

                FastExpander fastExpander = new FastExpander(myID, gameMap, gameHelper, moveHandler);
                fastExpander.execute();

                Radial radial = new Radial(myID, gameMap, gameHelper, moveHandler);
                radial.execute();

            } else {

                if (mode == 0) {

                    MassMove massMove = new MassMove(myID, gameMap, gameHelper, moveHandler);
                    massMove.execute(Direction.EAST);

                    modeCounter++;

                    if (modeCounter == 6) {
                        mode = 1;
                        modeCounter = 0;
                    }

                }
                else {
                    Attacker attacker = new Attacker(myID, gameMap, gameHelper, moveHandler);
                    underAttack = attacker.execute(true);

                    modeCounter++;

                    if (modeCounter == 30) {
                        mode = 0;
                        modeCounter = 0;
                    }
                }



            }

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