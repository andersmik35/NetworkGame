package game.client;


import javafx.application.Application;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {

    public static void main(String[] args) throws Exception {
        BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Indtast IP");
        String ip = inFromUser.readLine();

        System.out.println("Indtast navn");
        String name = inFromUser.readLine();

        TCPClient client = new TCPClient(ip, name);
        client.start();

        Application.launch(Gui.class);
    }
}