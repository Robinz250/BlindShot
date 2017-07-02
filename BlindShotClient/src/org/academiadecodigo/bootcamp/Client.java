package org.academiadecodigo.bootcamp;

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

    @Override
    public void run() {

        try {
            connectToServer();
            startChat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectToServer() throws IOException {
        clientSocket = new Socket("localhost", 6666);
    }

    public void startChat() throws IOException {

        receiveMessage();
        sendMessage(readFromKeyboard());
        String message = receiveMessage();
        player = Character.getNumericValue(message.charAt(message.length()-1));
        receiveMessage();

    }

    public String receiveMessage() throws IOException {
        System.out.println("receive");
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message = in.readLine();
        System.out.println(message);
        return message;
    }

    public void sendMessage(String message) throws IOException {
        System.out.println("send");
        out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
        out.write(message + "\n");
        out.flush();
    }

    public String readFromKeyboard() throws IOException {
        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        return keyboard.readLine();
    }

    public int getPlayer() {
        return player;
    }

    public Socket getClientSocket() {
        return clientSocket;
    }
}
