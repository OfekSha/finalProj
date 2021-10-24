package application.controller.tabs;

import application.DataHolder;
import gui.controller.cellController;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.Optional;

public class ModelEditorController {


    private VBox vbox_edit_menu;

    private GridPane modelEditTable;
    private ModelController modelGrid;
    public ModelEditorController(VBox vbox_edit_menu,GridPane modelEditTable){
        modelGrid= new ModelController(modelEditTable);
        modelGrid.setAllTablesAvailable(true);
        Button save=new Button("save");
        Button add=new Button("add");
        Button delete=new Button("delete");
        Button edit=new Button("edit");
        save.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                DataHolder.restaurant.get(DataHolder.rest_id).ifPresent(restaurant -> {
                    restaurant.getTables().addAll(modelGrid.getAllTables());
                    DataHolder.restaurant.update(restaurant,null);
                });

            }
        });
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cellController cell = cellController.getSelectedCell();
                cell.setHide(false);
            }
        });
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cellController cell = cellController.getSelectedCell();
                cell.setHide(true);
            }
        });
        edit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                cellController cell = cellController.getSelectedCell();
                if (cell.isHide()) return;
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Edit Table");
                GridPane gridPane = new GridPane();
                gridPane.setHgap(10);
                gridPane.setVgap(10);
                gridPane.setPadding(new Insets(20, 150, 10, 10));
                TextField table = new TextField();
                TextField seats = new TextField();
                CheckBox isSmoke= new CheckBox();
                gridPane.add(new Label("table id:"), 0, 0);
                gridPane.add(table, 1, 0);
                gridPane.add(new Label("seats:"), 0, 1);
                gridPane.add(seats, 1, 1);
                gridPane.add(new Label("smoke zone"), 1, 2);
                gridPane.add(isSmoke, 0, 2);
                alert.getDialogPane().setContent(gridPane);

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    cell.setTableNumber(table.getText());
                    cell.setSeats(seats.getText());
                    cell.setIsSmoke(isSmoke.isSelected());
                } else {
                    // ... user chose CANCEL or closed the dialog
                }
            }
        });
        vbox_edit_menu.getChildren().addAll(save,add,delete,edit);
    }

}
