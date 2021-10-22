package application.entities;


import org.jxmapviewer.viewer.GeoPosition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Restaurant {
    private String name;
    private String ownerName;
    private String address;
    private String phoneNumber;
    private String password;
    private Set<REST_TAGS> tags;
    private GeoPosition position;
    private int id;
    private ArrayList<Table> tables;
    private int model_height;
    private int model_width;
    private HashSet<Client> employee= new HashSet<Client>();
    public Restaurant(String name, String ownerName, String address, String phoneNumber, String password, Set<REST_TAGS> tags, GeoPosition position, int id) {
        this.name = name;
        this.ownerName = ownerName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.tags = tags;
        this.position = position;
        this.id = id;
    }

    public HashSet<Client> getEmployee() {
        return employee;
    }

    public String getName() {
        return name;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public Set<REST_TAGS> getTags() {
        return tags;
    }

    public GeoPosition getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTags(Set<REST_TAGS> tags) {
        this.tags = tags;
    }

    public void setPosition(GeoPosition position) {
        this.position = position;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Table> getTables() {
        return tables;
    }

    public void setTables(ArrayList<Table> tables) {
        this.tables = tables;
    }

    public int getModel_height() {
        return model_height;
    }

    public void setModel_height(int model_height) {
        this.model_height = model_height;
    }

    public int getModel_width() {
        return model_width;
    }

    public void setModel_width(int model_width) {
        this.model_width = model_width;
    }
}
