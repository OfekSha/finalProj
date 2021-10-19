package application.controller;

import application.entities.Table;
import gui.controller.cellController;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;

public class ModelController {
    //Tab modelTab;
    GridPane modelTable;
    public ModelController(GridPane modelTable){
        this.modelTable=modelTable;
    }
    private cellController createCellOfTable(Table table){
        if (table==null) return null;
        cellController cell= new cellController();
        cell.setSeats(""+table.getSeats());
        cell.setTableNumber(""+table.getId());
        cell.setIsSmoke(table.isSmoke());
        cell.setIsAvailable(table.isFree());
        return cell;
    }
    private cellController getCellOfTable(Table table){
        cellController result = null;
            ObservableList<Node> childrens = modelTable.getChildren();
            for (Node node : childrens) {
                if (!(node instanceof cellController))continue;
                if(modelTable.getRowIndex(node) == table.getX() && modelTable.getColumnIndex(node) == table.getY()) {
                    result =(cellController) node;
                    break;
                }
            }
            return result;
    }
    public void  setModel(int x , int y, ArrayList<Table> tables){
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
            modelTable.add(createCellOfTable(table),table.getY(),table.getX());// if table == null throw exception

        }
    }
    public void setCell(int x , int y, Table table){
        cellController cell = getCellOfTable(table);
        if (cell==null){
            cell=createCellOfTable(table); // if table == null throw exception
            modelTable.add(cell,table.getY(),table.getX());
        }
        else {
            if (table==null) modelTable.getChildren().remove(cell);
            else {
                cell.setSeats("" + table.getSeats());
                cell.setTableNumber("" + table.getId());
                cell.setIsSmoke(table.isSmoke());
                cell.setIsAvailable(table.isFree());
            }
        }
    }

}
