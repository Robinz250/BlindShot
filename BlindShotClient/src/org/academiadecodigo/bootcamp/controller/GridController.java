package org.academiadecodigo.bootcamp.controller;

import javafx.animation.*;
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
import org.academiadecodigo.bootcamp.Navigation;
import org.academiadecodigo.bootcamp.service.GameCommunication;
import org.academiadecodigo.bootcamp.service.GameService;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Created by ruimorais on 08/07/17.
 */
public class GridController implements Initializable {

    private GameService gameService;
    private ObservableList<Node> myGridElements;
    private GameCommunication gameCommunication;
    private String move;
    private String floorPane = "-fx-background-color: transparent";

    @FXML
    private GridPane grid;

    @FXML
    private Circle playerCircle;

    private boolean attackMode = false;

    private String HitPane = "-fx-background-image: url('images/Hole.png');-fx-background-size: cover;-fx-background-position: center";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameService = Navigation.getInstance().getGameService(); // mudar isto; criar service registry
        gameCommunication = Navigation.getInstance().getGameCommunication();
        myGridElements = grid.getChildren();
        createGridElements();
        createPlayerObject();
        new Thread(gameCommunication).start();
    }

    /**
     * Create jafaFx Pane in each cell of the Grid
     */
    private void createGridElements() {
        // Get number of Rows and
        int gridColumns = grid.getColumnConstraints().size();
        int gridRows = grid.getRowConstraints().size();

        for (int i = 0; i < gridColumns; i++) {
            for (int j = 0; j < gridRows; j++) {
                GridPane pane = new GridPane();
                String floorPane = "-fx-background-color: transparent";
                pane.setStyle(floorPane);
                pane.setId("FloorPane");
                pane.setDisable(true);
                grid.add(pane, i, j);
                pane.setOnMouseClicked(playerAction);
            }
        }
    }

    public void createPlayerObject() {
        playerCircle = new Circle();
        playerCircle.setRadius(10);
        playerCircle.setStyle("-fx-background-color: blue");
        playerCircle.setFill(Paint.valueOf("#CCE5FF"));
        playerCircle.setId("Player");
        playerCircle.setFocusTraversable(true);

        int column = (int) (Math.random() * grid.getColumnConstraints().size());
        int row = (int) (Math.random() * grid.getRowConstraints().size());
        grid.add(playerCircle, column, row);

        try {
            gameService.sendMessage(Integer.toString(column) + " " + Integer.toString(row));
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridPane.setHalignment(playerCircle, HPos.CENTER);
        playerCircle.setOnMouseEntered(onHoverPlayer);
        playerCircle.setOnMouseExited(hoverOutPlayer);
    }

    /**
     * Method that change the player Node Position, to cell that previous been clicked
     */
    private void onClickchangePlayerPosition(MouseEvent event) {
        if (gameCommunication.getTurn() == gameService.getPlayer().getId()) {
            Node element = event.getPickResult().getIntersectedNode();
            grid.getChildren().remove(playerCircle);
            grid.add(playerCircle, GridPane.getColumnIndex(element), GridPane.getRowIndex(element));
            move = GridPane.getRowIndex(element) + " " + GridPane.getColumnIndex(element);
            clearEnablePanes();
            attackMode = !attackMode;
        } else {
            showMessage("It's not your turn, MOTHERFUCKER!!!");
        }
    }

    public void showMessage(String message) {
        BorderPane rec = new BorderPane(new Text(message));
        rec.setPadding(new Insets(15));
        rec.setId("ModalMessage");
        grid.add(rec, 1, 0);

        rec.setStyle("-fx-background-color: lawngreen;-fx-opacity: 0.8");
        rec.setTranslateY(rec.getLayoutY() - 100);
        PauseTransition pt = new PauseTransition(Duration.millis(1000));
        TranslateTransition aa = new TranslateTransition(Duration.millis(1000), rec);
        aa.setByY(150);
        TranslateTransition bb = new TranslateTransition(Duration.millis(1000), rec);
        bb.setByY(-150);

        SequentialTransition seqTransition = new SequentialTransition(aa, pt, bb);
        seqTransition.play();
    }

    /**
     * Every pane/ cell is disable , this method put all the enable pane's disabled
     */
    public void clearEnablePanes() {
        for (Node node : myGridElements) {
            if (node instanceof Pane) {
                node.setDisable(true);
            }
        }
    }

    /**
     * Mouse click on empty cell, the player will do action between Attack or Move.
     */
    private EventHandler playerAction = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (attackMode) {
                onClickAttack(event);
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


    private void seeWereCanPlayerGo() {
        int PlayerWalker = 5;
        int Prow = getMyPlayerCoordenates().get("Row");
        int Pcolumn = getMyPlayerCoordenates().get("Column");
        highLightPath(PlayerWalker, Prow, Pcolumn, "red");
    }

    /**
     * Get player coordenades on hashMap
     */
    private Map<String, Integer> getMyPlayerCoordenates() {
        for (Node s : myGridElements) {
            if (s instanceof Circle) {
                Map<String, Integer> playerPosition = new HashMap<>();
                playerPosition.put("Column", GridPane.getColumnIndex(s));
                playerPosition.put("Row", GridPane.getRowIndex(s));
                return playerPosition;
            }
        }
        return null;
    }

    /**
     * Method both use by seeWereCanPlayerGo() and attack() to higligh the path
     * receive the number os cells length, the player row and player column and the color to paint panes
     */
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

    public Node getNodeByRowColumnIndex(int row, int column) {
        Node result = null;
        for (Node node : myGridElements) {
            if ((row <= grid.getRowConstraints().size() && row >= 0) || (column <= grid.getColumnConstraints().size() && column >= 0)) {
                if (GridPane.getRowIndex(node) != null || GridPane.getColumnIndex(node) != null) {
                    if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                        result = node;
                        break;
                    }
                }
            }
        }
        return result;
    }

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

    public void clearHighlight() {
        for (Node node : myGridElements) {
            if (node instanceof Pane) {
                if (node.getId().contains("FloorPane")) {
                    node.setStyle(floorPane);
                }
            }
        }
    }

    private void attack() {
        int AtakLenght = 2;
        int PlayerRow = getMyPlayerCoordenates().get("Row");
        int PlayerColumn = getMyPlayerCoordenates().get("Column");
        highLightPath(AtakLenght, PlayerRow, PlayerColumn, "orange");
    }

    public void clearAttacks() {
        for (Node node : myGridElements) {
            if (node instanceof Pane) {
                node.setStyle(floorPane);
            }
        }
    }

    public void drawAttack(int col, int row) {
        Node element = getNodeByRowColumnIndex(col, row);
        element.setStyle(HitPane);

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.millis(5000),
                ae -> clearAttacks()));
        timeline.play();
    }
}
