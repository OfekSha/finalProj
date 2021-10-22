package application.controller.tabs;

import application.DataHolder;
import application.entities.Table;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class ModelEditorController {


    private VBox vbox_edit_menu;

    private GridPane modelEditTable;
    private ModelController modelGrid;
    public ModelEditorController(VBox vbox_edit_menu,GridPane modelEditTable){
        modelGrid= new ModelController(modelEditTable);
        List<Table> tables = DataHolder.tables.getAll();
        DataHolder.restaurant.get(DataHolder.rest_id);
        modelGrid.setModel(5,5, setAllTablesAvailable(tables));
    }
    private List<Table> setAllTablesAvailable(List<Table> tables){
        for(Table tbl:tables){
            tbl.setFree(true);
        }
        return tables;

    }
}
