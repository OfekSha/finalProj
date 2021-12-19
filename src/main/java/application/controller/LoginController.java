package application.controller;

import application.DataHolder;
import application.entities.Client;
import application.entities.Restaurant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class LoginController implements Initializable {

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
            if (temp.isEmpty()){
                showErrorMessage("restaurant id is wrong.");
                return;
            }
            Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
            prefs.put("id",temp.get().getId());
            temp.ifPresent(rest -> {
                if (checkLogin(temp.get())) {
                    try {
                        BaseFrameController.instance.changeFrame("application/fxml/tabMenu.fxml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                else{
                    showErrorMessage("wrong password or name.");
                }

            });

        }
    }
    private void showErrorMessage(String msg){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Wrong input");
        alert.setHeaderText(msg);
        alert.setContentText("Please try again.");
        alert.showAndWait();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
        TF_id.setText(prefs.get("id",""));
    }
}
