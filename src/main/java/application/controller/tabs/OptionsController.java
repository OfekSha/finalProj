package application.controller.tabs;

import application.DataHolder;
import application.controller.BaseFrameController;
import application.controller.MapController;
import application.entities.Restaurant;
import gui.controller.ILocationGetter;
import javafx.scene.control.Button;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.jxmapviewer.viewer.GeoPosition;

import java.io.IOException;

public class OptionsController implements ILocationGetter {
    //TODO: need to implement for tabMenu.fxml and get nodes from TabMenuController
    GridPane menu;
    TextField name;
    TextField owner;
    TextField country;
    TextField region;
    TextField adr;
    TextField phone;
    TextField location;
    TextField status;
   public OptionsController(GridPane menu){

       this.menu=menu;
       menu.getChildren().clear();
       Button open = new Button("Open restaurant");
       Button close = new Button("Close restaurant");
       Button save = new Button("Save");
       Restaurant temp_rest = DataHolder.restaurant.get(DataHolder.rest_id).get();
       name=createGridPaneLine("restaurant name:",0,new TextField(),temp_rest.getName());
       owner=createGridPaneLine("owner name:",1,new TextField(),temp_rest.getOwnerName());
       country=createGridPaneLine("country:",2,new TextField());
       region=createGridPaneLine("Region:",3,new TextField());
       adr=createGridPaneLine("Address:",4,new TextField(),temp_rest.getAddress());
       phone=createGridPaneLine("Phone number:",5,new TextField(),temp_rest.getPhoneNumber());
       location=createGridPaneLine("Location:",6,new TextField());
       status=createGridPaneLine("Status:",7,new TextField(),temp_rest.isAvailable()+"");
       status.setEditable(false);
       location.setText(DataHolder.tempRest.getPosition().getLatitude() + " : "+temp_rest.getPosition().getLongitude());
       location.setOnMouseClicked(ev->{
           try {
               BaseFrameController.instance.changeFrame(new MapController(this));
           } catch (IOException e) {
               e.printStackTrace();
           }
       });
       menu.add(open,0,3);
       menu.add(close,0,4);
       menu.add(save,2,8,1,2);
       save.setOnAction(event->{
           Restaurant rest=DataHolder.tempRest;
           rest.setAddress(adr.getText());
           rest.setName(name.getText());
           rest.setOwnerName(owner.getText());
           rest.setPhoneNumber(phone.getText());

           DataHolder.restaurant.update(rest,new boolean[]{true,false});
       });
       open.setOnAction((event)->{
           DataHolder.requests.deleteAll();
           Restaurant rest = DataHolder.restaurant.get(DataHolder.rest_id).get();
           rest.setAvailable(true);
           rest.getTables().forEach(table->{
              table.getIsFreeByTime().replaceAll((time,isFree)->{
                  return true;
           });
       });
           DataHolder.restaurant.update(rest,new boolean[]{true,true});
    });
       close.setOnAction((event)->{
           DataHolder.requests.deleteAll();
           Restaurant rest = DataHolder.restaurant.get(DataHolder.rest_id).get();
           rest.setAvailable(false);
           rest.getTables().forEach(table->{
               table.getIsFreeByTime().replaceAll((time,isFree)->{
                   return true;
               });
           });
           DataHolder.restaurant.update(rest,new boolean[]{true,true});
       });
   }
    private <T extends Control> T createGridPaneLine(String name, int line, T input) {
        Label label = new Label(name);
        menu.add(label, 1, line);
        menu.add(input, 2, line);
        return input;

    }
    private <T extends Control> T createGridPaneLine(String name, int line, T input,String textInput){
        T tf=createGridPaneLine(name,line,input);
        ((TextField)tf).setText(textInput);
        return tf;
    }

    @Override
    public void setLocation(GeoPosition lastLocationSelected) {
        location.setText(lastLocationSelected.getLatitude()+" : "+lastLocationSelected.getLongitude());
        DataHolder.tempRest.setPosition(lastLocationSelected);
    }
}
