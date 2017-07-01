package org.academiadecodigo.bootcamp;

import javafx.application.Application;
import javafx.stage.Stage;
import org.academiadecodigo.bootcamp.utils.Navigation;

import java.io.IOException;

/**
 * Created by codecadet on 28/06/17.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Navigation.getInstance().setStage(primaryStage);
        //Navigation.getInstance().loadScreen("grid");
        primaryStage.setResizable(false);

        Client client = new Client();

        new Thread(client).start();

    }

    public static void main(String[] args) {
        launch(args);
    }

}

