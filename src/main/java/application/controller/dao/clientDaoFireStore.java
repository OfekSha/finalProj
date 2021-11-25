package application.controller.dao;

import application.DataHolder;
import application.controller.FireStoreConnection;
import application.controller.FireStoreListener;
import application.entities.Restaurant;

import java.util.List;
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
            DataHolder.rest_id = db.addDataRes("Restaurants", restaurant);

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
    public void update(Restaurant restaurant, String[] params) {
        try {
            db.updateData("Restaurants", DataHolder.rest_id, restaurant);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Restaurant restaurant) {

    }
}
