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
import org.academiadecodigo.bootcamp.Main;
import org.academiadecodigo.bootcamp.utils.Navigation;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class gridController implements Initializable {

    @FXML
    private GridPane grid;

    @FXML
    private Circle circle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GridPane.setColumnIndex(circle,1);
        GridPane.setRowIndex(circle, 0);
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

}