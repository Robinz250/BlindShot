package org.academiadecodigo.bootcamp;

import java.net.Socket;

import java.util.LinkedList;

/**
 * Created by codecadet on 24/06/17.
 */
public class GameLogic {
    private LinkedList<Socket> clients;

    public void init(LinkedList<Socket> clients) {
        this.clients = clients;
    }

    public void start() {

    }
}
