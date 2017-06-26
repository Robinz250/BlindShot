package org.academiadecodigo.bootcamp.utils;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created by codecadet on 23/06/17.
 */
public class Navigation {

    private final int MIN_WIDTH = 1024;
    private final int MIN_HEIGHT = 768;
    private final String VIEW_PATH = "../view";

    private LinkedList<Scene> scenes = new LinkedList<Scene>();
    private Map<String, Initializable> controllers = new HashMap<>();

    private Stage stage;

    private static Navigation instance = null;

    private Navigation(){

    }

    public void loadScreen(String view){
        try {
            FXMLLoader fxmlLoader;
            fxmlLoader = new FXMLLoader(getClass().getResource(VIEW_PATH + "/" + view + ".fxml"));
            Parent root = fxmlLoader.load();

            controllers.put(view, fxmlLoader.<Initializable>getController());

            root.getStylesheets().add("css/client.css");

            Scene scene = new Scene(root,MIN_WIDTH,MIN_HEIGHT);
            scenes.push(scene);

            setScene(scene);

        }catch (IOException e){
            System.out.println("Failure to load view " + view + " : " + e.getMessage());
        }
    }

    public static synchronized Navigation getInstance(){

        if(instance == null){
            instance = new Navigation();
        }
        return instance;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void back() {

        if (scenes.isEmpty()) {
            return;
        }

        // remove the current scene from the stack
        scenes.pop();

        // load the scene at the top of the stack
        setScene(scenes.peek());
    }

    private void setScene(Scene scene) {

        // set the scene

        stage.setScene(scene);

        // show the stage to reload
        stage.show();
    }

    public Initializable getController(String view) {
        return controllers.get(view);
    }
}
