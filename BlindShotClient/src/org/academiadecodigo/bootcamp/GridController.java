package org.academiadecodigo.bootcamp;

import javafx.fxml.Initializable;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by ruimorais on 02/07/17.
 */
public class GridController implements Initializable {

    private Client client;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = Navigation.getInstance().getClient();
    }

    public void move() {
        System.out.println("c");
        try {
            client.sendMessage("oie");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
