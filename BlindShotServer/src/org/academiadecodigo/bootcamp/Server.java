package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ruimorais on 02/07/17.
 */
public class Server {

    public static final int NUMBER_OF_PLAYERS = 2;
    private ServerSocket serverSocket;
    private Socket[] clientSockets;
    private Thread[] threads;
    private int turn = 0;

    public void init() throws IOException {

        serverSocket = new ServerSocket(6666);
        clientSockets = new Socket[NUMBER_OF_PLAYERS];
        threads = new Thread[NUMBER_OF_PLAYERS];

    }

    public void start() throws IOException, InterruptedException {

        for (int i = 0; i < clientSockets.length; i++) {
            clientSockets[i] = serverSocket.accept();
            threads[i] = new Thread(new PreGameChat(clientSockets[i], i));
            threads[i].start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        startGame();
    }

    public void startGame() throws IOException {

        System.out.println("kk");

        for (Socket socket : clientSockets) {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write("Let the games begin! \n");
            out.flush();
        }

        String message;
        BufferedReader in = new BufferedReader(new InputStreamReader(clientSockets[0].getInputStream()));

        while ((message = in.readLine()) == null) {
            message = in.readLine();
        }
        System.out.println(message);
    }

}
