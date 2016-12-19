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

        Board board = new Board(null, myID, gameMap);

        noHelp(board);
        together(board);
        getHelpFromOne(board);
        getHelpFromTwo(board);
        getHelpFromThree(board);

        for (Cell cell : board.getMyCells()) {
            if (cell.isMoved()) {
                Move move = new Move(new Location(cell.getX(), cell.getY()), cell.getMoveDirection());
                moveHandler.add(move);
            }
        }

    }

    private void noHelp(Board board) {

        for (Cell cell : board.getMyCells()) {

            if (cell.isMoved()) {
                continue;
            }

            Direction possibleDirection = possibleMove(board, cell);

            if (possibleDirection != null) {
                cell.move(possibleDirection);
            }
        }
    }

    private void together(Board board) {
       for (Cell cell : board.getMyCells()) {

           if (cell.isMoved()) {
               continue;
           }

           boolean found = false;

           for (Direction direction : Direction.CARDINALS) {

               Cell target = board.getCell(cell, direction);

               if (target.isMy()) {
                   continue;
               }

               for (Direction direction2 : Direction.CARDINALS) {
                   Cell partner = board.getCell(target, direction2);

                   if (cell == partner) {
                       continue;
                   }

                   if (!partner.isMy()) {
                       continue;
                   }

                   if (cell.getStrength() + partner.getStrength() > target.getStrength()) {
                       found = true;
                       cell.move(direction);
                       partner.move(opposite(direction2));
                   }

                   if (found) {
                       break;
                   }
               }
               if (found) {
                   break;
               }
           }
       }
    }

    private void getHelpFromThree(Board board) {
        for (Cell cell : board.getMyCells()) {

            if (cell.isMoved()) {
                continue;
            }

            int neededStrength = strengthNeeded(board, cell);
            boolean found = false;

            for (Direction direction1 : Direction.CARDINALS) {
                for (Direction direction2 : Direction.CARDINALS) {
                    for (Direction direction3 : Direction.CARDINALS) {

                        if (direction1 == direction2 || direction1 == direction3 || direction2 == direction3) {
                            continue;
                        }

                        Cell possibleHelper1 = board.getCell(cell, direction1);
                        Cell possibleHelper2 = board.getCell(cell, direction2);
                        Cell possibleHelper3 = board.getCell(cell, direction3);

                        if (!possibleHelper1.isMy() || !possibleHelper2.isMy() || !possibleHelper3.isMy()) {
                            continue;
                        }

                        if (possibleHelper1.isMoved() || possibleHelper2.isMoved() || possibleHelper3.isMoved()) {
                            continue;
                        }

                        if (possibleHelper1.getStrength()
                                + possibleHelper2.getStrength()
                                + possibleHelper3.getStrength()
                                > neededStrength) {

                            possibleHelper1.move(opposite(direction1));
                            possibleHelper2.move(opposite(direction2));
                            possibleHelper3.move(opposite(direction3));

                            found = true;
                        }

                        if (found) {
                            break;
                        }
                    }
                    if (found) {
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }

        }
    }

    private void getHelpFromTwo(Board board) {
        for (Cell cell : board.getMyCells()) {

            if (cell.isMoved()) {
                continue;
            }

            int neededStrength = strengthNeeded(board, cell);
            boolean found = false;

            for (Direction direction1 : Direction.CARDINALS) {
                for (Direction direction2 : Direction.CARDINALS) {
                    if (direction1 == direction2) {
                        continue;
                    }

                    Cell possibleHelper1 = board.getCell(cell, direction1);
                    Cell possibleHelper2 = board.getCell(cell, direction2);

                    if (!possibleHelper1.isMy() || !possibleHelper2.isMy()) {
                        continue;
                    }

                    if (possibleHelper1.isMoved() || possibleHelper2.isMoved()) {
                        continue;
                    }

                    if (possibleHelper1.getStrength() + possibleHelper2.getStrength() > neededStrength) {
                        possibleHelper1.move(opposite(direction1));
                        possibleHelper2.move(opposite(direction2));
                        found = true;
                    }

                    if (found) {
                        break;
                    }
                }
                if (found) {
                    break;
                }
            }
        }
    }

    private void getHelpFromOne(Board board) {
        for (Cell cell : board.getMyCells()) {

            if (cell.isMoved()) {
                continue;
            }

            int neededStrength = strengthNeeded(board, cell);

            for (Direction direction : Direction.CARDINALS) {
                Cell possibleHelper = board.getCell(cell, direction);

                if (!possibleHelper.isMy()) {
                    continue;
                }

                if (possibleHelper.isMoved()) {
                    continue;
                }

                if (possibleHelper.getStrength() > neededStrength) {
                    possibleHelper.move(opposite(direction));
                    break;
                }
            }
        }
    }

    private int strengthNeeded(Board board, Cell cell) {
        int minNeeded = 1000;

        for (Direction direction : Direction.CARDINALS) {
            Cell toOvertake = board.getCell(cell, direction);

            if (toOvertake.isMy()) {
                continue;
            }

            int needed = toOvertake.getStrength() - cell.getStrength();

            if (needed < minNeeded) {
                minNeeded = needed;
            }
        }

        return minNeeded;
    }

    private void freezeWithoutMove(Board board) {
        for (Cell cell : board.getMyCells()) {

            if (cell.isMoved()) {
                continue;
            }

            cell.move(Direction.STILL);

        }

    }

    private Direction possibleMove(Board board, Cell cell) {
        for (Direction direction : Direction.CARDINALS) {
            Cell target = board.getCell(cell, direction);

            if (target.isMy()) {
                continue;
            }

            if (cell.getStrength() > target.getStrength()) {
                return direction;
            }
        }
        return null;
    }

    private Direction opposite(Direction direction) {
        if (direction == Direction.NORTH) {
            return Direction.SOUTH;
        }

        if (direction == Direction.SOUTH) {
            return Direction.NORTH;
        }

        if (direction == Direction.WEST) {
            return Direction.EAST;
        }

        if (direction == Direction.EAST) {
            return Direction.WEST;
        }

        return direction;
    }
}
