package application.controller;

import application.DataHolder;
import application.entities.Restaurant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class LoginController {

    @FXML
    private HBox header;

    @FXML
    private TextField TF_id;

    @FXML
    private TextField TF_name;

    @FXML
    private PasswordField TF_password;

    @FXML
    private Button btn_login;
    @FXML
    void btnClicked(ActionEvent event) {
        Button btn = (Button) event.getSource(); // get the button object.
        if (btn.equals(btn_login)) { // login button clicked.
            Optional<Restaurant> temp = DataHolder.dao_rest.get(Long.parseLong(TF_id.getText()));
            temp.ifPresent(rest -> {
                if (rest.getPassword().equals(TF_password.getText())) {
                    try {
                        Stage stage = (Stage) btn.getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("application/fxml/tabMenu.fxml"));
                        Scene scene = new Scene(loader.load());
                        stage.setScene(scene);
                    } catch (IOException io) {
                        io.printStackTrace();
                    }
                }
                else{
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Wrong input");
                    alert.setHeaderText("Wrong password or restaurant id");
                    alert.setContentText("Please try again.");

                    alert.showAndWait();
                }

            });

        }
    }

}
