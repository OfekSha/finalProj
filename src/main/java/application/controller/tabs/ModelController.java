package application.controller.tabs;

import application.DataHolder;
import application.controller.FireStoreListener;
import application.controller.XMLReader;
import application.entities.Request;
import application.entities.Table;
import gui.controller.cellController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ModelController implements FireStoreListener<Table> {

    //Tab modelTab;
    GridPane modelTable;
    private static String time;
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
        DataHolder.restaurant.connectLiveData(this);

    }
    public ModelController(GridPane modelTable, Pane buttons) {
        this(modelTable);
        Button order=new Button("Order");
        order.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                cellController cell=cellController.getSelectedCell();
                //TODO: check this not happen twice when reading data.
                cell.setIsAvailable(false);
                DataHolder.requests.save(new Request("host "+cell.getTableNumber(), Long.parseLong(cell.getTableNumber()), LocalTime.now().toString(), true));
            }
        });
        Button cancel=new Button("Cancel");
        cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                cellController cell=cellController.getSelectedCell();
                DataHolder.requests.get("host "+cell.getId()).ifPresent(request->{
                    //TODO: check this not happen twice when reading data.
                    cell.setIsAvailable(true);
                    DataHolder.requests.save(new Request("host "+cell.getTableNumber(), Long.parseLong(cell.getTableNumber()), LocalTime.now().toString(), false));
                });

            }
        });
        MenuButton mb_time=new MenuButton("Time");
        mb_time.getItems().clear();
        XMLReader.getTimeArray().forEach(el->{
            MenuItem item = new MenuItem(el);
            item.setOnAction(event->{
                time=el;
                showTablesByTime(el);

            });
            mb_time.getItems().add(item);
        });
        buttons.getChildren().addAll(mb_time,order,cancel);
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
                   try {
                       tables.add(new Table(Integer.parseInt(cell.getTableNumber()), Integer.parseInt(cell.getSeats()), cell.isSmoke(), true, cell.getX(), cell.getY()));
                   }
                   catch (NumberFormatException e){
                       e.printStackTrace();
                   }
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

    @Override
    public void onFailed() {

    }

    @Override
    public void onDataChanged(Table data) {
        cellController cell = getCellOfTable(data);
        if (cell==null){
            throw new IndexOutOfBoundsException("cellController not exist in x= " + data.getX()+ " y= "+data.getY());
        }else {
            cell.setTableNumber(String.valueOf(data.getId()));
            cell.setIsSmoke(data.isSmoke());
            cell.setSeats(String.valueOf(data.getSeats()));
            cell.setHide(false);
            cell.setIsAvailable(data.getIsFreeByTime().get(time));
        }
    }

    @Override
    public void onDataRemoved(Table data) {

    }

    @Override
    public void onDataAdded(Table data) {
        onDataChanged(data);
    }
}
