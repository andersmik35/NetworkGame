package game.client;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

public class TCPClient extends Thread {
    private final Socket socket;
    private final String name;

    private DataOutputStream outToServer;

    private static TCPClient instance;

    public TCPClient(String ip, String name) throws Exception {
        instance = this;

        this.name = name;
        this.socket = new Socket(ip, 6789);
    }

    @Override
    public void run() {
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        try {
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes(name + '\n');

            while (socket.isConnected()) {
                String serverSentence = inFromServer.readLine();
                System.out.println("SERVER: " + serverSentence);

                if (serverSentence.startsWith("state:")) {
                    String gameData = serverSentence.substring(6);
                    // { "player1":0:0:"right":0 },{ "player2":3:5:"up":1 };6,4
                    updateGui(gameData);
                } else if (serverSentence.startsWith("WINNER:")) {
                    String[] data = serverSentence.split(":");
                    Gui.gameOver(data[1]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) {
        try {
            outToServer.writeBytes(msg + '\n');
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void updateGui(String serializedData) {
        String[] players = serializedData.split(",");
        PlayerUpdate[] updates = new PlayerUpdate[players.length];

        String[] treasure = players[players.length - 1].split(";");

        players[players.length - 1] = treasure[0];

        for (int i = 0; i < players.length; i++) {
            String player = players[i];
            player = player.substring(1, player.length() - 1);

            String[] playerInfo = player.split(":");

            String name = playerInfo[0];
            String direction = playerInfo[3];

            int newX = Integer.parseInt(playerInfo[1]);
            int newY = Integer.parseInt(playerInfo[2]);
            Pair newPos = new Pair(newX, newY);

            int points = Integer.parseInt(playerInfo[4]);

            updates[i] = new PlayerUpdate(name, newPos, direction, points);
        }

        if (treasure.length > 1) {
            String[] treasurePos = treasure[1].split(":");
            int treasureX = Integer.parseInt(treasurePos[0]);
            int treasureY = Integer.parseInt(treasurePos[1]);
            Pair treasurePair = new Pair(treasureX, treasureY);

            Gui.placeTreasureOnScreen(treasurePair);
        }

        Gui.updateGui(updates);
    }

    public static TCPClient getInstance() {
        return instance;
    }
}


