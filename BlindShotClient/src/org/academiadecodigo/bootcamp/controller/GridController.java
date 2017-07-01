package org.academiadecodigo.bootcamp.controller;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import org.academiadecodigo.bootcamp.Client;
import org.academiadecodigo.bootcamp.utils.Navigation;

import java.net.URL;
import java.util.*;

public class GridController implements Initializable {

    private ObservableList<Node> myGridElements;

    private boolean atackMode = true;

    @FXML
    private GridPane grid;

    @FXML
    private Circle circle;

    private Circle PlayerCircle;

    private Client client;

    private String move;

    private EventHandler playerAction = new EventHandler<MouseEvent>() {
        public void handle(MouseEvent event) {
            if (atackMode) {
                onClickAtack(event);
            } else {
                onClickchangePlayerPosition(event);
            }
        }
    };

    private void onClickAtack(MouseEvent event) {
        Node element = event.getPickResult().getIntersectedNode();
        element.setStyle("-fx-background-image: url('images/Hole.png');-fx-background-size: cover;-fx-background-position: center");
        clearEnablePanes();
        atackMode = !atackMode;
    }

    private void onClickchangePlayerPosition(MouseEvent event) {
        Node element = event.getPickResult().getIntersectedNode();
        int NodeRow = grid.getRowIndex(element).intValue();
        int NodeColumn = grid.getColumnIndex(element).intValue();
        grid.getChildren().remove(PlayerCircle);
        grid.add(PlayerCircle, NodeColumn, NodeRow);
        clearEnablePanes();
        atackMode = !atackMode;
    }

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


    private EventHandler hoverOutPlayer = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            if (atackMode) {
                clearAtakeHighlight();
            } else {
                clearHighlight();
            }

        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = Navigation.getInstance().getClient();
        grid.setAlignment(Pos.CENTER);

        this.myGridElements = grid.getChildren();

        createGridElements();
        createPlayerCircleAndPosition();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public boolean ifIsOutOfGrid(int number) {
        int columnSize = grid.getColumnConstraints().size() - 1;
        //int rowSize = grid.getRowConstraints().size() - 1;
        System.out.println(number + " " + columnSize);
        if (number <= columnSize) {
            return true;
        }
        return false;
    }

    public void move() {
        Navigation.getInstance().getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case DOWN:
                        if (GridPane.getRowIndex(PlayerCircle) != grid.getColumnConstraints().size() - 1) {

                            grid.getChildren().remove(PlayerCircle);
                            grid.add(PlayerCircle, GridPane.getColumnIndex(PlayerCircle), GridPane.getRowIndex(PlayerCircle) + 1);

                            move = "DOWN";
                            System.out.println(move);

                        } else {
                            System.out.println("Can't move that way!");
                        }
                        break;
                    case UP:
                        if (GridPane.getRowIndex(PlayerCircle) != 0) {

                            grid.getChildren().remove(PlayerCircle);
                            grid.add(PlayerCircle, GridPane.getColumnIndex(PlayerCircle), GridPane.getRowIndex(PlayerCircle) - 1);

                        } else {
                            System.out.println("Can't move that way!");
                        }
                        break;
                    case LEFT:
                        if (GridPane.getColumnIndex(PlayerCircle) != 0) {

                            grid.getChildren().remove(PlayerCircle);
                            grid.add(PlayerCircle, GridPane.getColumnIndex(PlayerCircle) - 1, GridPane.getRowIndex(PlayerCircle));

                        } else {
                            System.out.println("Can't move that way!");
                        }
                        break;
                    case RIGHT:
                        if (GridPane.getColumnIndex(PlayerCircle) != grid.getColumnConstraints().size() - 1) {

                            grid.getChildren().remove(PlayerCircle);
                            grid.add(PlayerCircle, GridPane.getColumnIndex(PlayerCircle) + 1, GridPane.getRowIndex(PlayerCircle));

                        } else {
                            System.out.println("Can't move that way!");
                        }
                        break;
                }
            }
        });

    }

    public void createPlayerCircleAndPosition() {
        PlayerCircle = new Circle();
        PlayerCircle.setRadius(10);
        PlayerCircle.setStyle("-fx-background-color: blue");
        PlayerCircle.setFill(Paint.valueOf("#CCE5FF"));
        PlayerCircle.setFocusTraversable(true);
        grid.add(PlayerCircle, (int) (Math.random() * grid.getColumnConstraints().size()), (int) (Math.random() * grid.getRowConstraints().size()));
        grid.setHalignment(PlayerCircle, HPos.CENTER);

        PlayerCircle.setOnMouseEntered(onHoverPlayer);

        PlayerCircle.setOnMouseExited(hoverOutPlayer);
    }

    public void clearHighlight() {
        for (Node node : myGridElements) {
            if (node instanceof Pane) {
                if (!node.getStyle().contains("Hole")) {
                    node.setStyle("-fx-background-color: black;-fx-border-color: white");
                }
            }
        }
    }

    public void clearAtakeHighlight() {
        for (Node node : myGridElements) {
            if (node instanceof Pane) {
                if (!node.getStyle().contains("Hole")) {
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

    private void changePlayerPosition(int row, int column) {

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

    public String getMove() {
        return move;
    }

    public void setMove(String move) {
        this.move = move;
    }

    public void movePlayer(String move) {
        System.out.println(move);
        grid.getChildren().remove(circle);
        grid.add(circle, GridPane.getColumnIndex(circle), GridPane.getRowIndex(circle) + 1);
    }
}