package org.academiadecodigo.bootcamp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.academiadecodigo.bootcamp.Navigation;
import org.academiadecodigo.bootcamp.Waiting;
import org.academiadecodigo.bootcamp.model.Player;
import org.academiadecodigo.bootcamp.service.GameService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MenuController implements Initializable {

    private GameService gameService;

    @FXML
    private Button playButton;

    @FXML
    private TextField playerMessage;

    @FXML
    private Label serverMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameService = Navigation.getInstance().getGameService();
    }

    @FXML
    private void playerMessage() {
        if (playButton.getText().equals("Submit")) {
            try {
                gameService.sendMessage(playerMessage.getText());
                String id = gameService.receiveMessage();
                serverMessage.setText("Hello, " + playerMessage.getText() + "! You will be player " + id);
                Navigation.getInstance().getGameService().getPlayer().setId(Integer.parseInt(id));
                playButton.setText("Play");
                playerMessage.setVisible(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                gameService.sendMessage("I'm ready");
                gameService.receiveMessage();
                playButton.setVisible(false);
                serverMessage.setText("Waiting for other players to connect...");
                new Thread(new Waiting(Navigation.getInstance().getGameService().getPlayer().getClientSocket())).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void goToInstructions() {

    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }
}
