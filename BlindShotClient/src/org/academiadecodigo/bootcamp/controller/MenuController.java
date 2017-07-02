package org.academiadecodigo.bootcamp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.academiadecodigo.bootcamp.Service.MessageService;
import org.academiadecodigo.bootcamp.Service.ServiceRegistry;
import org.academiadecodigo.bootcamp.utils.Navigation;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by codecadet on 02/07/17.
 */
public class MenuController implements Initializable {

    MessageService messageService = (MessageService) ServiceRegistry.getInstance().getService("Mensage");

    @FXML
    private Button Gobtn;

    @FXML
    private TextField nameInput;

    @FXML
    void sendName(ActionEvent event) {
        try {
            messageService.setPlayerName(nameInput.getText());
            messageService.sendMessage(nameInput.getText());
            Navigation.getInstance().loadScreen("grid");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
