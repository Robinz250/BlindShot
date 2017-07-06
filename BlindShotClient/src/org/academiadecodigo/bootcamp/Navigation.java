package org.academiadecodigo.bootcamp;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.academiadecodigo.bootcamp.service.Client;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by codecadet on 23/06/17.
 */
public final class Navigation {

    private static Navigation navigation = null;
    private Stage stage;
    public static final int WIDTH = 860;
    public static final int HEIGHT = 600;
    private Map<String, Initializable> controllers = new HashMap<>();
    private Client client;
    private LinkedList<Scene> scenes = new LinkedList<>();

    public static synchronized Navigation getInstance() {
        if (navigation == null) {
            navigation = new Navigation();
        }
        return navigation;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void loadScreen(String view) {

        try {
            FXMLLoader fxmlLoader;
            fxmlLoader = new FXMLLoader(getClass().getResource("view/"+ view +".fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.getController();
            controllers.put(view, fxmlLoader.<Initializable>getController());
            root.getStylesheets().add("/css/style.css");
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            scenes.push(scene);
            setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    public void back() {
        if (scenes.size() == 1) {
            return;
        }
        scenes.pop();
        setScene(scenes.peek());
    }

    public void close() {
        stage.close();
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Map<String, Initializable> getControllers() {
        return controllers;
    }

    public Client getClient() {
        return client;
    }
}