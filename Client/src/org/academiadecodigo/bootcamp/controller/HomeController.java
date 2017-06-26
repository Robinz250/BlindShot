package org.academiadecodigo.bootcamp.controller;

import javafx.fxml.FXML;
import javafx.scene.input.*;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

/**
 * Created by codecadet on 26/06/17.
 */
public class HomeController {



    @FXML
    private GridPane ClientGrid;

    @FXML
    private Circle circle;

    @FXML
    void DragCircle(MouseEvent event) {
        circle.setTranslateX(event.getSceneX() - circle.getRadius());
        circle.setTranslateY(event.getSceneY() - circle.getRadius());
        System.out.println("hey");
    }



}
