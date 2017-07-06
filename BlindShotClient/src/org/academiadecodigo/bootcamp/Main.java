package org.academiadecodigo.bootcamp;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by codecadet on 28/06/17.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // faz load da view pre-game na thread do javaFX

        Navigation.getInstance().setStage(primaryStage);
        Navigation.getInstance().loadScreen("startmenu");
        primaryStage.setResizable(false);






    }

    public static void main(String[] args) {
        launch(args);
    }

}

