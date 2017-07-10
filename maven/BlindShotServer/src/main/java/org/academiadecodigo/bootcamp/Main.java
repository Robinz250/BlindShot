package org.academiadecodigo.bootcamp;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by ruimorais on 08/07/17.
 */
public class Main {
    public static void main(String[] args) {
        /**
         * 1st arg is the number of players to connect
         */
        Server.NUMBER_OF_CLIENTS = Integer.parseInt(args[0]);
        try {
            System.out.println(InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            System.out.println("something happened");
        }

        Server server = new Server();
        server.init();

        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
