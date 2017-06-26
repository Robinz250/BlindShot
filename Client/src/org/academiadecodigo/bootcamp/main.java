package org.academiadecodigo.bootcamp;

import javafx.application.Application;
import javafx.scene.shape.Sphere;
import javafx.stage.Stage;
import org.academiadecodigo.bootcamp.utils.Navigation;

/**
 * Created by codecadet on 26/06/17.
 */
public class main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Navigation navigation = Navigation.getInstance();
        navigation.setStage(primaryStage);

        navigation.loadScreen("client");
    }

    public void init(){

    }
}
