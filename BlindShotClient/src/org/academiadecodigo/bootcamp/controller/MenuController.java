package org.academiadecodigo.bootcamp.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import org.academiadecodigo.bootcamp.Client;
import org.academiadecodigo.bootcamp.Navigation;
import org.academiadecodigo.bootcamp.Service.MessageService;
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

    @FXML
    private Button playButton;

    @FXML
    private TextField playerMessage;

    @FXML
    private Label serverMessage;

    @FXML
    private GridPane menuPane;

    private EventHandler AvatarSelected = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            GridController.setAvatar(Avatar.values()[Integer.parseInt(event.getPickResult().getIntersectedNode().getId())]);
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        this.client = new Client();
        Thread thread = new Thread(client);
        thread.start();
        MessageService.setClient(client);

        //create avatars to choose

        for(int i = 0 ; i < Avatar.values().length ; i++){
            Image image = new Image(Avatar.values()[i].getImage());
            ImageView iv1 = new ImageView();
            iv1.setImage(image);
            iv1.setId(""+i);

            if (i % 2 == 0 && i > 0){
                iv1.setTranslateX(-180);
            } else{
                iv1.setTranslateX(180);
            }

            if(i == 0){
                iv1.setTranslateX(0);
            }

            menuPane.setHalignment(iv1, HPos.CENTER);
            menuPane.add(iv1,0,0);
            menuPane.setAlignment(Pos.CENTER);
            iv1.setOnMouseClicked(AvatarSelected);
            //System.out.println(Navigation.getInstance().getScene().getWidth());
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
            serverMessage.setText("Waiting for other avatar to connect...");
            new Thread(new Waiting(client.getClientSocket())).start();
            System.out.println((client.getPlayer()));
        }
    }
}
