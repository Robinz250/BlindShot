package org.academiadecodigo.bootcamp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.academiadecodigo.bootcamp.Client;
import org.academiadecodigo.bootcamp.Navigation;
import org.academiadecodigo.bootcamp.Waiting;

import javax.print.attribute.standard.MediaSize;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by codecadet on 02/07/17.
 */
public class MenuController implements Initializable {

    private Client client;

    @FXML
    private Button playButton;

    @FXML
    private TextField playerMessage;

    @FXML
    private Label serverMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = Navigation.getInstance().getClient();
        playButton.setText("Submit");
    }

    @FXML
    public void playerMessage() throws IOException {

        if (playButton.getText().equals("Submit")) {
            try {
                client.sendMessage(playerMessage.getText());
                serverMessage.setText("Hello, " + playerMessage.getText() + "! You will be player " + client.getPlayer());
                playButton.setText("Play");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            client.sendMessage("fff");
            client.receiveMessage();
            playerMessage.setVisible(false);
            playButton.setVisible(false);
            serverMessage.setText("Waiting for other players to connect...");
            //client.waitForServerMessage();
            new Thread(new Waiting(client.getClientSocket())).start();
        }
    }
}
