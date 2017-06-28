package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Created by codecadet on 28/06/17.
 */
public class Server {

    private ServerSocket socket;
    private LinkedList<Socket> clients;
    private String message;

    public Server(LinkedList<Socket> clients) {
        this.clients = clients;

    }

    public void acceptClient(int players) throws IOException {
        while (clients.size() < players) {

            clients.add(socket.accept());
        }
    }

    public void socketConnect() throws IOException {
        socket = new ServerSocket(9999);
    }

    public void messageHandle() throws IOException {

        write();
        read();
    }

    private void write() throws IOException {

        BufferedWriter bwriter = new BufferedWriter(new OutputStreamWriter(clients.get(0).getOutputStream()));

        Scanner scanner  = new Scanner(System.in);

        message = scanner.nextLine();

        message += "\n";

        bwriter.write(message);

        bwriter.flush();

    }

    private void read() throws IOException {

        BufferedReader bReader = new BufferedReader(new InputStreamReader(clients.get(0).getInputStream()));

        System.out.println(bReader.readLine());

    }

}
