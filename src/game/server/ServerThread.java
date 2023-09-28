package game.server;

import game.server.model.Player;

import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.List;

public class ServerThread extends Thread {
    private Socket connSocket;
    private DataOutputStream outToClient;
    private String name;

    public ServerThread(Socket connSocket) {
        this.connSocket = connSocket;
    }

    public void run() {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()));
            outToClient = new DataOutputStream(connSocket.getOutputStream());

            while (connSocket.isConnected()) {
                String clientSentence = inFromClient.readLine();
                if (name == null) {
                    name = clientSentence;
                    System.out.println("Client name: " + name);
                    GameLogic.makePlayers(name, this);
//                    outToClient.writeBytes("Your name is: " + clientSentence + '\n');
                    return;
                }

                outToClient.writeBytes(clientSentence + '\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateState() throws IOException {
        // send state to client
        final List<Player> players = GameLogic.players;

        String state = Arrays.toString(players.stream()
                .map(Player::serializePlayer)
                .toArray());

        System.out.println("Sending state: " + state);

        outToClient.writeBytes(state + '\n');
    }
}
