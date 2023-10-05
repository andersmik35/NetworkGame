package game.server;

import game.server.model.Player;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;

public class ServerThread extends Thread {
    private Socket connSocket;
    private Player player;
    private ClientHandler clientHandler;

    private DataOutputStream outToClient;

    public ServerThread(Socket connSocket) {
        this.connSocket = connSocket;
    }

    public void run() {
        try (BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connSocket.getInputStream()))) {

            outToClient = new DataOutputStream(connSocket.getOutputStream());

            String name = inFromClient.readLine();
            System.out.println("Received from client: " + name);

            clientHandler = new ClientHandler(this);

            player = GameLogic.makePlayers(name);

            // send welcome message
            outToClient.writeBytes("Welcome to the game " + name + "!\n");

            while (!connSocket.isClosed()) {
                String clientSentence = inFromClient.readLine();
                if (clientSentence == null) {
                    break;
                }

                if (clientSentence.startsWith("move:")) {
                    String[] parts = clientSentence.split(":");

                    int x = Integer.parseInt(parts[1]);
                    int y = Integer.parseInt(parts[2]);
                    String direction = parts[3];

                    GameLogic.updatePlayer(player, x, y, direction);
                }
            }

        } catch (SocketException se) {
            System.out.println("Client disconnected: " + player.getName());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientHandler.stop();
                GameLogic.removePlayer(player);
                connSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateState(String state) throws IOException {
        outToClient.writeBytes("state:" + state + '\n');
    }
}
