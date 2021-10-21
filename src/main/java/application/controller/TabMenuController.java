package application.controller;

import application.controller.tabs.ModelController;
import application.controller.tabs.PermissionsController;
import application.entities.Permission;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TabMenuController implements Initializable {

    public ModelController modelController;
    @FXML
    private TabPane tabPane;
    @FXML
    private GridPane modelTable;
    @FXML
    private Tab settings;


    @FXML
    private TableView tableView_permissions;
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
    private TableColumn tc_name;
    @FXML
    private TableColumn tc_password;
    @FXML
    private TableColumn tc_permission;
    @FXML
    private HBox header;
    @FXML
    private Button btn_permis_save;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showByPermission(Permission.Owner); // testing

    }
    private void showByPermission(Permission p){
        ObservableList<Tab> tabs = tabPane.getTabs();
        switch (p) {
            case Waiter:
                modelController=new ModelController(modelTable);
                modelController.testModel(); // @@TODO: need to delete it and move to test.
                tabs.remove(notifications);
                tabs.remove(chat);
            case Hostess:
                tabs.remove(settings);
            case Manager:
                tabs.remove(permissions);
                tabs.remove(modelEdit);
            case Owner:
                PermissionsController pController=new PermissionsController(tableView_permissions);
                break;
            default:
                tabs.clear();
// the permission not exist.
        }
    }

}
