package application.controller.tabs;

import application.entities.Table;
import gui.controller.cellController;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;

public class ModelController {
    public void testModel(){
        ArrayList<Table> testTables = new ArrayList<Table>(); //@@@ test only!!!
        testTables.add(new Table(0,4,false,false,0,0));
        testTables.add(new Table(1,3,true,false,2,0));
        testTables.add(new Table(2,5,true,true,2,1));
        testTables.add(new Table(3,10,true,true,0,3));
        testTables.add(new Table(4,2,false,true,4,4));
        testTables.add(new Table(99,9,false,false,3,0));
        setModel(10,10,testTables);
    }
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
    public void  setModel(int x , int y, List<Table> tables){
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
