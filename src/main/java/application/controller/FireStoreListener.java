package application.controller;

import java.util.Map;

public interface FireStoreListener {
    public void onFailed();
    public void onDataChanged(Map<String, Object> data);
    public void onDataRemoved(Map<String, Object> data);
    public void onDataAdded(Map<String, Object> data);
}
