package application.controller.tabs;

import application.DataHolder;
import application.entities.Client;
import application.entities.Permission;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.util.converter.DefaultStringConverter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionsController {
    public class TableClientData{
        private final SimpleStringProperty name;
        private final SimpleStringProperty password;
        private final SimpleStringProperty permission;
        public TableClientData(Client client) {
            this.name = new SimpleStringProperty(client.getName());
            this.password = new SimpleStringProperty(client.getPassword());
            this.permission = new SimpleStringProperty(client.getPermission().name());
        }

        public TableClientData(){
            this(new Client());
        }
        public Client getClient(){
            return new Client(getName(),getPassword(), Permission.valueOf(getPermission()));
        }

        public String getName() {
            return name.get();
        }

        public void setName(String name) {
            this.name.set(name);
        }

        public String getPassword() {
            return password.get();
        }

        public void setPassword(String pass) {
            password.set(pass);
        }

        public String getPermission() {
            return permission.get();
        }

        public void setPermission(String permission) {
            this.permission.set(permission);
        }

    }
    TableView<TableClientData> tableView;
    HBox hbox_buttons;
    public PermissionsController(TableView<TableClientData> table, HBox hbox_buttons){
        tableView=table;
        this.hbox_buttons=hbox_buttons;
        if (table !=null)
        initPermissions();
        Button save=new Button("save");
        Button add=new Button("add");
        Button delete=new Button("delete");
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<TableClientData> tableData = tableView.getItems();
                    DataHolder.restaurant.get(DataHolder.rest_id).ifPresent(restaurant -> {
                        restaurant.getEmployee().clear();
                        restaurant.getEmployee().addAll(getListOfClients(tableData));
                        DataHolder.restaurant.update(restaurant,null);
                    });
            }
        });
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableView.getItems().add(new TableClientData());
            }
        });
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableView.getItems().remove(tableView.getSelectionModel().getFocusedIndex());
            }
        });
        hbox_buttons.getChildren().addAll(save,add,delete);

    }
    private void savePermissionsList(ActionEvent event) {


    }
    private List<TableClientData> getListOfData(Set<Client> clients){
        List<TableClientData> list = new ArrayList<TableClientData>();
        for (Client client:clients){
            list.add(new TableClientData(client));
        }
        return list;
    }
    private Set<Client> getListOfClients(List<TableClientData> data){
        Set<Client> set = new HashSet<Client>();
        for (TableClientData client:data){
            set.add(new Client(client.getName(), client.getPassword(),Permission.valueOf(client.getPermission())));
        }
        return set;
    }
    private void initPermissions(){
        DataHolder.restaurant.get(DataHolder.rest_id).ifPresent(restaurant -> {
            HashSet<Client> employee=restaurant.getEmployee();
            List<TableClientData> tableData = getListOfData(employee);
            ObservableList<TableClientData> data =  FXCollections.observableList(tableData);

            // set data for begin: @@ TODO: remove the data and move it to test.
            tableView.setItems(data);
            // set table editable and build it's columns:
            tableView.setEditable(true);
            TableColumn<TableClientData, String> tc_name = new TableColumn<TableClientData, String>("name");
            TableColumn<TableClientData, String> tc_password = new TableColumn<TableClientData, String>("password");
            TableColumn<TableClientData, String> tc_permission = new TableColumn<TableClientData, String>("permission");
            tc_name.setCellValueFactory(cellData -> cellData.getValue().name);
            tc_name.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
            tc_name.setEditable(true);
            tc_password.setCellValueFactory(cellData -> cellData.getValue().password);
            tc_password.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
            tc_password.setEditable(true);
            tc_permission.setCellValueFactory(cellData -> cellData.getValue().permission);
            // set the options in the cell of permission column:
            ObservableList<String> options = FXCollections.observableArrayList();
            for (Permission per : Permission.values()) {

                options.add(per.name());
            }
            tc_permission.setCellFactory(ComboBoxTableCell.forTableColumn(options));
            tc_permission.setEditable(true);
            tableView.getColumns().addAll(tc_name, tc_password, tc_permission);
            tc_name.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));
            tc_password.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));
            tc_permission.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
            tc_name.setResizable(false);
            tc_password.setResizable(false);
            tc_permission.setResizable(false);
        });

    }
}
