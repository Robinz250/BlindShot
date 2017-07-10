package org.academiadecodigo.bootcamp;

import javafx.application.Application;
import javafx.stage.Stage;
import org.academiadecodigo.bootcamp.model.Player;
import org.academiadecodigo.bootcamp.service.GameCommunication;
import org.academiadecodigo.bootcamp.service.GameService;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Player player = new Player();
        player.init();

        GameService gameService = new GameService();
        gameService.setPlayer(player);

        GameCommunication gameCommunication = new GameCommunication();
        gameCommunication.setPlayer(player);

        Navigation.getInstance().setStage(primaryStage);
        Navigation.getInstance().setGameService(gameService);
        Navigation.getInstance().setGameCommunication(gameCommunication);
        Navigation.getInstance().loadScreen("startmenu");
        primaryStage.setResizable(false);
    }

    /**
     * First argument the ip of the server
     * second argument the number of players
     * @param args
     */
    public static void main(String[] args) {
        Player.SERVER_IP = args[0];
        GameService.NUMBER_OF_PLAYERS = Integer.parseInt(args[1]);
        launch(args);
    }
}
