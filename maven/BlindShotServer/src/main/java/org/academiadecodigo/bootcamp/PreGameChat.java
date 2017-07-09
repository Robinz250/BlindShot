package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;

/**
 * Created by ruimorais on 02/07/17.
 */
public class PreGameChat implements Runnable {

    private Socket clientSocket;
    private int player;

    public PreGameChat(Socket clientSocket, int player) {
        this.clientSocket = clientSocket;
        this.player = player;
    }

    public void run() {

        try {
            sendMessage(Integer.toString(player+1));
            receiveMessage();
            receiveMessage();
            sendMessage("Player " + (player+1) + " is ready to play");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void receiveMessage() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message = in.readLine();
        System.out.println(message);
    }

    private void sendMessage(String message) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        out.write(message + "\n");
        out.flush();
    }
}
