package org.academiadecodigo.bootcamp;

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
    private Point players[];
    private Point attack;
    private int deadPlayers;

    public void init() throws IOException {

        serverSocket = new ServerSocket(6666);
        clientSockets = new Socket[NUMBER_OF_PLAYERS];
        threads = new Thread[NUMBER_OF_PLAYERS];
        players = new Point[NUMBER_OF_PLAYERS];

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

        for (Socket socket : clientSockets) {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write("Let the games begin! \n");
            out.flush();
        }

        receivePlayersPosition();
        startGameLogic();

    }

    private void receivePlayersPosition() { //se calhar faz mais sentido ser o servidor a decidir as posições e a passá-las aos clientes..

        String message;

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
    }

    private void startGameLogic() {

        while (turn < clientSockets.length) {

            receiveMove();
            String attack = receiveAttack();
            turn++;
            if (turn == clientSockets.length) {
                turn = 0;
            }
            checkDeath();
            sendResult(attack);
        }
    }

    private void sendResult(String attack) {
        String[] attackSplited = attack.split(" ");
        for (int i = 0; i < clientSockets.length; i++) {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSockets[i].getOutputStream()));
                out.write(Integer.toString((turn + 1)) + " | " + attack + " | " + checkHit(attackSplited, i) +"\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String checkHit(String[] attacks, int i) {
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

    private void receiveMove() {
        String move;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSockets[turn].getInputStream()));
            move = in.readLine();
            String[] moves = move.split(" ");
            System.out.println(move);
            players[turn].setLocation(Integer.parseInt(moves[7]), (Integer.parseInt(moves[11])));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String receiveAttack() {
        String attack = null;
        String[] attackSplited;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSockets[turn].getInputStream()));
            attack = in.readLine();
            System.out.println(attack);
            attackSplited = attack.split(" ");
            this.attack = new Point(Integer.parseInt(attackSplited[7]), (Integer.parseInt(attackSplited[11])));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return attack;
    }

    private void checkDeath() {
        for (Point point : players) {
            if (point.getX() == this.attack.getX() && point.getY() == this.attack.getY()) {
                deadPlayers++;
            }
        }
    }
}
