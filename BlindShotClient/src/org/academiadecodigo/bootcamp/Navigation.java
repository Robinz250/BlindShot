package org.academiadecodigo.bootcamp;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by codecadet on 23/06/17.
 */
public final class Navigation {

    private static Navigation navigation = null;
    private Stage stage;
    private Scene scene;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;
    private Map<String, Initializable> controllers = new HashMap<>();
    private Client client;

    private Navigation() {

    }

    public void connectToSocket() {

    }

    public static synchronized Navigation getInstance() {
        if(navigation == null) {
            navigation = new Navigation();
        }

        return navigation;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadScreen(String view) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("grid.fxml"));

            GridPane root = fxmlLoader.load();

            GridController gridController = fxmlLoader.getController();

            controllers.put(view, gridController);

            scene = new Scene(root, WIDTH, HEIGHT);
            setScene(scene);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Client getClient() {
        return client;
    }
}