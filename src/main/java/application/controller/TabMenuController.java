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
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TabMenuController implements Initializable {

    public ModelController modelController;
    @FXML
    private HBox header;

    @FXML
    private TabPane tabPane;
    // options tab:
    @FXML
    private Tab options;

    @FXML
    private TextField tf_name;

    @FXML
    private SplitMenuButton smbtn_tags;

    @FXML
    private TextField tf_owner;

    @FXML
    private TextField tf_country;

    @FXML
    private TextField tf_region;

    @FXML
    private TextField tf_adress;

    @FXML
    private TextField tf_phone;

    @FXML
    private MenuButton mb_status;
    // permission tab:
    @FXML
    private Tab permissions;

    @FXML
    private TableView tableView_permissions;

    @FXML
    private Button btn_permis_save;
    // model editor tab:
    @FXML
    private Tab modelEdit;

    @FXML
    private VBox vbox_edit_menu;

    @FXML
    private GridPane modelEditTable;
    // model tab:
    @FXML
    private Tab model1;

    @FXML
    private GridPane modelTable;

    @FXML
    private Tab notifications;

    @FXML
    private Tab chat;

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
                tabs.remove(options);
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
