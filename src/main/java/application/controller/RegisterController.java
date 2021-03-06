package application.controller;

import application.DataHolder;
import application.entities.Client;
import application.entities.Permission;
import application.entities.Restaurant;
import gui.controller.ILocationGetter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class RegisterController implements Initializable, ILocationGetter {
    Restaurant temp_rest;

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
    private TextField TF_Geo_Long;
    @FXML
    private TextField TF_Geo_Lat;

    @FXML
    void btn_clicked(ActionEvent event) throws IOException {
        Button btn = (Button) event.getSource(); // get the button object.
        if (btn.equals(btn_register)){
            if (temp_rest==null) return; // not clicked on set location btn.
            DataHolder.restaurant.save(temp_rest); // save the data.
            BaseFrameController.instance.changeFrame("application/fxml/entrance.fxml");
            Preferences prefs = Preferences.userNodeForPackage(LoginController.class);
            prefs.put("id",DataHolder.rest_id);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("new restaurant");
            alert.setHeaderText("you registered new restaurant to the system. welcome!");
            alert.setContentText("save your id for login: "+ DataHolder.rest_id);
            alert.showAndWait();
        }
        if (btn.equals(btn_location)){
            temp_rest= new Restaurant(TF_restName.getText(),TF_ownerName.getText(),TF_address.getText(),TF_phone.getText(),TF_password.getText(),null,null,"-1");
            temp_rest.getEmployee().add(new Client(TF_ownerName.getText(),TF_password.getText(), Permission.Owner));
            temp_rest.setModel_width(5);
            temp_rest.setModel_height(5);
            DataHolder.tempRest=temp_rest;
            try {
                BaseFrameController.instance.changeFrame(new MapController(this));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
    @Override
    public void setLocation(GeoPosition geo) {
            TF_Geo_Long.setText(geo.getLongitude()+"");
            TF_Geo_Lat.setText(geo.getLatitude()+"");
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (DataHolder.tempRest!=null){
            Restaurant tmp=DataHolder.tempRest;
            TF_restName.setText(tmp.getName());
            TF_ownerName.setText(tmp.getOwnerName());
            TF_address.setText(tmp.getAddress());
            TF_phone.setText(tmp.getPhoneNumber());
            TF_password.setText(tmp.getPassword());
            TF_Geo_Long.setText(tmp.getPosition().getLongitude()+"");
            TF_Geo_Lat.setText(tmp.getPosition().getLatitude()+"");
            temp_rest=tmp;

        }
    }
}
