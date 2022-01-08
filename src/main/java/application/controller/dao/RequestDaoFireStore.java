package application.controller.dao;

import application.DataHolder;
import application.controller.FireStoreConnection;
import application.controller.FireStoreListener;
import application.entities.Request;
import application.entities.Restaurant;
import application.entities.Table;

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
    private Thread backgroundTimerThread;
    private  LocalTime now=LocalTime.now();
    private void automateRequests(boolean on){
        if (backgroundTimerThread!=null) backgroundTimerThread.interrupt();
        if (!on) {
            backgroundTimerThread=null;
            return;
        }
        backgroundTimerThread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(!Thread.currentThread().isInterrupted()){
                    try {
                        Thread.sleep(10000);
                        now = LocalTime.now();
                        if (tempRequests != null)
                            if (!tempRequests.isEmpty()) {
                                Restaurant rest = DataHolder.restaurant.get(DataHolder.rest_id).get();
                                ArrayList<Table> tableWithOutRequests= (ArrayList<Table>) rest.getTables().clone();
                                tempRequests.forEach(el -> {
                                    if (!el.getTime().equals("no time")) {
                                        Table tbl = tableWithOutRequests.stream().filter(table -> {
                                            return table.getId() == el.getTable_id();
                                        }).findFirst().get();
                                        tableWithOutRequests.remove(tbl);
                                        if (LocalTime.parse(el.getTime()).isBefore(now.minusHours(2))) { // release table if 2 hours over.
                                            el.setApproved(false);
                                            update(el, null);
                                            tbl.getIsFreeByTime().put(el.getTime(), true);
                                        }
                                    }
                                });
                                tableWithOutRequests.forEach(tblWithOutRequest->{
                                    tblWithOutRequest.setFreeToAllTime(true);
                                    tblWithOutRequest.setFree(true);
                                });
                                DataHolder.restaurant.update(rest, new boolean[]{false,true});
                            }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        });
        backgroundTimerThread.start();
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
       update(request,null);
    }

    @Override
    public void connectLiveData(FireStoreListener listener) {
        this.listener = listener;
        db.connectListenerToData(DataHolder.rest_id,"requests", this);
        automateRequests(DataHolder.isAutoApprove);
    }

    @Override
    public void update(Request request, boolean[] params) {
        try {
            LocalTime requestTime = LocalTime.parse(request.getTime());
            int hour=requestTime.getHour();
            int minute=requestTime.getMinute();
            if (minute>=0 && minute<=15) minute= 0;
            else if (minute>15 && minute<=30) minute= 30;
            else if (minute>30 && minute<=45) minute= 30;
            else if (minute>45 && minute<=59) {
                hour++;
                minute= 0;
            }
            requestTime= LocalTime.of(hour,minute);
            request.setTime(requestTime.toString());
            db.addData(DataHolder.rest_id, "requests", request.getClient_id(), request);
            Restaurant rest = DataHolder.restaurant.get(DataHolder.rest_id).get();
            rest.getTables().stream().filter(table -> {
                return table.getId() == request.getTable_id();
            }).findFirst().get().getIsFreeByTime().put(request.getTime(), !request.isApproved());
            DataHolder.restaurant.update(rest, new boolean[]{false, true});
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
            db.deleteData(DataHolder.rest_id,"requests",request.getClient_id());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {
        db.deleteAllDocs(DataHolder.rest_id,"requests");
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
