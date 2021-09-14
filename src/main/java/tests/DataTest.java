package tests;

import application.entities.REST_TAGS;
import application.entities.Restaurant;
import org.jxmapviewer.viewer.GeoPosition;

import java.util.HashSet;
import java.util.Set;

public class DataTest {
    private static Restaurant rest;
    private static void createRestDataTest(){

        String name="rest name";
        String ownerName="owner name";
        String address="address";
        String phoneNumber="050412354";
        String password="abcdefg";
        GeoPosition position=new GeoPosition(45.4,50.4);
        Set<REST_TAGS> tags=new HashSet();
        int id=0;
        rest= new Restaurant(name,  ownerName,  address,  phoneNumber,  password,  tags,  position,  id);
    }
    public static Restaurant getRestDataTest(){
        if (rest==null) createRestDataTest();
        return rest;
    }
    public static void initRestDataTest(){
        rest=null;
    }
}
