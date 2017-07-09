package org.academiadecodigo.bootcamp;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.academiadecodigo.bootcamp.service.GameCommunication;
import org.academiadecodigo.bootcamp.service.GameService;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by codecadet on 23/06/17.
 */
public final class Navigation {

    private static Navigation navigation = null;
    private Stage stage;
    private GameService gameService;
    private GameCommunication gameCommunication;
    private Map<String, Initializable> controllers;

    private Navigation(){
        controllers = new HashMap<>();
    }

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
        FXMLLoader fxmlLoader;
        fxmlLoader = new FXMLLoader(getClass().getResource(view + ".fxml"));
        try {
            Parent root = fxmlLoader.load();
            root.getStylesheets().add("/css/style.css");
            controllers.put(view, fxmlLoader.getController());
            Scene scene = new Scene(root, 860, 600);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GameService getGameService() {
        return gameService;
    }

    public void setGameService(GameService gameService) {
        this.gameService = gameService;
    }

    public GameCommunication getGameCommunication() {
        return gameCommunication;
    }

    public void setGameCommunication(GameCommunication gameCommunication) {
        this.gameCommunication = gameCommunication;
    }

    public Map<String, Initializable> getControllers() {
        return controllers;
    }
}
