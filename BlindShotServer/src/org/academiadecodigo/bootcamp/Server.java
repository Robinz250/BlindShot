package org.academiadecodigo.bootcamp;

import javafx.geometry.Pos;

import java.awt.*;
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
    private Point players[] = new Point[NUMBER_OF_PLAYERS];

    public void init() throws IOException {

        serverSocket = new ServerSocket(6666);
        clientSockets = new Socket[NUMBER_OF_PLAYERS];
        threads = new Thread[NUMBER_OF_PLAYERS];

    }

    public void start() throws IOException, InterruptedException {

        for (int i = 0; i < 2; i++) {
            clientSockets[i] = serverSocket.accept();
            threads[i] = new Thread(new PreGameChat(clientSockets[i], i));
            threads[i].start();
        }

        threads[0].join();
        threads[1].join();
        startGame();
    }

    public void startGame() throws IOException {

        System.out.println("kk");

        for (Socket socket : clientSockets) {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write("Let the games begin! \n");
            out.flush();
        }
        startGameChat();

        //createPlayers();

    }

    public void createPlayers(String position) {

        //convert String to Point
        //players[0] = new Point(collumn, row);

        startGameChat();

    }

    public void gameChat() {
        //recebe uma string com a informação acerca de para onde o jogador se moveu e onde atacou; atualiza a sua posiçao
    }

    public void comparePlayers() {
        //compara a posiçao dos dois jogadores depois de um jogar
    }

    public void startGameChat() {

        while (turn < clientSockets.length) {

            String message;
            BufferedReader in = null;
            try {
                in = new BufferedReader(new InputStreamReader(clientSockets[turn].getInputStream()));

                message = in.readLine();
                System.out.println(message);

                message = in.readLine();
                System.out.println(message);

                turn++;

                if (turn == clientSockets.length) {
                    turn = 0;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
