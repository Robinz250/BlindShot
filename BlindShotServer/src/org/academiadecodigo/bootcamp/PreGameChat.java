package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;

/**
 * Created by ruimorais on 02/07/17.
 */
public class PreGameChat implements Runnable {

    private Socket clientSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private int player;

    public PreGameChat(Socket clientSocket, int player) {
        this.clientSocket = clientSocket;
        this.player = player;
    }

    @Override
    public void run() {

        try {
            sendMessage("What's your name?");
            String name = receiveMessage();
            sendMessage("Welcome, " + name + "! You will be player " + (player+1));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receiveMessage() throws IOException {
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message = in.readLine();
        System.out.println(message);
        return message;
    }

    public void sendMessage(String message) throws IOException {
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        out.write(message + "\n");
        out.flush();
    }
}
