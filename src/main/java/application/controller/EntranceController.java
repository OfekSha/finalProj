package application.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class EntranceController implements Initializable {
    @FXML
    private Button btn_login;

    @FXML
    private Button btn_register;

    @FXML
    private HBox header;

    @FXML
    void btnClicked(ActionEvent event) {
        Button btn = (Button) event.getSource(); // get the button object.
        Stage stage = (Stage) btn.getScene().getWindow(); // get the stage of the button.
        FXMLLoader loader = null;
        if (btn.equals(btn_login)) { // login button clicked.
            loader = new FXMLLoader(getClass().getClassLoader().getResource("application/fxml/Login.fxml"));
        }
        else if (btn.equals(btn_register)) {// register button clicked.
            URL url;
            loader = new FXMLLoader(getClass().getClassLoader().getResource("application/fxml/Register.fxml"));
        }
        else if (loader==null) return; // button not exist.
        try { // change the scene by loading from fxml file.
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
