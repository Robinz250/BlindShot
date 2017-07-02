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
public class Client {

    private final int port = 9999;
    private final String host = "localhost";
    private Socket clientSocket;
    private String playerName;
    private int i = 0;
    private int player;
    private int turn = 1;
    private int numberOfPlayers = 2;
    private GridController controller;

    private String move;

    public void connect() throws IOException {

        clientSocket = new Socket(host, port);

        receive();

        sendMessage();

        //receive();

        String message = receive();
        String[] messagearray;

        messagearray = message.split(" ");

        System.out.println(messagearray[9]);

        player = message.lastIndexOf(message);

        recieveMessage();

    }

    public void startGame() {

        controller = (GridController) Navigation.getInstance().getControllers().get("grid");

        /*while (true) {

            if (turn == numberOfPlayers) {
                turn = 0;
            }*/

            System.out.println("turn: " + turn);

            System.out.println(player + numberOfPlayers + 1);

            synchronized (this) {

                if (turn == player) {

                    controller.move();
                    turn++;
                    System.out.println(turn);
                }
            }

            //else {

                controller.movePlayer("");

                turn++;

            //}
        }
    //}

    public String recieveMessage() throws IOException {

        System.out.println("receive");

        String message = null;

        BufferedReader bReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        while (true) {

            if ((message = bReader.readLine()) != null) {
                break;
            }
        }

        System.out.println(message);
        //bReader.close();

        /*Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Navigation.getInstance().loadScreen("grid");
            }
        });*/
        return message;
    }

    public String receive() {

        String message = "";

        try {

            BufferedReader bReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            /*while (!(message = bReader.readLine()).equals("")) {

                System.out.println(message);
            }*/

            message = bReader.readLine();
            System.out.println(message);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return message;
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
