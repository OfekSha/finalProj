package application.controller.tabs;

import application.DataHolder;
import application.entities.Client;
import application.entities.Permission;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.converter.DefaultStringConverter;

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
    public PermissionsController(TableView<TableClientData> table){
        tableView=table;
        if (table !=null)
        initPermissions();

    }
    private void savePermissionsList(ActionEvent event) {
        ObservableList<TableClientData> tableData = tableView.getItems();
        for (TableClientData data:tableData){
            DataHolder.clients.save(data.getClient());
        }

    }
    private void initPermissions(){
        // set data for begin: @@ TODO: remove the data and move it to test.
        ObservableList<TableClientData> data =
                FXCollections.observableArrayList(
                        new TableClientData(),
                        new TableClientData()
                );
        tableView.setItems(data);
        // set table editable and build it's columns:
        tableView.setEditable(true);
        TableColumn<TableClientData, String> tc_name= new TableColumn<TableClientData, String>("name");
        TableColumn<TableClientData, String> tc_password= new TableColumn<TableClientData, String>("password");
        TableColumn<TableClientData, String> tc_permission= new TableColumn<TableClientData, String>("permission");
        tc_name.setCellValueFactory( cellData -> cellData.getValue().name);
        tc_name.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        tc_name.setEditable(true);
        tc_password.setCellValueFactory( cellData -> cellData.getValue().password);
        tc_password.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        tc_password.setEditable(true);
        tc_permission.setCellValueFactory( cellData -> cellData.getValue().permission);
        // set the options in the cell of permission column:
        ObservableList<String> options = FXCollections.observableArrayList();
        for (Permission per : Permission.values()){

            options.add(per.name());
        }
        tc_permission.setCellFactory(ComboBoxTableCell.forTableColumn(options));
        tc_permission.setEditable(true);
        tableView.getColumns().addAll(tc_name,tc_password,tc_permission);
        tc_name.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));
        tc_password.prefWidthProperty().bind(tableView.widthProperty().multiply(0.4));
        tc_permission.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        tc_name.setResizable(false);
        tc_password.setResizable(false);
        tc_permission.setResizable(false);

    }
}
