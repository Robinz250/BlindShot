package org.academiadecodigo.bootcamp.controller;

import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.academiadecodigo.bootcamp.service.Client;
import org.academiadecodigo.bootcamp.Navigation;
import org.academiadecodigo.bootcamp.Waiting;
import org.academiadecodigo.bootcamp.avatar.Avatar;

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
        client = Navigation.getInstance().getClient();
        chooseAvatar();
    }

    @FXML
    public void playerMessage() throws IOException {
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
    private void goToInstructions(ActionEvent event) {
        Navigation.getInstance().loadScreen("instructionsview");
    }

    private void chooseAvatar() {
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

            GridPane.setHalignment(iv1, HPos.CENTER);
            GridPane.setHalignment(avatarGrid, HPos.CENTER);
            menuPane.add(iv1, 0, 0);
            menuPane.add(avatarGrid, 0, 0);
            menuPane.setAlignment(Pos.CENTER);
            iv1.setOnMouseClicked(avatarSelected);
            iv1.setOnMouseEntered(hoverAvatar);
            iv1.setOnMouseExited(exitAvatar);
        }
    }
}
