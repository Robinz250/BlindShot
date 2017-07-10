package org.academiadecodigo.bootcamp;

import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.util.LinkedList;

/**
 * Created by ruimorais on 08/07/17.
 */
public class Server {

    private ServerSocket serverSocket;
    private Client[] clients;
    public static final int NUMBER_OF_CLIENTS = 3;
    private int turn;
    private Thread[] threads;
    private LinkedList<Integer> deaths;

    public void init() {
        try {
            serverSocket = new ServerSocket(6666);
            clients = new Client[NUMBER_OF_CLIENTS];
            threads = new Thread[NUMBER_OF_CLIENTS];
            deaths = new LinkedList<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() throws IOException, InterruptedException {

        for (int i = 0; i < clients.length; i++) {
            clients[i] = new Client(serverSocket.accept(), i+1);
            System.out.println("connected");
            threads[i] = new Thread(new MessageService(clients[i]));
            threads[i].start();
        }

        System.out.println("Pregame is over");
        startGame();
    }

    private void startGame() throws IOException, InterruptedException {
        for (Thread thread : threads) {
            try {
                thread.join();
                System.out.println("thread died");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < clients.length; i++) {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clients[i].getClientSocket().getOutputStream()));
            try {
                System.out.println("let the games begin");
            out.write("Let the games begin! \n");
            out.flush();

        } catch (IOException e) {
                clients[i].getClientSocket().close();
                reconnect(i);
                i = 0;
            }
        }
        System.out.println("write your position");
        receivePlayersPosition();
        System.out.println("start game logic");
        startGameLogic();
    }

    private void reconnect(int i) throws IOException, InterruptedException {
        clients[i] = new Client(serverSocket.accept(), i+1);
        threads[i] = new Thread(new MessageService(clients[i]));
        threads[i].start();
        startGame();
    }

    private void receivePlayersPosition() {
        String message;
        for (int i = 0; i < clients.length; i++) {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(clients[i].getClientSocket().getInputStream()));
                message = in.readLine();
                String[] divide = message.split(" ");
                System.out.println("Player " + (i+1) + " position: " + Integer.parseInt(divide[0]) + " " + Integer.parseInt(divide[1]));
                clients[i].setPoint(new Point(Integer.parseInt(divide[0]), Integer.parseInt(divide[1])));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void startGameLogic() {
        while (turn < clients.length) {
            System.out.println("It's player " + (turn+1) + " turn");
            String message = receiveMessage(); // recebe mensagem com a posição do cliente e onde ele atacou - ID | POSIÇÃO | ATAQUE
            String[] divide = message.split(" ");
            System.out.println("Player " + (turn+1) + " attacked " + Integer.parseInt(divide[2]) + " " + Integer.parseInt(divide[3]));
            updatePositions(divide);
            String result = checkHit(divide); // verifica se algum cliente está na posição atacada
            sendMessage("Player " + (turn+1) + " " + result + " " + Integer.parseInt(divide[2]) + " " + Integer.parseInt(divide[3])); // envia mensagem aos restantes clientes com MISS/HIT
            System.out.println("turn0: " + turn);
            turn++;
            System.out.println("turn1: " + turn);
            if (turn == clients.length) {
                turn = 0;
            }
            System.out.println("turn2: " + turn);
            if (deaths.contains(turn)) {
                turn++;
                continue;
            }
        }
    }

    private String receiveMessage() {
        String message = null;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clients[turn].getClientSocket().getInputStream()));
            message = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(message);
        return message;
    }

    public String checkHit(String[] divide) {
        for (int i = 0; i < clients.length; i++) {
            if (turn != i && clients[i].getPoint().getX() == Integer.parseInt(divide[2]) && clients[i].getPoint().getY() == Integer.parseInt(divide[3])) {
                System.out.println("Player " + (i+1) + " was hit!");
                deaths.add(i);
                return "HIT Player " + (i + 1);
            }
        }
        System.out.println("Player " + (turn+1) + " missed!");
        return "MISSED . .";
    }

    public void sendMessage(String message) {
        for (int i = 0; i < clients.length; i++) {
            try {
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clients[i].getClientSocket().getOutputStream()));
                out.write(message + "\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void updatePositions(String[] divide) {
        for (int i = 0; i < clients.length; i++) {
            if (i == turn) {
                clients[i].getPoint().setLocation(Integer.parseInt(divide[0]), Integer.parseInt(divide[1]));
                System.out.println("Player " + (i+1) + " changed position to " + Integer.parseInt(divide[0]) + " " + Integer.parseInt(divide[1]));
            }
        }
    }
}
