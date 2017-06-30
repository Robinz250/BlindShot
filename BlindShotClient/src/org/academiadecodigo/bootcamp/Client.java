package org.academiadecodigo.bootcamp;

import javafx.fxml.Initializable;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by codecadet on 28/06/17.
 */
public class Client implements Runnable {

    private final int port = 9999;
    private final String host = "localhost";
    private Socket clientSocket;
    private String playerName;
    private int i = 0;
    private int player;
    private int turn = 0;
    private int numberOfPlayers = 2;

    @Override
    public void run() {
        try {
            connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connect() throws IOException {

        clientSocket = new Socket(host, port);

        recieveMessage();

        while (true) {

            if (turn == numberOfPlayers) {
                turn = 0;
            }

            System.out.println("turn: " + turn);

            System.out.println(player + numberOfPlayers + 1);

            if (turn == player) {
                sendMessage();
                turn++;
            }

            else {
                recieveMessage();
                turn++;
            }
        }
    }

    public void recieveMessage() throws IOException {

        System.out.println("receive");

        BufferedReader bReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message = bReader.readLine();
        System.out.println(message);

        if (i == 0) {

            player = Integer.parseInt(message.substring(message.length() - 1));

            System.out.println("player: " + player);

        }

        i++;

    }

    public void sendMessage() throws IOException {

        System.out.println("send");

        BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        Scanner scanner = new Scanner(System.in);

        String message = scanner.nextLine();

        message += "\n";

        bWriter.write(message);

        bWriter.flush();

    }

    public Socket getSocket() {
        return clientSocket;
    }

    public int getPlayer() {
        return player;
    }
}
