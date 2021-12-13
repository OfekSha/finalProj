package application.controller.dao;

import application.controller.FireStoreListener;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DAO <T> {

    Optional<T> get(String id);

    List<T> getAll();

    void save(T t);

    void connectLiveData(FireStoreListener listener);

    void update(T t, boolean[] params);

    void delete(T t);

    // connect live to the data change:
    public void onFailed();
    public void onDataChanged(Map<String, Object> data);
    public void onDataRemoved(Map<String, Object> data);
    public void onDataAdded(Map<String, Object> data);
}