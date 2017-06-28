package org.academiadecodigo.bootcamp;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Created by codecadet on 28/06/17.
 */
public class Client {

    private final int port = 9999;
    private final String host = "localhost";
    Socket socket;

    public void connect() throws IOException {

        socket = new Socket(host, port);

    }

    public void recieveMessage() throws IOException {

        BufferedReader bReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(bReader.readLine());

    }

    public void sendMessage() throws IOException {

        BufferedWriter bWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        Scanner scanner = new Scanner(System.in);

        String message = scanner.nextLine();

        message += "\n";

        bWriter.write(message);

        bWriter.flush();

    }

    public Socket getSocket() {
        return socket;
    }

}
