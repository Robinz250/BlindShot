package org.academiadecodigo.bootcamp;

import javafx.fxml.Initializable;
import org.academiadecodigo.bootcamp.controller.GridController;
import org.academiadecodigo.bootcamp.utils.Navigation;

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

    private String move;

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

                while (((GridController) Navigation.getInstance().getControllers().get("grid")).getMove() == null) {

                }

                System.out.println(((GridController) Navigation.getInstance().getControllers().get("grid")).getMove());

                sendMessage(((GridController) Navigation.getInstance().getControllers().get("grid")).getMove() + "\n");


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

        //((GridController) Navigation.getInstance().getControllers().get("grid")).movePlayer(message);

        if (i == 0) {

            player = Integer.parseInt(message.substring(message.length() - 1));

            System.out.println("player: " + player);

        }

        i++;

    }

    public void sendMessage(String move) throws IOException {

        System.out.println("send");

        BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        Scanner scanner = new Scanner(System.in);

        //String message = scanner.nextLine();

        //message += "\n";

        //bWriter.write(message);

        bWriter.write(move);

        bWriter.flush();

    }

    public Socket getSocket() {
        return clientSocket;
    }

    public int getPlayer() {
        return player;
    }

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }
}
