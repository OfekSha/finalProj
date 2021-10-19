package application.controller;

import application.entities.Permission;
import application.entities.Table;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ArrayList;
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
        modelController=new ModelController(modelTable);
        ArrayList<Table> testTables = new ArrayList<Table>(); //@@@ test only!!!
        testTables.add(new Table(0,4,false,false,0,0));
        testTables.add(new Table(1,3,true,false,2,0));
        testTables.add(new Table(2,5,true,true,2,1));
        testTables.add(new Table(3,10,true,true,0,3));
        testTables.add(new Table(4,2,false,true,4,4));
        testTables.add(new Table(99,9,false,false,3,0));
        modelController.setModel(10,10,testTables);
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
