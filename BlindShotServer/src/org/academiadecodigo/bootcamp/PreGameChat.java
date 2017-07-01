package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;

/**
 * Created by ruimorais on 01/07/17.
 */
public class PreGameChat implements Runnable {

    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private int id;
    private Server server;

    public PreGameChat(Socket clientSocket, int i, Server server) {
        this.clientSocket = clientSocket;
        this.id = i;
        this.server = server;

    }

    @Override
    public void run() {

        try {

            {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                sendMessage("Ahh, i see you have arrived! What's your name, mate? \n");
                String name = receiveMessage();

                sendMessage("Nice to meet you, " + name + "! \nYou will be player " + (id + 1) + "\nThe game will start as soon the other players connect...\n");

                //in.close();
                //out.close();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendMessage(String message) {

        try {
            out.write(message + "\n");
            out.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String receiveMessage() {

        String message = "";

        try {

            message = in.readLine();
            System.out.println(message);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;

    }
}
