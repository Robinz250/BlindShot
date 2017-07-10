package org.academiadecodigo.bootcamp.model;

import java.awt.*;
import java.io.IOException;
import java.net.Socket;

/**
 * Created by ruimorais on 08/07/17.
 */
public class Player {

    private Socket clientSocket;
    private int id;
    private boolean dead;

    public void init() {
        try {
            clientSocket = new Socket("localhost", 6666);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {

    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    public boolean isDead() {
        return dead;
    }
}
