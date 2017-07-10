package org.academiadecodigo.bootcamp.service;

import org.academiadecodigo.bootcamp.model.Player;

import java.io.*;

/**
 * Created by ruimorais on 08/07/17.
 */
public class GameService {

    private Player player;
    public static int NUMBER_OF_PLAYERS;

    public void sendMessage(String message) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(player.getClientSocket().getOutputStream()));
        out.write(message + "\n");
        out.flush();
    }

    public String receiveMessage() {
        String message = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(player.getClientSocket().getInputStream()));
            message = in.readLine();
            System.out.println(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}

