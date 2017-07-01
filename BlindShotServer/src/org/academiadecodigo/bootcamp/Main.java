package org.academiadecodigo.bootcamp;

import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created by codecadet on 28/06/17.
 */
public class Main {


    public static final int NUMBEROFPLAYERS = 2;

    public static void main(String[] args) {

        LinkedList<Socket> clients = new LinkedList<>();
        Server server = new Server(clients);

        try {

            server.socketConnect();
            server.acceptClient(NUMBEROFPLAYERS);

            //server.messageHandle();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
