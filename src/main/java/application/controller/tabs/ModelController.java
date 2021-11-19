package application.controller.tabs;

import application.DataHolder;
import application.entities.Table;
import gui.controller.cellController;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ModelController {

    //Tab modelTab;
    GridPane modelTable;
    public ModelController(GridPane modelTable){
        this.modelTable=modelTable;
        List<Table> tables=new ArrayList<Table>();
        AtomicInteger rows= new AtomicInteger();
        AtomicInteger cols=new AtomicInteger();
        DataHolder.restaurant.get(DataHolder.rest_id).ifPresent(restaurant -> {
            tables.addAll(restaurant.getTables());
            cols.set(restaurant.getModel_height());
            rows.set(restaurant.getModel_width());
        });
        setModel(rows.get(),cols.get(), tables);

    }
    public void showTablesByTime(String time){
        DataHolder.restaurant.get(DataHolder.rest_id).ifPresent(restaurant->{
            restaurant.getTables().forEach(table->{
                getCellOfTable(table).setIsAvailable(table.getIsFreeByTime().get(time));
            });
        });
    }
    public ArrayList<Table> getAllTables(){
        ArrayList<Table> tables= new ArrayList<Table>();
        for (Node node:modelTable.getChildren()){
            if (node instanceof cellController){
                cellController cell = (cellController) node;
               if (!cell.isHide()){
                   tables.add(new Table(Integer.parseInt(cell.getTableNumber()),Integer.parseInt(cell.getSeats()),cell.isSmoke(),true,cell.getX(),cell.getY()));
               }
            }
        }
        return tables;
    }
    public void setAllTablesAvailable(boolean on){ // TODO: update the tables in database. (DAO)
        for (Node node:modelTable.getChildren()){
            if (node instanceof cellController){
                cellController cell = (cellController) node;
                if (!cell.isHide()){
                    cell.setIsAvailable(on);
                }
            }
        }
    }
    public cellController createCellOfTable(Table table) {
        if (table == null) {
            return null;
        }
        cellController cell = new cellController(table.getX(), table.getY());
        cell.setSeats("" + table.getSeats());
        cell.setTableNumber("" + table.getId());
        cell.setIsSmoke(table.isSmoke());
        cell.setIsAvailable(table.isFree());
        return cell;
    }
    public cellController getCellOfTable(Table table){
        cellController result = null;
            ObservableList<Node> children = modelTable.getChildren();
            for (Node node : children) {
                if (!(node instanceof cellController))continue;
                if(modelTable.getRowIndex(node) == table.getY() && modelTable.getColumnIndex(node) == table.getX()) {
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
        cellController[][] cells=new cellController[x][y];
        for (int i=0;i<x;i++){
            for (int j=0;j<y;j++){
                modelTable.add(cells[i][j] = new cellController(i,j),i,j);
                cells[i][j].setHide(true);
            }
        }
        for (Table table:tables){
            cellController cell = cells[table.getX()][table.getY()];
            cell.setHide(false);
            cell.setIsAvailable(table.isFree());
            cell.setIsSmoke(table.isSmoke());
            cell.setTableNumber(table.getId()+"");
            cell.setSeats(table.getSeats()+"");
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
