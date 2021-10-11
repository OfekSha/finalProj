package application.controller;

import application.DataHolder;
import application.entities.Restaurant;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class RegisterController {
    Restaurant temp_rest;
    @FXML
    private HBox header;

    @FXML
    private TextField TF_restName;



    @FXML
    private SplitMenuButton SMBtn_tags;

    @FXML
    private TextField TF_ownerName;

    @FXML
    private TextField TF_address;

    @FXML
    private TextField TF_phone;
    @FXML
    private TextField TF_password;

    @FXML
    private Button btn_location;

    @FXML
    private Button btn_register;

    @FXML
    void btn_clicked(ActionEvent event) {
        Button btn = (Button) event.getSource(); // get the button object.
        if (btn.equals(btn_register)){
            temp_rest= new Restaurant(TF_restName.getText(),TF_ownerName.getText(),TF_address.getText(),TF_phone.getText(),TF_password.getText(),null,null,-1);
            DataHolder.dao_rest.save(temp_rest);

        }


    }


}
