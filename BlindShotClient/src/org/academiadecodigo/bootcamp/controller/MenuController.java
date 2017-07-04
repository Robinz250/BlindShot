package org.academiadecodigo.bootcamp.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.academiadecodigo.bootcamp.Client;
import org.academiadecodigo.bootcamp.Navigation;
import org.academiadecodigo.bootcamp.Waiting;

import javax.print.attribute.standard.MediaSize;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by codecadet on 02/07/17.
 */
public class MenuController implements Initializable {

    private Client client;

    @FXML
    private Button playButton;

    @FXML
    private TextField playerMessage;

    @FXML
    private Label serverMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = Navigation.getInstance().getClient();
        playButton.setText("Submit");
    }

    @FXML
    public void playerMessage() throws IOException {

        // se o botão disser "submit", envia mensagem para o servidor com o nome do player, escrito no textfield
        if (playButton.getText().equals("Submit")) {
            try {
                client.sendMessage(playerMessage.getText());
                serverMessage.setText("Hello, " + playerMessage.getText() + "! You will be player " + client.getPlayer());
                playButton.setText("Play");
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
            playerMessage.setVisible(false);
            playButton.setVisible(false);
            serverMessage.setText("Waiting for other players to connect...");
            new Thread(new Waiting(client.getClientSocket())).start();
            System.out.println((client.getPlayer()));
        }
    }
}
