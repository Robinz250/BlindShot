package org.academiadecodigo.bootcamp.controller;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.academiadecodigo.bootcamp.Client;
import org.academiadecodigo.bootcamp.Navigation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GridController implements Initializable {

    //All our node in all cells, are in here
    private ObservableList<Node> myGridElements;

    //AtackMode Boolean true if is the Player turn to attack
    private boolean atackMode = false;

    private Client client;

    @FXML
    private GridPane grid;

    @FXML
    private Circle PlayerCircle;

    private int turn = 1;

    /**
     * Mouse click on empty cell, the player will do action between Attack or Move.
     */
    private EventHandler playerAction = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (atackMode) {
                onClickAtack(event);
            } else {
                onClickchangePlayerPosition(event);
            }
        }
    };

    /**
     * Mouse hover on player element , change behaviour trow attackMode Bollean
     */

    private EventHandler onHoverPlayer = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (atackMode) {
                atack();
            } else {
                seeWereCanPlayerGo();
            }


        }
    };

    /**
     * Mouse hover out player element , it reverse the changes of Mouse Hover.
     */
    private EventHandler hoverOutPlayer = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            clearHighlight();
        }
    };

    /**
     * Method that change the background image of the Node selected, after been clicked
     */

    private void onClickAtack(MouseEvent event) {
        System.out.println("ffff");
        Node element = event.getPickResult().getIntersectedNode();
        element.setStyle("-fx-background-image: url('images/Hole.png');-fx-background-size: cover;-fx-background-position: center");
        showMessage("Player " + client.getPlayer() + " | Attack | Row | " + grid.getRowIndex(element) + " | Column | " + grid.getColumnIndex(element));
        turn++;
        System.out.println(turn);
        try {
            client.sendMessage("Player " + client.getPlayer() + " | Attack | Row | " + grid.getRowIndex(element) + " | Column | " + grid.getColumnIndex(element));
        } catch (IOException e) {
            e.printStackTrace();
        }
        clearEnablePanes();
        atackMode = !atackMode;
    }

    /**
     * Method that change the player Node Position, to cell that previous been clicked
     */
    private void onClickchangePlayerPosition(MouseEvent event) {
        System.out.println(turn);

        if (turn == client.getPlayer()) {
            Node element = event.getPickResult().getIntersectedNode();
            grid.getChildren().remove(PlayerCircle);
            grid.add(PlayerCircle, grid.getColumnIndex(element).intValue(), grid.getRowIndex(element).intValue());
            showMessage("Player 1 | Move | Row | " + grid.getRowIndex(element) + " | Column | " + grid.getColumnIndex(element));

            try {
                client.sendMessage("Player " + client.getPlayer() + " | Move | Row | " + grid.getRowIndex(element) + " | Column | " + grid.getColumnIndex(element));
                System.out.println("send");
            } catch (IOException e) {
                e.printStackTrace();
            }


            clearEnablePanes();
            atackMode = !atackMode;
        }
        else {
            showMessage("It's not your turn, MOTHERFUCKER!!!");
        }

        if (turn == Client.numberOfPlayers + 1) {
            turn = 1;
        }
    }


    /**
     * First Method to be read - get
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //this.messageService = (MessageService) ServiceRegistry.getInstance().getService("Mensage");
        client = Navigation.getInstance().getClient();
        this.myGridElements = grid.getChildren();
        System.out.println("grid loaded");
        new Thread(new turnMessage()).start();

        createGridElements();
        createPlayerCircleAndPosition();

        //showMessage("Welcome : " + messageService.getPlayerName());
    }

    public void createPlayerCircleAndPosition() {
        PlayerCircle = new Circle();
        PlayerCircle.setRadius(10);
        PlayerCircle.setStyle("-fx-background-color: blue");
        PlayerCircle.setFill(Paint.valueOf("#CCE5FF"));
        PlayerCircle.setFocusTraversable(true);
        int column = (int) (Math.random() * grid.getColumnConstraints().size());
        int row = (int) (Math.random() * grid.getRowConstraints().size());
        grid.add(PlayerCircle, column, row);
        try {
            client.sendMessage("Player " + client.getPlayer() + " column: " + Integer.toString(column) + " row: " + Integer.toString(row));
        } catch (IOException e) {
            e.printStackTrace();
        }
        grid.setHalignment(PlayerCircle, HPos.CENTER);

        PlayerCircle.setOnMouseEntered(onHoverPlayer);

        PlayerCircle.setOnMouseExited(hoverOutPlayer);
    }

    public void clearHighlight() {
        for (Node node : myGridElements) {
            if (node instanceof Pane) {
                if (!node.getStyle().contains("Hole") && !node.getStyle().contains("lawngreen")) {
                    node.setStyle("-fx-background-color: black;-fx-border-color: white");
                }
            }
        }
    }

    public void clearEnablePanes() {
        for (Node node : myGridElements) {
            if (node instanceof Pane) {
                node.setDisable(true);
            }
        }
    }

    private void createGridElements() {
        // Get number of Rows and
        int gridColumns = grid.getColumnConstraints().size();
        int gridRows = grid.getRowConstraints().size();

        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                GridPane pane = new GridPane();
                pane.setStyle("-fx-background-color: black;-fx-border-color: white");
                pane.setDisable(true);
                grid.add(pane, i, j);
                pane.setOnMouseClicked(playerAction);

            }
        }
    }

    private Map<String, Integer> getMyPlayerCoordenates() {
        for (Node s : myGridElements) {
            if (s instanceof Circle) {
                Map<String, Integer> playerPosition = new HashMap<>();
                playerPosition.put("Column", grid.getColumnIndex(s));
                playerPosition.put("Row", grid.getRowIndex(s));
                return playerPosition;
            }
        }
        return null;
    }

    private void seeWereCanPlayerGo() {
        int PlayerWalker = 2;
        int Prow = getMyPlayerCoordenates().get("Row").intValue();
        int Pcolumn = getMyPlayerCoordenates().get("Column").intValue();

        highLightPath(PlayerWalker, Prow, Pcolumn, "red");
    }

    private void highLightPath(int pathLenght, int PlayerRow, int PlayerColumn, String color) {
        for (int i = 1; i <= pathLenght; i++) {
            if (getNodeByRowColumnIndex(PlayerRow + i, PlayerColumn) != null) {
                if (!getNodeByRowColumnIndex(PlayerRow + i, PlayerColumn).getStyle().contains("Hole")) {
                    getNodeByRowColumnIndex(PlayerRow + i, PlayerColumn).setStyle("-fx-background-color:" + color);
                }
                getNodeByRowColumnIndex(PlayerRow + i, PlayerColumn).setDisable(false);
            }
            if (getNodeByRowColumnIndex(PlayerRow - i, PlayerColumn) != null) {
                if (!getNodeByRowColumnIndex(PlayerRow - i, PlayerColumn).getStyle().contains("Hole.png")) {
                    getNodeByRowColumnIndex(PlayerRow - i, PlayerColumn).setStyle("-fx-background-color:" + color);
                }
                getNodeByRowColumnIndex(PlayerRow - i, PlayerColumn).setDisable(false);
            }
            if (getNodeByRowColumnIndex(PlayerRow, PlayerColumn + i) != null) {
                if (!getNodeByRowColumnIndex(PlayerRow, PlayerColumn + i).getStyle().contains("Hole.png")) {
                    getNodeByRowColumnIndex(PlayerRow, PlayerColumn + i).setStyle("-fx-background-color:" + color);
                }
                getNodeByRowColumnIndex(PlayerRow, PlayerColumn + i).setDisable(false);
            }
            if (getNodeByRowColumnIndex(PlayerRow, PlayerColumn - i) != null) {
                if (!getNodeByRowColumnIndex(PlayerRow, PlayerColumn - i).getStyle().contains("Hole.png")) {
                    getNodeByRowColumnIndex(PlayerRow, PlayerColumn - i).setStyle("-fx-background-color:" + color);
                }
                getNodeByRowColumnIndex(PlayerRow, PlayerColumn - i).setDisable(false);
            }
        }
    }

    private void atack() {
        int AtakLenght = 5;
        int PlayerRow = getMyPlayerCoordenates().get("Row").intValue();
        int PlayerColumn = getMyPlayerCoordenates().get("Column").intValue();

        highLightPath(AtakLenght, PlayerRow, PlayerColumn, "orange");
    }

    public Node getNodeByRowColumnIndex(final int row, final int column) {
        Node result = null;
        for (Node node : myGridElements) {
            if ((row <= grid.getRowConstraints().size() && row >= 0) || (column <= grid.getColumnConstraints().size() && column >= 0)) {
                if (grid.getRowIndex(node) != null || grid.getColumnIndex(node) != null) {
                    if (grid.getRowIndex(node) == row && grid.getColumnIndex(node) == column) {
                        result = node;
                        break;
                    }
                }
            }
        }

        return result;
    }

    public void showMessage(String message) {
        BorderPane rec = new BorderPane(new Text(message));
        rec.setPadding(new Insets(15));
        rec.setId("ModalMessage");
        grid.add(rec, 1, 0);

        rec.setStyle("-fx-background-color: lawngreen;-fx-opacity: 0.8");
        rec.setTranslateY(rec.getLayoutY() - 100);

        TranslateTransition tt = new TranslateTransition(Duration.millis(1000), rec);
        tt.setByY(150);
        tt.play();

        PauseTransition wait = new PauseTransition(Duration.seconds(2));
        wait.setOnFinished((e) -> {
        /*YOUR METHOD*/
            tt.setByY(-150);
            tt.play();
            wait.playFromStart();
        });
        wait.play();
    }

    private class turnMessage implements Runnable {

        @Override
        public void run() {
            System.out.println("thread turnMessage");
            String message;
            int number = 1;
            while (true) {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getClientSocket().getInputStream()));
                    message = in.readLine();
                    String[] divide;
                    divide = message.split(" \\| ");
                    if (number == 1) {
                        turn = Integer.parseInt(divide[0]);
                    }
                    number++;

                    for (String s : divide) {
                        System.out.println(s);
                    }
                    System.out.println(Integer.parseInt(divide[4]));
                    Node element = getNodeByRowColumnIndex(Integer.parseInt(divide[4]),Integer.parseInt(divide[6]));
                    element.setStyle("-fx-background-image: url('images/Hole.png');-fx-background-size: cover;-fx-background-position: center");
                    System.out.println(message);
                    System.out.println("turn: " + turn);
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                clearAttacks();
            }
        }

        public void clearAttacks() {
            for (Node node : myGridElements) {
                if (node instanceof Pane) {
                    node.setStyle("-fx-background-color: black;-fx-border-color: white");
                }
            }
        }
    }
}