package game.client;

import game.client.Pair;

import java.io.*;
import java.lang.reflect.Array;
import java.net.*;
import java.util.Arrays;

public class TCPClient extends Thread {
    private final Socket socket;
    private final String name;

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

            DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
            outToServer.writeBytes(name + '\n');

            while (socket.isConnected()) {
                String serverSentence = inFromServer.readLine();
                System.out.println("SERVER: " + serverSentence);

                if (serverSentence.startsWith("state:")) {
                    String serializedPlayers = serverSentence.substring(6);
                    updateGui(serializedPlayers);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String msg) throws Exception {
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        outToServer.writeBytes(msg + '\n');
    }

    private void updateGui(String serializedPlayers) {
        String[] players = serializedPlayers.split(",");
        PlayerUpdate[] updates = new PlayerUpdate[players.length];

        for (int i = 0; i < players.length; i++) {
            String player = players[i];
            player = player.substring(1, player.length() - 1);

            String[] playerInfo = player.split(":");

            System.out.println(Arrays.toString(playerInfo));
            String name = playerInfo[0];
            String direction = playerInfo[3];

            int newX = Integer.parseInt(playerInfo[1]);
            int newY = Integer.parseInt(playerInfo[2]);
            Pair newPos = new Pair(newX, newY);

            int points = Integer.parseInt(playerInfo[4]);

            updates[i] = new PlayerUpdate(name, newPos, direction, points);
        }

        System.out.println(Arrays.toString(updates));
        Gui.updateGui(updates);
    }

    public static TCPClient getInstance() {
        return instance;
    }
}


