package org.academiadecodigo.bootcamp;

import java.io.IOException;

/**
 * Created by codecadet on 28/06/17.
 */
public class Main {

    public static void main(String[] args) throws IOException, InterruptedException {

        Server server = new Server();
        server.init();
        server.start();

    }
}
