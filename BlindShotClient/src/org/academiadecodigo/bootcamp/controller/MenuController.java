package org.academiadecodigo.bootcamp.controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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
    private Text errorText;

    @FXML
    private Label serverMessage;

    @FXML
    private GridPane menuPane;


    /**
     * Event on click Avatar : remove class avatarSelected from all avatar, and add on selected avatar.
     * Then set avatar on GridController
     */
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

            String[] divide = event.getPickResult().getIntersectedNode().getId().split(" ");

            GridController.setAvatar(Avatar.values()[Integer.parseInt(divide[1])]);
        }
    };

    /**
     * Event on hover Avatar : add Class avatarHover and remove initialAvatar with opacity.
     */

    private EventHandler hoverAvatar = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            node = event.getPickResult().getIntersectedNode();
            event.getPickResult().getIntersectedNode().getStyleClass().add("avatarHover");
            event.getPickResult().getIntersectedNode().getStyleClass().remove("initialAvatar");
        }
    };

    /**
     * Event on hoverOut Avatar : add Class initialAvatar and remove avatarHover.
     */
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
        if (NameIsEmpty() && avatarIsSelected()) {
            errorText.setText("");
            if (playButton.getText().equals("Submit")) {
                try {
                    client.sendMessage(playerMessage.getText());
                    serverMessage.setText("Hello, " + playerMessage.getText() + "! You will be player " + client.getPlayer());
                    playButton.setText("Play");
                    playerMessage.setVisible(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                client.sendMessage("I'm ready");
                client.receiveMessage();
                playButton.setVisible(false);
                serverMessage.setText("Waiting for other players to connect...");
                new Thread(new Waiting(client.getClientSocket())).start();
                System.out.println((client.getPlayer()));
            }
        } else {
            if (!NameIsEmpty()) {
                playerMessage.setStyle("-fx-border-color: red");
                errorText.setText("Choose a name");
                return;
            }
            if (!avatarIsSelected()) {
                int depth = 100; //Setting the uniform variable for the glow width and height
                DropShadow borderGlow = new DropShadow();
                borderGlow.setOffsetY(0f);
                borderGlow.setOffsetX(0f);
                borderGlow.setColor(Color.GOLD);
                borderGlow.setWidth(depth);
                borderGlow.setHeight(depth);

                for (Node PaneChildren : menuPane.getChildren()) {
                    if (PaneChildren instanceof Label && PaneChildren.getId().contains("Avatar")) {
                        PaneChildren.setEffect(borderGlow);

                        Timeline timeline = new Timeline(new KeyFrame(
                                Duration.millis(2000),
                                ae -> PaneChildren.setEffect(null)));
                        timeline.play();
                    }
                }
                errorText.setText("Choose a Player");
            }
        }
    }

    @FXML
    private void goToInstructions(ActionEvent event) {
        Navigation.getInstance().loadScreen("instructionsview");
    }

    /**
     * create avatars and display on screen.
     */

    private void chooseAvatar() {
        for (int i = 0; i < Avatar.values().length; i++) {
            Text avatarText = new Text();
            Label imageHolder = new Label();
            imageHolder.setMaxSize(150, 150);
            imageHolder.setId("Avatar " + i);
            imageHolder.setStyle("-fx-background-image:url(" + Avatar.values()[i].getImage() + ") ");
            imageHolder.getStyleClass().add("initialAvatar");
            imageHolder.setTranslateY(-30);
            avatarText.setText(Avatar.values()[i].getName());
            avatarText.setId("Text");
            avatarText.setTranslateY(70);
            if (i == 0) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(1495), imageHolder);
                TranslateTransition uu = new TranslateTransition(Duration.millis(1495), avatarText);

                uu.setByX(-200);
                tt.setByX(-200);

                tt.play();
                uu.play();
            }
            if (i == 1) {
                TranslateTransition t2 = new TranslateTransition(Duration.millis(1495), imageHolder);
                TranslateTransition t1 = new TranslateTransition(Duration.millis(1495), avatarText);

                t1.setByX(200);
                t2.setByX(200);

                t1.play();
                t2.play();
            }

            if (i == 2) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(1495), imageHolder);
                tt.setByX(0);
                tt.play();
            }

            menuPane.setHalignment(imageHolder, HPos.CENTER);
            menuPane.setHalignment(avatarText, HPos.CENTER);

            menuPane.add(imageHolder, 0, 0);
            menuPane.add(avatarText, 0, 0);

            menuPane.setAlignment(Pos.CENTER);
            imageHolder.setOnMouseClicked(avatarSelected);
            imageHolder.setOnMouseEntered(hoverAvatar);
            imageHolder.setOnMouseExited(exitAvatar);
        }
    }

    /**
     * check if avatar is selected
     */
    private boolean avatarIsSelected() {
        for (Node PaneChildren : menuPane.getChildren()) {
            if (PaneChildren instanceof Label && PaneChildren.getId().contains("Avatar")) {
                for (Object ChildrenClasses : PaneChildren.getStyleClass()) {
                    if (ChildrenClasses.toString().contains("avatarSelected")) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean NameIsEmpty() {
        if (playerMessage.getText().equals("")) {
            return false;
        }
        return true;
    }
}
