package application.entities;

import org.jxmapviewer.viewer.GeoPosition;

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
}
