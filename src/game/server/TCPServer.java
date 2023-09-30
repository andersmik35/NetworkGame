package game.server;

import java.net.*;

public class TCPServer {
    public static void main(String[] args) throws Exception {
        try (ServerSocket welcomeSocket = new ServerSocket(6789)) {
            System.out.println("Server started");
            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                System.out.println("Client connected from " + connectionSocket.getInetAddress().getHostAddress());

                (new ServerThread(connectionSocket)).start();
            }
        }
    }
}
