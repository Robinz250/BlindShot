package org.academiadecodigo.bootcamp;

import java.awt.*;
import java.net.Socket;

/**
 * Created by ruimorais on 08/07/17.
 */
public class Client {

    private Point point;
    private int id;
    private Socket clientSocket;
    private String name;
    private boolean dead;

    public Client(Socket clientSocket, int id) {
        this.clientSocket = clientSocket;
        this.id = id;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }
}
