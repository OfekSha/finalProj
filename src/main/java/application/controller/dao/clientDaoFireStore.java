package application.controller.dao;

import application.DataHolder;
import application.controller.FireStoreConnection;
import application.controller.FireStoreListener;
import application.controller.tabs.ModelController;
import application.entities.Restaurant;
import application.entities.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class clientDaoFireStore implements DAO<Restaurant> {
    private FireStoreConnection db = FireStoreConnection.getDB();
    private FireStoreListener listener;

    public void setListener(FireStoreListener listener) {
        this.listener = listener;
    }

    @Override
    public Optional<Restaurant> get(String id) {

        try {
            Restaurant restaurant = new Restaurant();
            restaurant = db.getDataById(id, restaurant);
            if (restaurant != null) {
                DataHolder.rest_id = id;
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

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectLiveData(FireStoreListener listener) {
        this.listener = listener;
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
    public void onFailed() {

    }

    @Override
    public void onDataChanged(Map<String, Object> data) {
        if (listener instanceof ModelController){
            listener.onDataChanged(data.get("tables"));
        }
    }

    @Override
    public void onDataRemoved(Map<String, Object> data) {

    }

    @Override
    public void onDataAdded(Map<String, Object> data) {

    }
}
