package org.academiadecodigo.bootcamp.controller;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import org.academiadecodigo.bootcamp.Client;
import org.academiadecodigo.bootcamp.Main;
import org.academiadecodigo.bootcamp.utils.Navigation;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class GridController implements Initializable {

    @FXML
    private GridPane grid;

    @FXML
    private Circle circle;

    private Client client;

    private String move;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GridPane.setColumnIndex(circle,1);
        GridPane.setRowIndex(circle, 0);

        client = Navigation.getInstance().getClient();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public void move() {

        Navigation.getInstance().getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                switch (event.getCode()) {
                    case DOWN:
                        if (GridPane.getRowIndex(circle) != grid.getColumnConstraints().size()-1) {

                            grid.getChildren().remove(circle);
                            grid.add(circle, GridPane.getColumnIndex(circle), GridPane.getRowIndex(circle) + 1);

                            move = "DOWN";
                            System.out.println(move);

                        }
                        else {
                            System.out.println("Can't move that way!");
                        }
                        break;
                    case UP:
                        if (GridPane.getRowIndex(circle) != 0) {

                            grid.getChildren().remove(circle);
                            grid.add(circle, GridPane.getColumnIndex(circle), GridPane.getRowIndex(circle) - 1);

                        }
                        else {
                            System.out.println("Can't move that way!");
                        }
                        break;
                    case LEFT:
                        if (GridPane.getColumnIndex(circle) != 0) {

                            grid.getChildren().remove(circle);
                            grid.add(circle, GridPane.getColumnIndex(circle) - 1, GridPane.getRowIndex(circle));

                        }
                        else {
                            System.out.println("Can't move that way!");
                        }
                        break;
                    case RIGHT:
                        if (GridPane.getColumnIndex(circle) != grid.getColumnConstraints().size()-1) {

                            grid.getChildren().remove(circle);
                            grid.add(circle, GridPane.getColumnIndex(circle) + 1, GridPane.getRowIndex(circle));

                        }
                        else {
                            System.out.println("Can't move that way!");
                        }
                        break;
                }
            }
        });
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