package org.academiadecodigo.bootcamp;

import javafx.application.Platform;
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

        receive();

        sendMessage();

        receive();

        recieveMessage();

        while (true) {

            if (turn == numberOfPlayers) {
                turn = 0;
            }

            System.out.println("turn: " + turn);

            System.out.println(player + numberOfPlayers + 1);

            if (turn == player) {

                sendMove(((GridController) Navigation.getInstance().getControllers().get("grid")).getMove());
                turn++;
            }

            else {

                receive();
                turn++;
            }
        }
    }

    public void recieveMessage() throws IOException {

        System.out.println("receive");

        BufferedReader bReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        while (true) {

            if (bReader.readLine() != null) {
                break;
            }
        }

        System.out.println("let the games begin");
        bReader.close();

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Navigation.getInstance().loadScreen("grid");
            }
        });

    }

    public void receive() {

        try {

            BufferedReader bReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message = "";

            while (!(message = bReader.readLine()).equals("")) {

                System.out.println(message);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMove(String move) throws IOException {

        System.out.println("send");

        BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        bWriter.write(move);

        bWriter.flush();

    }

    public void sendMessage() throws IOException {

        System.out.println("send");

        BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

        Scanner scanner = new Scanner(System.in);

        String message = scanner.nextLine() + "\n";

        //message += "\n";

        bWriter.write(message);

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
