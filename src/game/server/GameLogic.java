package game.server;

import game.client.Generel;
import game.client.Gui;
import game.server.model.Pair;
import game.server.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GameLogic {
    public static List<Player> players = new ArrayList<Player>();
    public static Player me;

    public static void makePlayers(String name, ServerThread serverThread) {
        Pair p = getRandomFreePosition();
        me = new Player(name, p, "up");
        players.add(me);

        new ClientHandler(me, serverThread);

        ClientHandler.sendStateToAll();
    }

    // finds a random new position which is not wall
    // and not occupied by other players
    public static Pair getRandomFreePosition() {
        int x = 1;
        int y = 1;
        boolean foundFreePos = false;
        while (!foundFreePos) {
            Random r = new Random();
            x = Math.abs(r.nextInt() % 18) + 1;
            y = Math.abs(r.nextInt() % 18) + 1;
            // er det gulv ?
            if (Generel.board[y].charAt(x) == ' ') {
                foundFreePos = true;
                for (Player p : players) {
                    if (p.getXpos() == x && p.getYpos() == y) //pladsen optaget af en anden
                        foundFreePos = false;
                }

            }
        }
        Pair p = new Pair(x, y);
        return p;
    }

    public static void updatePlayer(int deltaX, int deltaY, String direction) {
        me.setDirection(direction);
        int x = me.getXpos(), y = me.getYpos();

        if (isWall(x + deltaX, y + deltaY)) {
            me.addPoints(-1);
        } else {
            // collision detection
            Player p = getPlayerAt(x + deltaX, y + deltaY);
            if (p != null) {
                me.addPoints(10);
                //update the other player
                p.addPoints(-10);
                Pair pa = getRandomFreePosition();
                p.setLocation(pa);
                Pair oldpos = new Pair(x + deltaX, y + deltaY);

//                Gui.movePlayerOnScreen(oldpos, pa, p.getDirection());
                ClientHandler.sendStateToAll();

            } else
                me.addPoints(1);
            Pair oldpos = me.getLocation();
            Pair newpos = new Pair(x + deltaX, y + deltaY);

//            Gui.movePlayerOnScreen(oldpos, newpos, direction);
            // Send besked til spillere om at spilleren er flyttet

            me.setLocation(newpos);
        }
    }

    public static Player getPlayerAt(int x, int y) {
        for (Player p : players) {
            if (p.getXpos() == x && p.getYpos() == y) {
                return p;
            }
        }
        return null;
    }

    public static boolean isWall(int x, int y) {
        return Generel.board[y].charAt(x) == 'w';
    }
}
