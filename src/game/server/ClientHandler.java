package game.server;

import game.server.model.Player;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler {
    private static final Set<ClientHandler> clientHandlers = new HashSet<>();
    private final Player player;
    private final ServerThread serverThread;

    public ClientHandler(Player player, ServerThread serverThread) {
        this.player = player;
        this.serverThread = serverThread;
        clientHandlers.add(this);
    }

    public static void sendStateToAll() {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.updateState();
        }
    }

    private void updateState() {
        try {
            serverThread.updateState();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
