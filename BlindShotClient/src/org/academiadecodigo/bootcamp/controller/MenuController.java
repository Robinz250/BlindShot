package org.academiadecodigo.bootcamp.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import org.academiadecodigo.bootcamp.Service.MessageService;
import org.academiadecodigo.bootcamp.Service.ServiceRegistry;

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
    void sendName(ActionEvent event) {
        System.out.println("cenas");
        try {
            messageService.sendMessage("Hey");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
