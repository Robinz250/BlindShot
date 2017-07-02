package org.academiadecodigo.bootcamp;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by codecadet on 28/06/17.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Client client = new Client();
        Thread thread = new Thread(client);
        thread.start();
        //client.connectToServer();
        //client.startChat();

        Navigation.getInstance().setClient(client);
        Navigation.getInstance().setStage(primaryStage);
        Navigation.getInstance().loadScreen("grid");
        primaryStage.setResizable(false);

    }

    public static void main(String[] args) {
        launch(args);
    }

}

