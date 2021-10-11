package application.controller;

import application.entities.Permission;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TabMenuController implements Initializable {
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab settings;


    @FXML
    private Tab permissions;

    @FXML
    private Tab modelEdit;

    @FXML
    private Tab modelView;

    @FXML
    private Tab notifications;

    @FXML
    private Tab chat;

    @FXML
    private HBox header;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showByPermission(Permission.Owner); // testing
    }
    private void showByPermission(Permission p){
        ObservableList<Tab> tabs = tabPane.getTabs();
        switch (p){
            case Owner:
                tabs.remove(chat);
                break;
            case Manager:
                tabs.remove(permissions);
                tabs.remove(modelEdit);
                tabs.remove(chat);
                break;
            case Hostess:
                tabs.remove(permissions);
                tabs.remove(modelEdit);
                tabs.remove(settings);
                break;
            case Waiter:
                tabs.remove(settings);
                tabs.remove(permissions);
                tabs.remove(modelEdit);
                tabs.remove(notifications);
                tabs.remove(chat);
                break;


        }
    }
}
