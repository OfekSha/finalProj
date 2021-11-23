package application.controller.tabs;

import application.DataHolder;
import application.entities.Request;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.Pane;
import javafx.util.converter.BooleanStringConverter;
import javafx.util.converter.DefaultStringConverter;

import java.util.ArrayList;
import java.util.List;

public class NotificationsController {

    public class TableNotificationData {
        private final SimpleStringProperty client_id;
        private final SimpleStringProperty table_id;
        private final SimpleBooleanProperty approved;
        private final SimpleStringProperty time;

        public TableNotificationData(String client_id, String table_id, boolean approved, String time) {
            this.client_id = new SimpleStringProperty(client_id);
            this.table_id = new SimpleStringProperty(table_id);
            this.approved = new SimpleBooleanProperty(approved);
            this.time = new SimpleStringProperty(time);
        }

        public Request getRequestObject() {
            return new Request(client_id.get(), table_id.get(), time.get(), approved.get());
        }

        public TableNotificationData() {
            this("no id", "no id", false, "no time");
        }

        public String getClient_id() {
            return client_id.get();
        }

        public SimpleStringProperty client_idProperty() {
            return client_id;
        }

        public void setClient_id(String client_id) {
            this.client_id.set(client_id);
        }

        public String getTable_id() {
            return table_id.get();
        }

        public SimpleStringProperty table_idProperty() {
            return table_id;
        }

        public void setTable_id(String table_id) {
            this.table_id.set(table_id);
        }

        public boolean isApproved() {
            return approved.get();
        }

        public SimpleBooleanProperty approvedProperty() {
            return approved;
        }

        public void setApproved(boolean approved) {
            this.approved.set(approved);
        }

        public String getTime() {
            return time.get();
        }

        public SimpleStringProperty timeProperty() {
            return time;
        }

        public void setTime(String time) {
            this.time.set(time);
        }
    }

    TableView<TableNotificationData> tableView;
    Pane buttons;

    public NotificationsController(TableView<TableNotificationData> table, Pane buttons) {
        tableView = table;
        this.buttons = buttons;
        //TODO: fix init to be for notifications
       if (table !=null)
            initRequestsTable();

        Button approve = new Button("approve");
        Button approve_all = new Button("approve all");
        Button delete = new Button("delete");
        Button cancel = new Button("cancel");
        Button save = new Button("save");
        approve.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).setApproved(true);
            }
        });
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ObservableList<TableNotificationData> tableData = tableView.getItems();
                DataHolder.restaurant.get(DataHolder.rest_id).ifPresent(restaurant -> {
                    getListOfRequests(tableData).forEach(request -> {
                        DataHolder.requests.save(request);
                    });
                    DataHolder.restaurant.update(restaurant, null);
                });
            }
        });
        approve_all.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableView.getItems().forEach(item -> {
                    item.setApproved(true);
                });
            }
        });
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableView.getItems().remove(tableView.getSelectionModel().getFocusedIndex());
            }
        });
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                tableView.getItems().get(tableView.getSelectionModel().getFocusedIndex()).setApproved(false);
            }
        });
        buttons.getChildren().addAll(approve, approve_all, delete, cancel, save);

    }

    private ArrayList<Request> getListOfRequests(List<TableNotificationData> data) {
        ArrayList<Request> requests = new ArrayList<Request>();
        for (TableNotificationData request : data) {
            requests.add(request.getRequestObject());
        }
        return requests;
    }

    private void initRequestsTable() {
        // TODO: add data from the dao.
        /*
        ObservableList<TableNotificationData> data = FXCollections.observableList(tableData);
        // set data for begin:
        tableView.setItems(data);
        */

        // set table editable and build it's columns:
        tableView.setEditable(false);
        TableColumn<TableNotificationData, String> tc_client = new TableColumn<TableNotificationData, String>("client id");
        TableColumn<TableNotificationData, String> tc_table = new TableColumn<TableNotificationData, String>("table id");
        TableColumn<TableNotificationData, Boolean> tc_approved = new TableColumn<TableNotificationData, Boolean>("approved");
        TableColumn<TableNotificationData, String> tc_time = new TableColumn<TableNotificationData, String>("time");
        tc_client.setCellValueFactory(cellData -> cellData.getValue().client_id);
        tc_client.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        tc_client.setEditable(false);
        tc_table.setCellValueFactory(cellData -> cellData.getValue().table_id);
        tc_table.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        tc_table.setEditable(false);
        tc_approved.setCellValueFactory(cellData -> cellData.getValue().approved);
        tc_approved.setCellFactory(TextFieldTableCell.forTableColumn(new BooleanStringConverter()));
        tc_approved.setEditable(false);
        tc_time.setCellValueFactory(cellData -> cellData.getValue().table_id);
        tc_time.setCellFactory(TextFieldTableCell.forTableColumn(new DefaultStringConverter()));
        tc_time.setEditable(false);
        tableView.getColumns().addAll(tc_client, tc_table, tc_approved, tc_time);
        tc_client.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
        tc_table.prefWidthProperty().bind(tableView.widthProperty().multiply(0.3));
        tc_approved.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        tc_time.prefWidthProperty().bind(tableView.widthProperty().multiply(0.2));
        tc_client.setResizable(false);
        tc_table.setResizable(false);
        tc_approved.setResizable(false);
        tc_time.setResizable(false);
    }

}

