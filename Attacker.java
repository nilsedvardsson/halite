public class Attacker {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;

    public Attacker(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
    }

    public boolean execute() {

        Board board = new Board(null, myID, gameMap);

        boolean underAttack = false;

        for (Cell cell : board.getMyCells()) {
            for (Direction direction : Direction.CARDINALS) {
                Cell test = board.getCell(cell, direction);

                if (!test.isMy() && test.getOwner() != 0) {
                    underAttack = true;
                    break;
                }
            }
            if (underAttack) {
                break;
            }
        }

        if (!underAttack) {
            return false;
        }


        for (Cell cell : board.getMyCells()) {

            if (cell.getStrength() == 255) {
                cell.move(Direction.EAST);
            }
            else if (cell.getStrength() > 30) {

                if (cell.getX() % 2 == 0) {
                    cell.move(Direction.EAST);
                }
                else {
                    cell.move(Direction.NORTH);
                }
            }

        }

        for (Cell cell : board.getMyCells()) {
            if (cell.isMoved()) {
                Move move = new Move(new Location(cell.getX(), cell.getY()), cell.getMoveDirection());
                moveHandler.add(move);
            }
        }

        return true;
    }

}
