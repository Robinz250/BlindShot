package org.academiadecodigo.bootcamp.controller;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.academiadecodigo.bootcamp.service.Client;
import org.academiadecodigo.bootcamp.Navigation;
import org.academiadecodigo.bootcamp.avatar.Avatar;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.*;

public class GridController implements Initializable {

    //All nodes on the game - floor - messages and players.
    private ObservableList<Node> myGridElements;

    //All floor nodes
    private List<Node> gridPanes;

    //All panes that been attack image
    private List<Node> paneAttack = new ArrayList<>();

    //List of highlight panes
    private List<Node> paneHighLight = new ArrayList<>();

    //Node image of player
    private Image playerImage;

    //AttackMode Boolean true if is the Player turn to attack
    private boolean attackMode = false;

    private Client client;

    @FXML
    private GridPane grid;

    private ImageView playerImageView;

    private int turn = 1;

    private String winOrLose;

    private static Avatar avatar;

    private BorderPane messagePane = null;

    private String HitPane = "-fx-background-image: url('images/Hole.png');-fx-background-size: cover;-fx-background-position: center";
    private String floorPane = "-fx-background-color: transparent";


    /**
     * Mouse click on empty cell, the player will do action between Attack or Move.
     */
    private EventHandler playerAction = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (attackMode) {
                onClickAttack(event);
            } else {
                onClickChangePlayerPosition(event);
            }
        }
    };

    /**
     * Mouse hover on player element , change behaviour trow attackMode Bollean
     */

    private EventHandler onHoverPlayer = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (attackMode) {
                attack();
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

    private void onClickAttack(MouseEvent event) {
        Node element = event.getPickResult().getIntersectedNode();
        element.setStyle(HitPane);
        paneAttack.add(element);

        showMessage("Player " + client.getPlayer() + " | Attack | Row | " + grid.getRowIndex(element) + " | Column | " + grid.getColumnIndex(element));
        turn++;
        try {
            client.sendMessage("Player " + client.getPlayer() + " | Attack | Row | " + GridPane.getRowIndex(element) + " | Column | " + GridPane.getColumnIndex(element));
        } catch (IOException e) {
            e.printStackTrace();
        }
        clearEnablePanes();
        attackMode = !attackMode;
    }

    /**
     * First Method to be read - get the service for mesage handler
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = Navigation.getInstance().getClient();
        this.myGridElements = grid.getChildren();
        new Thread(new turnMessage()).start();
        createGridElements();
        createPlayerObject();
    }

    public static void setAvatar(Avatar avatar) {
        GridController.avatar = avatar;
    }

    /**
     * Method that change the player Node Position, to cell that previous been clicked
     */
    private void onClickChangePlayerPosition(MouseEvent event) {
        if (turn == client.getPlayer()) {
            Node element = event.getPickResult().getIntersectedNode();
            changeDirection(element);
            grid.getChildren().remove(playerImageView);
            grid.add(playerImageView, GridPane.getColumnIndex(element), GridPane.getRowIndex(element));
            showMessage("Player 1 | Move | Row | " + GridPane.getRowIndex(element) + " | Column | " + GridPane.getColumnIndex(element));
            try {
                client.sendMessage("Player " + client.getPlayer() + " | Move | Row | " + grid.getRowIndex(element) + " | Column | " + grid.getColumnIndex(element));
                System.out.println("send");
            } catch (IOException e) {
                e.printStackTrace();
            }
            clearEnablePanes();
            attackMode = !attackMode;
        } else {
            showMessage("It's not your turn, MOTHERFUCKER!!!");
        }

        if (turn == Client.numberOfPlayers + 1) {
            turn = 1;
        }
    }

    /**
     * Create jafafx object that represent the player
     */

    public void createPlayerObject() {
        playerImage = new Image("images/Avatar/" + avatar.getFolder() + "/down.png");
        playerImageView = new ImageView(playerImage);
        playerImageView.setFitHeight(grid.getRowConstraints().get(1).getPrefHeight() + 40);
        playerImageView.setFitWidth(grid.getRowConstraints().get(1).getPrefHeight() + 40);
        System.out.println(grid.getRowConstraints().get(1).getPrefHeight());
        playerImageView.setId("Player");

        int column = (int) (Math.random() * grid.getColumnConstraints().size());
        int row = (int) (Math.random() * grid.getRowConstraints().size());
        grid.add(playerImageView, column, row);

        try {
            client.sendMessage("Player " + client.getPlayer() + " column: " + Integer.toString(column) + " row: " + Integer.toString(row));
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridPane.setHalignment(playerImageView, HPos.CENTER);
        playerImageView.setOnMouseEntered(onHoverPlayer);
        playerImageView.setOnMouseExited(hoverOutPlayer);
    }

    /**
     * Clear grid cells that been painted to highlight the path
     */
    public void clearHighlight() {
        for (Node node : paneHighLight) {
            node.setStyle(floorPane);
        }
        paneHighLight.clear();
    }

    /**
     * Every pane/ cell is disable , this method put all the enable pane's disabled
     */
    public void clearEnablePanes() {
        for (Node node : gridPanes) {
            node.setDisable(true);
        }
    }

    /**
     * Create jafaFx Pane in each cell of the Grid
     */
    private void createGridElements() {
        gridPanes = new ArrayList<>();
        // Get number of Rows and
        int gridColumns = grid.getColumnConstraints().size();
        int gridRows = grid.getRowConstraints().size();

        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                GridPane pane = new GridPane();
                pane.setStyle(floorPane);
                pane.setId("floorPane");
                pane.setDisable(true);
                grid.add(pane, i, j);
                //Add pane to gridPanes List
                gridPanes.add(pane);
                pane.setOnMouseClicked(playerAction);
            }
        }
    }

    /**
     * Get player coordenades on hashMap by string "row" or Column
     */
    private Map<String, Integer> getMyPlayerCoordenates() {
        Map<String, Integer> playerPosition = new HashMap<>();
        playerPosition.put("Column", GridPane.getColumnIndex(playerImageView));
        playerPosition.put("Row", GridPane.getRowIndex(playerImageView));
        return playerPosition;
    }

    /**
     * Create the highlight to see were the player should go. Move mode
     */
    private void seeWereCanPlayerGo() {
        int PlayerWalker = avatar.getMoveRange();
        int Prow = getMyPlayerCoordenates().get("Row");
        int Pcolumn = getMyPlayerCoordenates().get("Column");
        highLightPath(PlayerWalker, Prow, Pcolumn);
    }

    /**
     * Method both use by seeWereCanPlayerGo() and attack() to higligh the path
     * receive the number os cells length, the player row and player column and the color to paint panes
     */
    private void highLightPath(int pathLenght, int PlayerRow, int PlayerColumn) {

        for (int i = 1; i <= pathLenght; i++) {


            Node upNodes = getNodeByRowColumnIndex(PlayerRow + i, PlayerColumn);
            highlightNodes(upNodes);


            Node downNodes = getNodeByRowColumnIndex(PlayerRow - i, PlayerColumn);
            highlightNodes(downNodes);

            Node rightNodes = getNodeByRowColumnIndex(PlayerRow, PlayerColumn + i);
            highlightNodes(rightNodes);

            Node leftNodes = getNodeByRowColumnIndex(PlayerRow, PlayerColumn - i);
            highlightNodes(leftNodes);
        }
    }

    private void highlightNodes(Node node) {

        String color = "images/Avatar/bigOne/up.png";
        if (attackMode) {
            //color = "Orange";
        } else {
            //color = "red";
        }

        if (node != null && !node.getStyle().contains("Hole")) {
            System.out.println(color);
            System.out.println("enter");
            node.setStyle("-fx-background-image:url("+ color +"); -fx-opacity: 1");
            //node.setStyle("-fx-background-color : red");
            node.setDisable(false);
            paneHighLight.add(node);
            //fadeOut(node);
        }

    }

    private void fadeOut(Node node) {
        FadeTransition nodeFade = new FadeTransition(Duration.millis(3000), node);
        nodeFade.setFromValue(0);
        nodeFade.setToValue(1);
        nodeFade.play();
    }

    private void attack() {
        int AtakLenght = avatar.getKillRange();
        int PlayerRow = getMyPlayerCoordenates().get("Row");
        int PlayerColumn = getMyPlayerCoordenates().get("Column");
        highLightPath(AtakLenght, PlayerRow, PlayerColumn);
    }

    public Node getNodeByRowColumnIndex(int row, int column) {
        Node result = null;
        for (Node node : gridPanes) {
            if (GridPane.getRowIndex(node) != null || GridPane.getColumnIndex(node) != null) {
                if ((row <= grid.getRowConstraints().size() && row >= 0) || column <= grid.getColumnConstraints().size() && column >= 0) {
                    if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                        result = node;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /**
     * show message on top left corner
     */

    public void showMessage(String message) {
        messagePane = new BorderPane(new Text(message));
        messagePane.setPadding(new Insets(15));
        messagePane.setId("ModalMessage");
        grid.add(messagePane, 1, 0);

        messagePane.setStyle("-fx-background-color: lawngreen;-fx-opacity: 0.8");
        messagePane.setTranslateY(messagePane.getLayoutY() - 100);
        PauseTransition animationPause = new PauseTransition(Duration.millis(1000));
        TranslateTransition appearAnimation = new TranslateTransition(Duration.millis(1000), messagePane);
        appearAnimation.setByY(150);
        TranslateTransition dissapearAnimation = new TranslateTransition(Duration.millis(1000), messagePane);
        dissapearAnimation.setByY(-150);

        SequentialTransition seqTransition = new SequentialTransition(appearAnimation, animationPause, dissapearAnimation);
        seqTransition.play();
    }

    private class turnMessage implements Runnable {

        @Override
        public void run() {
            String message;
            while (true) {
                try {
                    BufferedReader in = new BufferedReader(new InputStreamReader(client.getClientSocket().getInputStream()));
                    message = in.readLine();
                    System.out.println(message);
                    String[] divide;
                    divide = message.split(" \\| ");
                    turn = Integer.parseInt(divide[0]);
                    winOrLose = divide[7];

                    Node element = getNodeByRowColumnIndex(Integer.parseInt(divide[4]), Integer.parseInt(divide[6]));
                    element.setStyle(HitPane);
                    paneAttack.add(element);

                    Timeline timeline = new Timeline(new KeyFrame(
                            Duration.millis(5000),
                            ae -> clearAttacks()));
                    timeline.play();

                    winOrLose();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void winOrLose() {
            if (winOrLose.equals("YOU LOOSE")) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Navigation.getInstance().loadScreen("gameOver");
                        GameOverController gameOverController = (GameOverController) Navigation.getInstance().getControllers().get("gameOver");
                        gameOverController.setWinnerLabelText("YOU LOOSE");
                    }
                });

            } else if (winOrLose.equals("YOU WIN")) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        Navigation.getInstance().loadScreen("gameOver");
                        GameOverController gameOverController = (GameOverController) Navigation.getInstance().getControllers().get("gameOver");
                        gameOverController.setWinnerLabelText("YOU WIN");
                    }
                });
            }
        }

        public void clearAttacks() {
            System.out.println("entrei");
            for (Node node : paneAttack) {
                node.setStyle(floorPane);
            }

            paneAttack.clear();
        }
    }

    public void changeDirection(Node element) {

        int col = grid.getColumnIndex(playerImageView);
        int row = grid.getRowIndex(playerImageView);

        int futureCol = grid.getColumnIndex(element);
        int futureRow = grid.getRowIndex(element);

        System.out.println(col + " " + row + " " + futureCol + " " + futureRow);

        if (col < futureCol && row == futureRow) {
            System.out.println("enter");
            playerImage = new Image("images/Avatar/" + avatar.getFolder() + "/right.png");
            playerImageView.setImage(playerImage);
        }
        if (col > futureCol && row == futureRow) {
            System.out.println("enter");
            playerImage = new Image("images/Avatar/" + avatar.getFolder() + "/left.png");
            playerImageView.setImage(playerImage);
        }
        if (row > futureRow && col == futureCol) {
            System.out.println("enter");
            playerImage = new Image("images/Avatar/" + avatar.getFolder() + "/up.png");
            playerImageView.setImage(playerImage);
        }
        if (row < futureRow && col == futureCol) {
            System.out.println("enter");
            playerImage = new Image("images/Avatar/" + avatar.getFolder() + "/down.png");
            playerImageView.setImage(playerImage);
        }
    }
}