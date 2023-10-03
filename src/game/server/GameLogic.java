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

    public static Player makePlayers(String name, ServerThread serverThread) {
        Pair p = getRandomFreePosition();
        Player me = new Player(name, p, "up");
        players.add(me);

        new ClientHandler(serverThread);

        String state = me.serializePlayer(p);
        ClientHandler.sendStateToAll(state);

        return me;
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

    public static void updatePlayer(Player me, int deltaX, int deltaY, String direction) {
        me.setDirection(direction);
        int x = me.getXpos(), y = me.getYpos();

        if (isWall(x + deltaX, y + deltaY)) {
            me.addPoints(-1);
        } else {
            // collision detection
            Player other = getPlayerAt(x + deltaX, y + deltaY);

            if (other != null) {
                me.addPoints(10);
                //update the other player
                other.addPoints(-10);
                Pair pa = getRandomFreePosition();
                other.setLocation(pa);
                Pair oldpos = new Pair(x + deltaX, y + deltaY);

                // Send besked til spillere om at spilleren er flyttet
                String state = other.serializePlayer(oldpos);
                ClientHandler.sendStateToAll(state);
            } else
                me.addPoints(1);

            Pair oldpos = me.getLocation();
            Pair newpos = new Pair(x + deltaX, y + deltaY);

            me.setLocation(newpos);

            // Send besked til spillere om at spilleren er flyttet
            String state = me.serializePlayer(oldpos);
            ClientHandler.sendStateToAll(state);
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
