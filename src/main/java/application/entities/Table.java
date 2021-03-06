package application.entities;

import application.controller.XMLReader;

import java.util.HashMap;

public class Table {
    private int id=0; // id of table.
    private int seats=0; // number of seats.
    private boolean smoke=false; // is in smoking zone.
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
        this.smoke = isSmoke;
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
    public void setFreeToAllTime(boolean free){
        if(free) isFree=true;
        isFreeByTime.replaceAll((k,v)->{
            return free;
        });
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
        return smoke;
    }

    public void setSmoke(boolean smoke) {
        this.smoke = smoke;
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

    @Override
    public String toString() {
        return ""+id;
    }
}
