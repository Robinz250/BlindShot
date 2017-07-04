package org.academiadecodigo.bootcamp;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Created by codecadet on 28/06/17.
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // cria e inicia uma thread para fazer a comunicação pre-game com o servidor
        Client client = new Client();
        Thread thread = new Thread(client);
        thread.start();

        // faz load da view pre-game na thread do javaFX
        Navigation.getInstance().setClient(client);
        Navigation.getInstance().setStage(primaryStage);
        Navigation.getInstance().loadScreen("startmenu");
        primaryStage.setResizable(false);






    }

    public static void main(String[] args) {
        launch(args);
    }

}

