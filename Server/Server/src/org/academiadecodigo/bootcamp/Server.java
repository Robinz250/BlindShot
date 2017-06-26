package org.academiadecodigo.bootcamp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

/**
 * Created by codecadet on 24/06/17.
 */
public class Server {

    private static ServerSocket socket;
    private static LinkedList<Socket> clients;

    public Server(LinkedList<Socket> clients) {
        this.clients = clients;
    }

    /**
     * Accepting players' connections.
     * Players can say how many will play in the same game
     *
     * @param players
     * @throws IOException
     */
    public void acceptClient(int players) throws IOException {
        while (clients.size() < players) {
            clients.add(socket.accept());
        }
    }

    /**
     * Starts server socket in port 9999
     *
     * @throws IOException
     */
    public void socketConnect() throws IOException {
        socket = new ServerSocket(9999);
    }
}
