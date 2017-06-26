package org.academiadecodigo.bootcamp.view;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by codecadet on 24/06/17.
 */
public final class ViewLoader {

    //Create and instanciate the instance of this class,
    // the list of scenes and the container of controllers
    private static ViewLoader instance = new ViewLoader();
    private LinkedList<Scene> scenes = new LinkedList<>();
    private HashMap<String, Initializable> controllers = new HashMap<>();

    //Reference to the application window
    private Stage stage;
    private Scene scene;

    private ViewLoader() {
    }

    //Always return the same instance
    public static ViewLoader getInstance() {
        return instance;
    }

    public void loadScreen(String view) {
        try {
            //Instanciate the view and the controller
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(view));
            Parent root = fxmlLoader.load();

            //Store the controllers
            controllers.put(view, fxmlLoader.<Initializable>getController());

            //Create a new Scene and add it to the stack
            Scene scene = new Scene(root);
            scenes.push(scene);

            //Put the scene on the stage
            setScene(scene);
        } catch (IOException e) {
            System.out.println("Failed to load view " + view + "\n" + e.getMessage());
        }
    }

    public void setScene(Scene scene) {
        stage.setScene(scene);
        stage.show();
    }

    public void back() {
        //If there are no more scenes to go back, return and do nothing
        if (scenes.size() <= 1) {
            return;
        }

        //Remove the current scene from the stack
        scenes.pop();

        //Load the next last scene from the stack
        setScene(scenes.peek());
    }
}