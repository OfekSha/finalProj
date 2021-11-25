package application.controller.dao;

import application.DataHolder;
import application.controller.FireStoreConnection;
import application.controller.FireStoreListener;
import application.entities.Request;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class RequestDaoFireStore implements DAO<Request> {
    private FireStoreConnection db = FireStoreConnection.getDB();
    private FireStoreListener listener;
    public void setListener(FireStoreListener listener) {
        this.listener = listener;
    }

    @Override
    public Optional<Request> get(String id) {

        try {
            Request request = new Request();
            request = db.getDataById(id, DataHolder.rest_id,"requests",request);
            if (request != null) {
            }
            return Optional.ofNullable(request);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Request> getAll() {
        List<Request> requests=null;
        try {
            Request request = new Request();
            requests = db.getAllData(DataHolder.rest_id, "requests", request);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public void save(Request request) {
        try {
            db.addDataRes("Restaurants",  DataHolder.rest_id,"requests",request.getClient_id(),request);

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
    public void update(Request request, String[] params) {
        try {
            db.addDataRes("Restaurants",  DataHolder.rest_id,"requests",request.getClient_id(),request);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Request request) {

    }
}
