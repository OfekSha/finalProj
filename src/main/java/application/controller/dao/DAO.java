package application.controller.dao;

import application.controller.FireStoreListener;

import java.util.List;
import java.util.Optional;

public interface DAO <T> {

    Optional<T> get(String id);

    List<T> getAll();

    void save(T t);

    void connectLiveData(FireStoreListener listener);

    void update(T t, String[] params);

    void delete(T t);
}