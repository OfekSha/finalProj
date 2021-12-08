package application.controller.dao;

import application.DataHolder;
import application.controller.FireStoreConnection;
import application.controller.FireStoreListener;
import application.entities.Request;
import application.entities.Restaurant;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class RequestDaoFireStore implements DAO<Request> {
    private FireStoreConnection db = FireStoreConnection.getDB();
    private  FireStoreListener<Request> listener;
    private  ArrayList<Request> tempRequests=new ArrayList<Request>();
    private  LocalTime now=LocalTime.now();
    public void setListener(FireStoreListener listener) {
        this.listener = listener;
    }

    private void automateRequests(){
        Thread ticks=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                        now=LocalTime.now();
                        if (tempRequests!=null)
                            if(!tempRequests.isEmpty())
                        tempRequests.forEach(el->{
                            if (!el.getTime().equals("no time"))
                            if (LocalTime.parse(el.getTime()).isBefore(now.minusHours(2))){ // release table if 2 hours over.
                                el.setApproved(false);
                                update(el,null);
                                Restaurant rest = DataHolder.restaurant.get(DataHolder.rest_id).get();
                                rest.getTables().stream().filter(table->{
                                    return table.getId()==el.getTable_id();
                                }).findFirst().get().getIsFreeByTime().put(el.getTime(),true);
                                DataHolder.restaurant.update(rest,null);
                            }
                        });

                }
            }
        });
        ticks.start();
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
            if (request.isApproved()){
                Restaurant rest = DataHolder.restaurant.get(DataHolder.rest_id).get();
                rest.getTables().stream().filter(table->{
                    return table.getId()==request.getTable_id();
                }).findFirst().get().getIsFreeByTime().put(request.getTime(),false);
                DataHolder.restaurant.update(rest,null);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectLiveData(FireStoreListener listener) {
        db.connectListenerToData(DataHolder.rest_id,"requests", this);
        this.listener = listener;
        automateRequests();
    }

    @Override
    public void update(Request request, String[] params) {
        try {
            db.addDataRes("Restaurants",  DataHolder.rest_id,"requests",request.getClient_id(),request);
            if (request.isApproved()){
                Restaurant rest = DataHolder.restaurant.get(DataHolder.rest_id).get();
                rest.getTables().stream().filter(table->{
                    return table.getId()==request.getTable_id();
                }).findFirst().get().getIsFreeByTime().put(request.getTime(),false);
                DataHolder.restaurant.update(rest,null);
            }
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Request request) {
        //TODO: implement delete
        try {
            db.deleteData("Restaurants",DataHolder.rest_id,"requests",request.getClient_id());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailed() {

    }

    @Override
    public void onDataChanged(Map<String, Object> data) {
        Optional<Request> request=tempRequests.stream().filter(req->{
            return req.getClient_id().equals(data.get("client_id"));
        }).findFirst();
        if (request.isEmpty()) return;
        listener.onDataChanged(db.fromMapToObject(data,request.get()));
    }

    @Override
    public void onDataRemoved(Map<String, Object> data) {
        Optional<Request> request=tempRequests.stream().filter(req->{
            return req.getClient_id().equals(data.get("client_id"));
        }).findFirst();
        if (request.isEmpty()) return;
        listener.onDataRemoved(db.fromMapToObject(data,request.get()));
        tempRequests.remove(request);

    }

    @Override
    public void onDataAdded(Map<String, Object> data) {
        tempRequests.add(new Request());
        listener.onDataAdded(db.fromMapToObject((data),tempRequests.get(tempRequests.size()-1)));
    }
}
