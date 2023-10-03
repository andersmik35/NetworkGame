package game.server;

import game.server.model.Player;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ClientHandler {
    private static final Set<ClientHandler> clientHandlers = new HashSet<>();
    private final ServerThread serverThread;

    public ClientHandler(ServerThread serverThread) {
        this.serverThread = serverThread;
        clientHandlers.add(this);
    }

    public static void sendStateToAll(String state) {
        for (ClientHandler clientHandler : clientHandlers) {
            clientHandler.updateState(state);
        }
    }

    private void updateState(String state) {
        try {
            serverThread.updateState(state);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
