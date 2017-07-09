package org.academiadecodigo.bootcamp;

import javafx.application.Application;
import javafx.stage.Stage;
import org.academiadecodigo.bootcamp.service.Client;

/**
 * Created by codecadet on 28/06/17.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Client client = new Client();
        new Thread(client).start();
        Navigation.getInstance().setStage(primaryStage);
        Navigation.getInstance().setClient(client);
        Navigation.getInstance().loadScreen("startmenu");
        primaryStage.setResizable(false);

    }

    public static void main(String[] args) {
        launch(args);
    }

}

