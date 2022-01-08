package DAO;

import application.controller.FireStoreListener;
import application.controller.dao.DAO;
import application.entities.Restaurant;
import application.entities.Table;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class restDao implements DAO<Restaurant> {
    Restaurant rest;
    Gson gson;
    public ArrayList<Table> testModel(){
        ArrayList<Table> testTables = new ArrayList<Table>(); //@@@ test only!!!
        testTables.add(new Table(0,4,false,false,0,0));
        testTables.add(new Table(1,3,true,false,2,0));
        testTables.add(new Table(2,5,true,true,2,1));
        testTables.add(new Table(3,10,true,true,0,3));
        testTables.add(new Table(4,2,false,true,4,4));
        testTables.add(new Table(99,9,false,false,3,0));
        return testTables;
    }
    public restDao() {
        this.rest = null;
        gson= new Gson();
    }

    @Override
    public Optional<Restaurant> get(String id) {
        Reader reader = null;
        try {
            reader = Files.newBufferedReader(Paths.get("rest_test_data.json"));
            rest = gson.fromJson(reader,Restaurant.class);
           // if (rest.getId()!=id) rest=null;
        } catch (IOException e) {
            rest=null;
            e.printStackTrace();
        }
        return Optional.ofNullable(rest);

    }
    @Deprecated // restaurant has only one data.
    @Override
    public List<Restaurant> getAll() {
        return null;
    }

    @Override
    public void save(Restaurant restaurant) {
        rest=restaurant;
        if (rest!=null) {
            rest.setId("1");
            rest.setTables(testModel());
            rest.setModel_height(10);
            rest.setModel_width(10);
        }
        /*
        Reader reader = null;
        if (rest.getId()==-1){ // create id is automate in the cloud.
            try {
                reader = Files.newBufferedReader(Paths.get("rest_test_data.json"));
                Restaurant temp = gson.fromJson(reader,Restaurant.class);
                if (temp==null) rest.setId(1);
            } catch (IOException e) {
                rest=null;
                e.printStackTrace();
            }
        }*/
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("rest_test_data.json"));
            gson.toJson(rest,writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectLiveData(FireStoreListener listener) {

    }

    @Override
    public void update(Restaurant restaurant, boolean[] params) {
        rest=restaurant;
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("rest_test_data.json"));
            gson.toJson(rest,writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Restaurant restaurant) {
        rest = null;
        try {
            Writer writer = Files.newBufferedWriter(Paths.get("rest_test_data.json"));
            gson.toJson(rest,writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onDataChanged(Map<String, Object> data) {

    }

    @Override
    public void onDataRemoved(Map<String, Object> data) {

    }

    @Override
    public void onDataAdded(Map<String, Object> data) {

    }
}
