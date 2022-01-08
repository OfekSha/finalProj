package application.controller;

import application.DataHolder;
import application.controller.tabs.*;
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
    private GridPane gridpane_options;
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
    private HBox hbox_buttons;

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
    private VBox vbox_model_view_buttons;
    @FXML
    private GridPane modelTable;


    // notification tab:
    @FXML
    private Tab notifications;

    @FXML
    private TableView<NotificationsController.TableNotificationData> tableView_notifications;

    @FXML
    private HBox buttons_notifications;
// chat tab:

    @FXML
    private Tab chat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showByPermission(DataHolder.myUser.getPermission());

    }
    private void showByPermission(Permission p){
        ObservableList<Tab> tabs = tabPane.getTabs();
        switch (p) {
            case Waiter:
                tabs.remove(notifications);
                tabs.remove(chat);
            case Hostess:
                tabs.remove(options);
                DataHolder.isAutoApprove=true; // only hostess and waiter.
            case Manager:
                tabs.remove(permissions);
                tabs.remove(modelEdit);
            case Owner:
                modelController=new ModelController(modelTable,vbox_model_view_buttons);
                if (p==Permission.Waiter) break;
                new ModelEditorController(vbox_edit_menu,modelEditTable);
                NotificationsController nController= new NotificationsController(tableView_notifications,buttons_notifications);
                if (p==Permission.Manager) break;
                PermissionsController pController=new PermissionsController(tableView_permissions,hbox_buttons);
                OptionsController optionTab= new OptionsController(gridpane_options);
                break;
            default:
                tabs.clear();
// the permission not exist.
        }
    }

}
