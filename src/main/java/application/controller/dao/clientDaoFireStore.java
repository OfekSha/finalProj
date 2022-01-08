package application.controller.dao;

import application.DataHolder;
import application.controller.FireStoreConnection;
import application.controller.FireStoreListener;
import application.controller.tabs.ModelController;
import application.entities.Restaurant;
import application.entities.Table;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class clientDaoFireStore implements DAO<Restaurant> {
    private FireStoreConnection db = FireStoreConnection.getDB();
    private ArrayList <FireStoreListener> listener= new ArrayList();

    @Override
    public Optional<Restaurant> get(String id) {

        try {
            Restaurant restaurant = new Restaurant();
            restaurant = db.getDataById(id, restaurant);
            if (restaurant != null) {
                DataHolder.rest_id = id;
                DataHolder.tempRest=restaurant;
                restaurant.setId(id);
                restaurant.setTables(db.getDataById("tables",id ,"data",new ArrayList<Table>()));
            }
            return Optional.ofNullable(restaurant);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Restaurant> getAll() {
        return null;
    }

    @Override
    public void save(Restaurant restaurant) {
        try {
            DataHolder.rest_id = db.addData(null, restaurant);
            restaurant.setId(DataHolder.rest_id);
            update(restaurant, new boolean[]{true, false});

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectLiveData(FireStoreListener listener) {
        this.listener.add(listener) ;
        db.connectListenerToData(DataHolder.rest_id,"data", this);
    }

    @Override
    public void update(Restaurant restaurant, boolean[] params) {
        // params[0] - update restaurant general data.
        // params[1] - update tables in restaurant.
        if (params==null) params=new boolean[]{true,true};
        if (params[1]){
            restaurant.getTables();
            //db.updateData("Restaurants",DataHolder.rest_id,"tables",restaurant.getTables());
            try {
                db.addData(DataHolder.rest_id,"data","tables", restaurant.getTables());
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            if (params[0])
            db.addData(DataHolder.rest_id, restaurant);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Restaurant restaurant) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onDataChanged(Map<String, Object> data) {
        listener.forEach(listener -> {
            if (listener instanceof ModelController){
                ((ArrayList)data.get("Table")).forEach(tbl->{
                    listener.onDataChanged(db.fromMapToObject((HashMap)tbl,new Table()));
                });

            }
        });

    }

    @Override
    public void onDataRemoved(Map<String, Object> data) {

    }

    @Override
    public void onDataAdded(Map<String, Object> data) {

    }
}
