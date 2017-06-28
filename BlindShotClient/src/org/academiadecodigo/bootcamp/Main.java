package org.academiadecodigo.bootcamp;

import java.io.IOException;

/**
 * Created by codecadet on 28/06/17.
 */
public class Main {
    public static void main(String[] args) {

        Client client = new Client();
        try {

            client.connect();
            client.recieveMessage();
            client.sendMessage();


        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}
