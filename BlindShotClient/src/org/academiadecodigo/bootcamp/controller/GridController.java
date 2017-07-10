package org.academiadecodigo.bootcamp.controller;

import javafx.animation.*;
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
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.academiadecodigo.bootcamp.Navigation;
import org.academiadecodigo.bootcamp.avatar.Avatar;
import org.academiadecodigo.bootcamp.service.GameCommunication;
import org.academiadecodigo.bootcamp.service.GameService;

import java.io.IOException;
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

    @FXML
    private GridPane grid;

    private ImageView playerImageView;

    private int turn = 1;

    private String winOrLose;

    private static Avatar avatar;

    private String floorPane = "-fx-background-color: transparent";

    private GameCommunication gameCommunication;
    private GameService gameService;
    private String move;

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
        String hitPane = "-fx-background-image: url('images/Hole.png');-fx-background-size: cover;-fx-background-position: center";
        element.setStyle(hitPane);
        try {
            gameService.sendMessage(move + " " + GridPane.getRowIndex(element) + " " + GridPane.getColumnIndex(element));
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
        myGridElements = grid.getChildren();
        gameService = Navigation.getInstance().getGameService(); // mudar isto; criar service registry
        gameCommunication = Navigation.getInstance().getGameCommunication();
        myGridElements = grid.getChildren();
        createGridElements();
        createPlayerObject();
        new Thread(gameCommunication).start();
    }

    public static void setAvatar(Avatar avatar) {
        GridController.avatar = avatar;
    }

    /**
     * Method that change the player Node Position, to cell that previous been clicked
     */
    private void onClickChangePlayerPosition(MouseEvent event) {
        if (gameCommunication.getTurn() == gameService.getPlayer().getId()) {
            Node element = event.getPickResult().getIntersectedNode();
            grid.getChildren().remove(playerImageView);
            grid.add(playerImageView, GridPane.getColumnIndex(element), GridPane.getRowIndex(element));
            move = GridPane.getRowIndex(element) + " " + GridPane.getColumnIndex(element);
            clearEnablePanes();
            attackMode = !attackMode;
        } else {
            showMessage("It's not your turn, MOTHERFUCKER!!!");
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
            gameService.sendMessage(Integer.toString(column) + " " + Integer.toString(row));
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
            String imageUrl= null;
        for (int i = 1; i <= pathLenght; i++) {

            Node upNodes = getNodeByRowColumnIndex(PlayerRow - i, PlayerColumn);
            imageUrl = "\"images/Avatar/"+avatar.getFolder()+"/up.png\"";
            highlightNodes(upNodes,imageUrl);

            Node downNodes = getNodeByRowColumnIndex(PlayerRow + i, PlayerColumn);
            imageUrl = "\"images/Avatar/"+avatar.getFolder()+"/down.png\"";
            highlightNodes(downNodes,imageUrl);

            Node rightNodes = getNodeByRowColumnIndex(PlayerRow, PlayerColumn + i);
            imageUrl = "\"images/Avatar/"+avatar.getFolder()+"/right.png\"";
            highlightNodes(rightNodes,imageUrl);

            Node leftNodes = getNodeByRowColumnIndex(PlayerRow, PlayerColumn - i);
            imageUrl = "\"images/Avatar/"+avatar.getFolder()+"/left.png\"";
            highlightNodes(leftNodes,imageUrl);
        }
    }

    private void highlightNodes(Node node,String imageUrl) {
        String nodeBg;

        if (attackMode) {
            nodeBg = "\"images/Avatar/bigOne/up.png\"";
        } else {
            nodeBg = imageUrl;
        }

        if (node != null && !node.getStyle().contains("Hole")) {
            node.setStyle("-fx-background-position:center;-fx-background-size:cover;-fx-background-image:url("+ nodeBg +"); -fx-opacity: 1;");
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
        BorderPane messagePane = new BorderPane(new Text(message));
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

    public void changeDirection(Node element) {

        int col = GridPane.getColumnIndex(playerImageView);
        int row = GridPane.getRowIndex(playerImageView);

        int futureCol = GridPane.getColumnIndex(element);
        int futureRow = GridPane.getRowIndex(element);

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

    public void drawAttack(int col, int row) {
        Node element = getNodeByRowColumnIndex(col, row);
        String hitPane = "-fx-background-image: url('images/Hole.png');-fx-background-size: cover;-fx-background-position: center";
        element.setStyle(hitPane);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(5000),
                ae -> clearAttacks()));
        timeline.play();
    }

    public void clearAttacks() {
        for (Node node : myGridElements) {
            if (node instanceof Pane) {
                node.setStyle(floorPane);
            }
        }
    }
}