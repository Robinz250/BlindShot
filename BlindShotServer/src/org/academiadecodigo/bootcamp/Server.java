package org.academiadecodigo.bootcamp;

import javafx.geometry.Pos;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

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
    private Point attack;
    //private Map<Socket, Point> clients = new HashMap<>();
    private int deadPlayers;

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

        receivePlayersPosition();
        startGameChat();

        //createPlayers();

    }

    public String receivePlayersPosition() {

        String message = null;

        for (int i = 0; i < clientSockets.length; i++) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSockets[i].getInputStream()));

                message = in.readLine();
                System.out.println(message);
                String[] string = message.split(" ");
                for (int j = 0; j < players.length; j++) {
                    players[j] = new Point(Integer.parseInt(string[3]), Integer.parseInt(string[5]));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //convert String to Point
        //players[0] = new Point(collumn, row);

        return message;
    }

    public void gameChat() {
        //recebe uma string com a informação acerca de para onde o jogador se moveu e onde atacou; atualiza a sua posiçao
    }

    public void comparePlayers() {
        //compara a posiçao dos dois jogadores depois de um jogar
    }

    public void startGameChat() {

        while (turn < clientSockets.length) {

            String move;
            String attack;

            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSockets[turn].getInputStream()));

                move = in.readLine();
                String[] moves = move.split(" ");
                System.out.println(move);
                players[turn] = new Point(Integer.parseInt(moves[7]), (Integer.parseInt(moves[11])));

                attack = in.readLine();
                System.out.println(attack);

                String[] attacks = attack.split(" ");
                this.attack = new Point(Integer.parseInt(attacks[7]), (Integer.parseInt(attacks[11])));

                turn++;

                if (turn == clientSockets.length) {
                    turn = 0;
                }

                for (Point point : players) {
                    if (point.getX() == this.attack.getX() && point.getY() == this.attack.getY()) {
                        deadPlayers++;
                    }
                }

                for (int i = 0; i < clientSockets.length; i++) {
                    try {
                        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSockets[i].getOutputStream()));
                        out.write(Integer.toString((turn + 1)) + " | " + attack + " | " + attackRecieve(attacks, i) +"\n");
                        out.flush();
                        System.out.println("turn: " + (turn + 1));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public String attackRecieve(String[] attacks, int i) {

            System.out.println("player " + i + " x " + players[i].getX());
            System.out.println("attack x: " + attack.getX());
            System.out.println("player " + i + " y: " + players[i].getY());
            System.out.println("attack y: " + attack.getY());
            System.out.println("who attacked? " + Integer.parseInt(attacks[1]));
            System.out.println("dead: " + deadPlayers);
            System.out.println("i: " + i);

            if (players[i].getX() == attack.getX() && players[i].getY() == attack.getY() && Integer.parseInt(attacks[1]) != (i+1)) {
                System.out.println("YOU LOOSE");
                return "YOU LOOSE";
            }

            if (((players.length - 1) == deadPlayers) && (players[i].getX() != attack.getX() || players[i].getY() != attack.getY())) {
                System.out.println("YOU WIN");
                return "YOU WIN";
            }
        return "MISS";
    }
}
