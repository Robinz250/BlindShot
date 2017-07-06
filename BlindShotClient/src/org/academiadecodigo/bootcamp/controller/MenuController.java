package org.academiadecodigo.bootcamp.controller;

import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.academiadecodigo.bootcamp.Client;
import org.academiadecodigo.bootcamp.Navigation;
import org.academiadecodigo.bootcamp.Service.MessageService;
import org.academiadecodigo.bootcamp.Waiting;
import org.academiadecodigo.bootcamp.avatar.Avatar;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by Paulo Sousa on 02/07/17.
 */
public class MenuController implements Initializable {

    private Client client;

    private Node node;

    @FXML
    private Button playButton;

    @FXML
    private TextField playerMessage;

    @FXML
    private Label serverMessage;

    @FXML
    private Hyperlink instructionsLink;

    @FXML
    private GridPane menuPane;

    private EventHandler avatarSelected = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            for (Node s : menuPane.getChildren()) {
                if (s.getId().contains("Avatar")) {
                    s.getStyleClass().remove("avatarSelected");
                }
            }

            event.getPickResult().getIntersectedNode().getStyleClass().add("avatarSelected");
            event.getPickResult().getIntersectedNode().getStyleClass().remove("initialAvatar");

            String[] divide;
            divide = event.getPickResult().getIntersectedNode().getId().split(" ");

            GridController.setAvatar(Avatar.values()[Integer.parseInt(divide[1])]);
        }
    };
    private EventHandler hoverAvatar = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            node = event.getPickResult().getIntersectedNode();
            event.getPickResult().getIntersectedNode().getStyleClass().add("avatarHover");
            event.getPickResult().getIntersectedNode().getStyleClass().remove("initialAvatar");
        }
    };

    private EventHandler exitAvatar = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            node.getStyleClass().remove("avatarHover");
            node.getStyleClass().add("initialAvatar");
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.client = new Client();
        Thread thread = new Thread(client);
        thread.start();
        MessageService.setClient(client);

        //create avatars to choose

        for (int i = 0; i < Avatar.values().length; i++) {
            Text avatarGrid = new Text();
            Label iv1 = new Label();
            iv1.setMaxSize(150, 150);
            iv1.setId("Avatar " + i);
            iv1.setStyle("-fx-background-image:url(" + Avatar.values()[i].getImage() + ") ");
            iv1.getStyleClass().add("initialAvatar");
            iv1.setTranslateY(-30);
            avatarGrid.setText(Avatar.values()[i].getName());
            avatarGrid.setId("AvatarText");
            avatarGrid.setTranslateY(70);
            if (i == 0) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(1495), iv1);
                TranslateTransition uu = new TranslateTransition(Duration.millis(1495), avatarGrid);

                uu.setByX(-200);
                tt.setByX(-200);

                tt.play();
                uu.play();
            }
            if (i == 1) {
                TranslateTransition t2 = new TranslateTransition(Duration.millis(1495), iv1);
                TranslateTransition t1 = new TranslateTransition(Duration.millis(1495), avatarGrid);

                t1.setByX(200);
                t2.setByX(200);

                t1.play();
                t2.play();
            }

            if (i == 2) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(1495), iv1);
                tt.setByX(0);
                tt.play();
            }


            menuPane.setHalignment(iv1, HPos.CENTER);
            menuPane.setHalignment(avatarGrid, HPos.CENTER);
            menuPane.add(iv1, 0, 0);
            menuPane.add(avatarGrid, 0, 0);
            menuPane.setAlignment(Pos.CENTER);
            iv1.setOnMouseClicked(avatarSelected);
            iv1.setOnMouseEntered(hoverAvatar);
            iv1.setOnMouseExited(exitAvatar);
        }


        //client = Navigation.getInstance().getClient();
    }

    @FXML
    public void playerMessage() throws IOException {

        // se o botão disser "submit", envia mensagem para o servidor com o nome do player, escrito no textfield
        if (playButton.getText().equals("Submit")) {
            try {
                client.sendMessage(playerMessage.getText());
                serverMessage.setText("Hello, " + playerMessage.getText() + "! You will be player " + client.getPlayer());
                playButton.setText("Play");
                playerMessage.setVisible(false);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // depois de enviado o nome, o butão de "submit" transforma-se em "play"; ao ser clickado, o cliente envia uma
        // mensagem ao servidor a informar que está pronto para jogar; recebe mensagem do servidor a dizer que ta a espera que os outros clientes respondam
        // cria uma nova thread que fica a espera da mensagem do servidor para começar o jogo, e que faz load da view do jogo qd a recebe
        else {
            client.sendMessage("I'm ready");
            client.receiveMessage();
            playButton.setVisible(false);
            serverMessage.setText("Waiting for other players to connect...");
            new Thread(new Waiting(client.getClientSocket())).start();
            System.out.println((client.getPlayer()));
        }
    }
    @FXML
    void goToInstructions(ActionEvent event) {
        Navigation.getInstance().loadScreen("instructionsview");


    }
}
