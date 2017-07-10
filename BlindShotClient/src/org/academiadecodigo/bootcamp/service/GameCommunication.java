package org.academiadecodigo.bootcamp.service;

import javafx.application.Platform;
import org.academiadecodigo.bootcamp.Navigation;
import org.academiadecodigo.bootcamp.controller.GameOverController;
import org.academiadecodigo.bootcamp.controller.GridController;
import org.academiadecodigo.bootcamp.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 * Created by ruimorais on 09/07/17.
 */
public class GameCommunication implements Runnable {

    private Player player;
    private int turn = 1;
    private String message;
    private LinkedList<Integer> deaths = new LinkedList<>();
    private LinkedList<Integer> players = new LinkedList<>();

    @Override
    public void run() {
        while (true) {
            try {
                GridController controller = (GridController) Navigation.getInstance().getControllers().get("grid");
                BufferedReader in = new BufferedReader(new InputStreamReader(player.getClientSocket().getInputStream()));
                message = in.readLine();
                System.out.println(message);
                String[] divide = message.split(" ");
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.drawAttack(Integer.parseInt(divide[5]), Integer.parseInt(divide[6]));
                        controller.showMessage(divide[0] + " " + divide[1] + " " + divide[2] + " " + divide[3] + " " + divide[4]);
                    if (divide[2].equals("HIT") && Integer.parseInt(divide[4]) == player.getId()) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Navigation.getInstance().loadScreen("gameOver");
                        ((GameOverController) Navigation.getInstance().getControllers().get("gameOver")).setWinnerLabelText("YOU LOOSE");
                    }
                    }
                });
                if (divide[2].equals("HIT")) {
                    deaths.add(Integer.parseInt(divide[4]));
                }
                for (int i : deaths) {
                    System.out.println("deaths");
                    System.out.println(i);
                }
                if (deaths.size() == GameService.NUMBER_OF_PLAYERS-1 && turn == player.getId()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Navigation.getInstance().loadScreen("gameOver");
                            ((GameOverController) Navigation.getInstance().getControllers().get("gameOver")).setWinnerLabelText("YOU WIN");
                        }
                    });
                }
                turn++;
                if (turn == GameService.NUMBER_OF_PLAYERS + 1) {
                    turn = 1;
                }
                System.out.println("turn" + turn);
                System.out.println(player.isDead());
                if (deaths.contains(turn) && turn < GameService.NUMBER_OF_PLAYERS) {
                    turn++;
                }
                if (deaths.contains(turn) && turn == GameService.NUMBER_OF_PLAYERS) {
                    turn = 1;
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        controller.showMessage("It's Player " + turn + " turn");
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getTurn() {
        return turn;
    }

    public String getMessage() {
        return message;
    }
}
