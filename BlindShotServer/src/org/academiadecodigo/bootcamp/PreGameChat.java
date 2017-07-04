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
            sendMessage(Integer.toString(player+1));
            receiveMessage();
            receiveMessage();
            sendMessage("f");
            System.out.println("Thread of pregamechat died");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveMessage() throws IOException {
        System.out.println("receive");
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message = in.readLine();
        System.out.println(message);
    }

    public void sendMessage(String message) throws IOException {
        System.out.println("send");
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        out.write(message + "\n");
        out.flush();
    }
}
