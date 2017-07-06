package org.academiadecodigo.bootcamp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import org.academiadecodigo.bootcamp.Navigation;

public class InstructionsController {

    @FXML
    private Button backButton;

    @FXML
    void backToMenu(ActionEvent event) {
        Navigation.getInstance().loadScreen("startmenu.fxml");

    }

}
