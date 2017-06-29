package org.academiadecodigo.bootcamp;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by codecadet on 28/06/17.
 */
public class Main {


    public static final int NUMBEROFPLAYERS = 2;

    public static void main(String[] args) {

        Map<Integer, Socket> clients = new HashMap<>();
        Server server = new Server(clients);

        try {

            server.socketConnect();
            server.acceptClient(NUMBEROFPLAYERS);

            server.messageHandle();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
