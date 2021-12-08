package application.controller;

public interface FireStoreListener <T>{
    public void onFailed();
    public void onDataChanged(T data);
    public void onDataRemoved(T data);
    public void onDataAdded(T data);
}
