package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by codecadet on 28/06/17.
 */
public class Server {

    private ServerSocket socket;
    private Map<Integer, Socket> clients;
    private String message;

    public Server(Map<Integer, Socket> clients) {

        this.clients = clients;

    }

    public void acceptClient(int players) throws IOException {

        int i = 0;

        while (clients.size() < players) {

            Socket clientSocket = socket.accept();

            clients.put(i, clientSocket);

            PrintStream out = new PrintStream(clientSocket.getOutputStream());

            out.println("Welcome player " + i);

            i++;

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

        System.out.println(message);

    }

}
