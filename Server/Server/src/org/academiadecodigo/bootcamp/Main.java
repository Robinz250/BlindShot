package org.academiadecodigo.bootcamp;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created by codecadet on 25/06/17.
 */
public class Main {
    //TODO remove after the number of players being set by the players
    public static final int NUMBEROFPLAYERS = 2;

    private static LinkedList<Socket> clients;

    public static void main(String[] args) {
        clients = new LinkedList<>();
        Server server = new Server(clients);
        GameLogic game = new GameLogic();
        try {
            server.socketConnect();
            server.acceptClient(NUMBEROFPLAYERS);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        game.init(clients);
        game.start();
    }
}
