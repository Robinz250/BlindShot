package org.academiadecodigo.bootcamp.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.academiadecodigo.bootcamp.Client;
import org.academiadecodigo.bootcamp.Navigation;

import java.net.URL;
import java.util.ResourceBundle;

public class GameOverController implements Initializable {

    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private Button quitButton;

    @FXML
    private Label winnerLabel;

    @FXML
    private Button restartButton;

    @FXML
    void quit(ActionEvent event) {
        Navigation.getInstance().close();
    }

    @FXML
    void restart(ActionEvent event) {
        Navigation.getInstance().back();
        Navigation.getInstance().back();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setWinnerLabelText(String labelText) {
        winnerLabel.setText(labelText);
    }
}
