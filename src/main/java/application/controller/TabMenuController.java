package application.controller;

import application.entities.Permission;
import application.entities.Table;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;

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
        modelController=new ModelController(modelView);
        ArrayList<Table> testTables = new ArrayList<Table>(); //@@@ test only!!!
        testTables.add(new Table(0,4,false,true,0,0));
        testTables.add(new Table(1,4,false,true,2,0));
        testTables.add(new Table(2,4,false,true,2,1));
        testTables.add(new Table(3,4,false,true,0,3));
        testTables.add(new Table(4,4,false,true,4,4));
        testTables.add(new Table(5,4,false,true,3,0));
        setModel(6,6,testTables);
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
    private void  setModel(int x , int y, ArrayList<Table> tables){
        modelTable.getColumnConstraints().clear();
        modelTable.getRowConstraints().clear();
        modelTable.getChildren().clear();
        modelTable.setGridLinesVisible(true);
        for (int i=0;i<y;i++){
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / y);
            modelTable.getColumnConstraints().add(colConst);
        }
        for (int i=0;i<x;i++){
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / x);
            modelTable.getRowConstraints().add(rowConst);
        }
        for (Table table:tables){
            Pane cell= new Pane();
            TextField tf;
            cell.getChildren().add(tf=new TextField(""+table.getId()));
            //cell.setPrefWidth(3);
            //cell.setPrefHeight(3);
            modelTable.add(cell,table.getY(),table.getX());

        }
    }
}
