package application.controller;

import application.DataHolder;
import application.entities.Client;
import application.entities.Restaurant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.util.HashSet;
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
            Optional<Restaurant> temp = DataHolder.restaurant.get(TF_id.getText());
            temp.ifPresent(rest -> {
                if (checkLogin(temp.get())) {
                    try {
                        BaseFrameController.instance.changeFrame("application/fxml/tabMenu.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
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
    private boolean checkLogin(Restaurant rest){ // check if user is correct and update the user for future use.
        HashSet<Client> employee = rest.getEmployee();
        for (Client e :employee){
            if (TF_password.getText().equals(e.getPassword()) &&  TF_name.getText().equals(e.getName())){
                DataHolder.myUser=e;
                return true;
            }
        }
        return false;
}
}
