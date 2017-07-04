package org.academiadecodigo.bootcamp;

import javafx.application.Platform;
import org.academiadecodigo.bootcamp.controller.MenuController;

import java.io.*;
import java.net.Socket;

/**
 * Created by ruimorais on 02/07/17.
 */
public class Client implements Runnable {

    private Socket clientSocket;
    private BufferedWriter out;
    private BufferedReader in;
    private int player;
    public static final int numberOfPlayers = 2;

    @Override
    public void run() {

        try {
            connectToServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToServer() throws IOException {
        clientSocket = new Socket("localhost", 6666); // conecta o cliente ao servidor
        waitForServerMessage(); // fica a espera da primeira mensagem do servidor, que lhe atribui o numero do player
    }

    public void waitForServerMessage() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            message = in.readLine();
            player = Integer.parseInt(message);
            System.out.println(player);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) throws IOException {
        System.out.println("sen");
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        out.write(message + "\n");
        out.flush();
    }

    public void receiveMessage() {
        System.out.println("receive");
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            message = in.readLine();
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getPlayer() {
        return player;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
