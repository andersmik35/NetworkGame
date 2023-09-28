package game.client;

import game.client.Pair;

import java.io.*;
import java.net.*;
import java.util.Arrays;

public class TCPClient extends Thread {
    private Socket socket;
    private String name;

    private static TCPClient instance;

    public TCPClient(String ip, String name) throws Exception {
        instance = this;
        socket = new Socket(ip, 6789);
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                String msg = inFromUser.readLine();
                System.out.println("raw: " + msg);
                updateGui(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void send(String msg) throws Exception {
        DataOutputStream outToServer = new DataOutputStream(socket.getOutputStream());
        outToServer.writeBytes(msg + '\n');
    }

    private void updateGui(String serializedPlayers) {
        String[] players = serializedPlayers.split(",");
        System.out.println("Players: " + Arrays.toString(players));
        for (String player : players) {
            player = player.substring(1, player.length() - 1);
            String[] playerInfo = player.split(":");

            System.out.println(Arrays.toString(playerInfo));
            int x = Integer.parseInt(playerInfo[1]);
            int y = Integer.parseInt(playerInfo[2]);

            Pair newPos = new Pair(x, y);
            Gui.placePlayerOnScreen(newPos, playerInfo[3]);
        }
    }

    public static TCPClient getInstance() {
        return instance;
    }
}


