package org.academiadecodigo.bootcamp.controller;

        import javafx.event.ActionEvent;
        import javafx.fxml.FXML;
        import javafx.fxml.Initializable;
        import javafx.scene.control.Button;
        import javafx.scene.control.Label;

        import java.net.URL;
        import java.util.ResourceBundle;

public class GameOverController implements Initializable {

    @FXML
    private Button quitButton;

    @FXML
    private Label winnerLabel;

    @FXML
    private Button restartButton;

    @FXML
    void quit(ActionEvent event) {

    }

    @FXML
    void restart(ActionEvent event) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setWinnerLabelText(String labelText) {
        winnerLabel.setText(labelText);
    }
}
