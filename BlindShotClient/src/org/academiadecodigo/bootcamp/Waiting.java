package org.academiadecodigo.bootcamp;

import javafx.application.Platform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by codecadet on 03/07/17.
 */
public class Waiting implements Runnable {

    private Socket clientSocket;

    public Waiting(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String message;
            message = in.readLine();
            System.out.println(message);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    Navigation.getInstance().loadScreen("grid");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
