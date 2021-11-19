package application.entities;

import application.controller.XMLReader;

import java.util.HashMap;

public class Table {
    private int id=0; // id of table.
    private int seats=0; // number of seats.
    private boolean isSmoke=false; // is in smoking zone.
    private boolean isFree=false; // is free to order.
    private HashMap<String,Boolean> isFreeByTime= new HashMap<String,Boolean>();
    private int x=0,y=0; // place in the model.
    public Table() {
        // read from xml file the time of orders.
        XMLReader.getTimeArray().forEach(el->{
            isFreeByTime.put(el,false);
        });
    }
    public Table(int id, int seats, boolean isSmoke, boolean isFree, int x, int y) {
        this();
        this.id = id;
        this.seats = seats;
        this.isSmoke = isSmoke;
        this.isFree = isFree;
        this.x = x;
        this.y = y;
    }

    public HashMap<String, Boolean> getIsFreeByTime() {
        return isFreeByTime;
    }

    public void setIsFreeByTime(HashMap<String, Boolean> isFreeByTime) {
        this.isFreeByTime = isFreeByTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public boolean isSmoke() {
        return isSmoke;
    }

    public void setSmoke(boolean smoke) {
        isSmoke = smoke;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
