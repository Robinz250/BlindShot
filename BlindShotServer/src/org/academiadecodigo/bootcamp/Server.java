package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by codecadet on 28/06/17.
 */
public class Server {

    private ServerSocket socket;
    private LinkedList<Socket> clients;
    private String message;
    private final Thread[] threads = new Thread[2];

    public Server(LinkedList<Socket> clients) {

        this.clients = clients;

    }

    public void acceptClient(int players) throws IOException {

        int i = 0;

        BufferedWriter bWriter;

        while (clients.size() < players) {

            Socket clientSocket = socket.accept();

            threads[i] = new Thread(new PreGameChat(clientSocket, i, this));

            threads[i].start();

            clients.add(clientSocket);

            i++;

        }

        try {
            System.out.println("waiting for threads to die...");
            threads[0].join();
            System.out.println("thread 0 died");
            threads[1].join();
            System.out.println("thread 1 died");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startGame();

    }

    /*synchronized (this) {
        startGame();
        }
    }*/

    public void startGame() {

        System.out.println("let the games begin");

        for (Socket s : clients) {

                try {

                    BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
                    bWriter.write("Let the games begin... bitches! MUHUHAHAHAHA.. *cof*cof*");
                    bWriter.close();

                } catch (IOException e) {
                    e.printStackTrace();
            }

        }

    }

    public void socketConnect() throws IOException {
        socket = new ServerSocket(9999);
    }

    public void messageHandle() throws IOException {

        int i = 0;

        while (i < clients.size()) {

            read(i);

            //Insert GameLogicHere

            if (i  == (clients.size()-1)) {
                i = 0;
                continue;
            }

            i++;

        }
    }

    private void write(int i) throws IOException {

        BufferedWriter bwriter;

        for (int j = 0; j < clients.size(); j++) {
            if (j == i) {
                continue;
            }

            bwriter = new BufferedWriter(new OutputStreamWriter(clients.get(j).getOutputStream()));

            bwriter.write(message);

            bwriter.flush();

        }

    }

    private void read(int i) throws IOException {

        BufferedReader bReader = new BufferedReader(new InputStreamReader(clients.get(i).getInputStream()));

        message = bReader.readLine() + "\n";

        //System.out.println(message);

        write(i);

    }

}
