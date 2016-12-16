public class Cross {

    private int myID;
    private GameMap gameMap;
    private GameHelper gameHelper;
    private MoveHandler moveHandler;

    public Cross(int myID, GameMap gameMap, GameHelper gameHelper, MoveHandler moveHandler) {
        this.myID = myID;
        this.gameMap = gameMap;
        this.gameHelper = gameHelper;
        this.moveHandler = moveHandler;
    }

    public boolean execute(Location startLocation) {
        int threshold = 30;

        boolean runVertical = true;
        boolean runHorizontal = true;

        if (gameHelper.stepsToNonBuddy(startLocation, Direction.NORTH) == 100) {
            runVertical = false;
        }

        if (gameHelper.stepsToNonBuddy(startLocation, Direction.EAST) == 100) {
            runHorizontal = false;
        }

        if (!runVertical || !runHorizontal) {
            return false;
        }

        if (!hasCross(startLocation)) {
            crossSetup(startLocation);
            return true;
        }


        for (int y = 0; y < gameMap.height; y++) {
            for (int x = 0; x < gameMap.width; x++) {

                Location loc = new Location(x, y);
                Site site = gameMap.getSite(loc);

                if (site.owner == myID) {

                    if (runVertical) {

                        if (loc.x == startLocation.x) {

                            Site n = gameMap.getSite(loc, Direction.NORTH);
                            Site s = gameMap.getSite(loc, Direction.SOUTH);

                            if (n.owner != myID) {

                                if (site.strength > n.strength) {

                                    Move move = new Move(loc, Direction.NORTH);
                                    if (moveHandler.canBeAdded(move)) {
                                        moveHandler.add(move);
                                    }
                                } else {
                                    if (s.owner == myID && ((s.strength + site.strength > n.strength))) {
                                        Location toMove = gameMap.getLocation(loc, Direction.SOUTH);
                                        moveNorth(toMove);
                                    }
                                }


                            } else if (s.owner != myID) {
                                if (site.strength > s.strength) {

                                    Move move = new Move(loc, Direction.SOUTH);
                                    if (moveHandler.canBeAdded(move)) {
                                        moveHandler.add(move);
                                    }
                                } else {
                                    if (n.owner == myID && ((n.strength + site.strength > s.strength))) {
                                        Location toMove = gameMap.getLocation(loc, Direction.NORTH);
                                        moveSouth(toMove);
                                    }
                                }
                            } else {

                                if (site.strength > 30) {
                                    int northSteps = gameHelper.stepsToNonBuddy(loc, Direction.NORTH);
                                    int southSteps = gameHelper.stepsToNonBuddy(loc, Direction.SOUTH);

                                    if (northSteps < southSteps) {
                                        moveNorth(loc);
                                    } else {
                                        moveSouth(loc);
                                    }
                                }
                            }
                        }

                    }

                    if (runHorizontal) {

                        if (loc.y == startLocation.y) {

                            Site e = gameMap.getSite(loc, Direction.EAST);
                            Site w = gameMap.getSite(loc, Direction.WEST);

                            if (e.owner != myID) {

                                if (site.strength > e.strength) {

                                    Move move = new Move(loc, Direction.EAST);
                                    if (moveHandler.canBeAdded(move)) {
                                        moveHandler.add(move);
                                    }
                                } else {
                                    if (w.owner == myID && ((w.strength + site.strength > e.strength))) {
                                        Location toMove = gameMap.getLocation(loc, Direction.WEST);
                                        moveNorth(toMove);
                                    }
                                }


                            } else if (w.owner != myID) {
                                if (site.strength > w.strength) {

                                    Move move = new Move(loc, Direction.WEST);
                                    if (moveHandler.canBeAdded(move)) {
                                        moveHandler.add(move);
                                    }
                                } else {
                                    if (e.owner == myID && ((e.strength + site.strength > w.strength))) {
                                        Location toMove = gameMap.getLocation(loc, Direction.EAST);
                                        moveWest(toMove);
                                    }
                                }
                            } else {

                                if (site.strength > 30) {
                                    int eastSteps = gameHelper.stepsToNonBuddy(loc, Direction.EAST);
                                    int westSteps = gameHelper.stepsToNonBuddy(loc, Direction.WEST);

                                    if (eastSteps < westSteps) {
                                        moveEast(loc);
                                    } else {
                                        moveWest(loc);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return true;
    }

    private void moveNorth(Location location) {
        Move move = new Move(location, Direction.NORTH);
        if (moveHandler.canBeAdded(move)) {
            moveHandler.add(move);
        }
    }

    private void moveSouth(Location location) {
        Move move = new Move(location, Direction.SOUTH);
        if (moveHandler.canBeAdded(move)) {
            moveHandler.add(move);
        }
    }

    private void moveEast(Location location) {
        Move move = new Move(location, Direction.EAST);
        if (moveHandler.canBeAdded(move)) {
            moveHandler.add(move);
        }
    }

    private void moveWest(Location location) {
        Move move = new Move(location, Direction.WEST);
        if (moveHandler.canBeAdded(move)) {
            moveHandler.add(move);
        }
    }

    private boolean hasCross(Location midPosition) {

        return (gameHelper.ownedSites() >= 5);

        /*
        if (gameMap.getSite(midPosition, Direction.NORTH).owner != myID) {
            return false;
        }

        if (gameMap.getSite(midPosition, Direction.EAST).owner != myID) {
            return false;
        }

        if (gameMap.getSite(midPosition, Direction.SOUTH).owner != myID) {
            return false;
        }

        if (gameMap.getSite(midPosition, Direction.WEST).owner != myID) {
            return false;
        }

        return true;
        */
    }

    private void crossSetup(Location midPosition) {
        Site site = gameMap.getSite(midPosition);

        Location nLocation = gameMap.getLocation(midPosition, Direction.NORTH);
        Site nSite = gameMap.getSite(nLocation);

        Location eLocation = gameMap.getLocation(midPosition, Direction.EAST);
        Site eSite = gameMap.getSite(eLocation);

        Location sLocation = gameMap.getLocation(midPosition, Direction.SOUTH);
        Site sSite = gameMap.getSite(sLocation);

        Location wLocation = gameMap.getLocation(midPosition, Direction.WEST);
        Site wSite = gameMap.getSite(wLocation);


        if (nSite.owner != myID && site.strength > nSite.strength) {
            moveNorth(midPosition);
            return;
        }

        if (eSite.owner != myID && site.strength > eSite.strength) {
            moveEast(midPosition);
            return;
        }

        if (sSite.owner != myID && site.strength > sSite.strength) {
            moveSouth(midPosition);
            return;
        }

        if (wSite.owner != myID && site.strength > wSite.strength) {
            moveWest(midPosition);
            return;
        }

        int nRequires = (nSite.owner != myID ? nSite.strength - site.strength : 1000);
        int eRequires = (eSite.owner != myID ? eSite.strength - site.strength : 1000);
        int sRequires = (sSite.owner != myID ? sSite.strength - site.strength : 1000);
        int wRequires = (wSite.owner != myID ? wSite.strength - site.strength : 1000);

        int totStrength = 0;

        if (nSite.owner == myID) {
            totStrength += nSite.strength;
        }
        if (eSite.owner == myID) {
            totStrength += eSite.strength;
        }
        if (sSite.owner == myID) {
            totStrength += sSite.strength;
        }
        if (wSite.owner == myID) {
            totStrength += wSite.strength;
        }

        if (totStrength > nRequires || totStrength > eRequires || totStrength > sRequires || totStrength > wRequires) {

            int bestStrength = 0;

            if (nSite.owner == myID) {
                if (nSite.strength > bestStrength) {
                    bestStrength = nSite.strength;
                }
            }
            if (eSite.owner == myID) {
                if (eSite.strength > bestStrength) {
                    bestStrength = eSite.strength;
                }
            }
            if (sSite.owner == myID) {
                if (sSite.strength > bestStrength) {
                    bestStrength = sSite.strength;
                }
            }
            if (wSite.owner == myID) {
                if (wSite.strength > bestStrength) {
                    bestStrength = wSite.strength;
                }
            }

            if (nSite.owner == myID && nSite.strength == bestStrength) {
                moveSouth(nLocation);
            }
            else if (eSite.owner == myID && eSite.strength == bestStrength) {
                moveWest(eLocation);
            }
            else if (sSite.owner == myID && sSite.strength == bestStrength) {
                moveNorth(sLocation);
            }
            else if (wSite.owner == myID && wSite.strength == bestStrength) {
                moveEast(wLocation);
            }
        }
    }
}
