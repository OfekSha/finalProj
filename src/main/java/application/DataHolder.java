package application;

import application.controller.dao.DAO;
import application.entities.Client;
import application.entities.Request;
import application.entities.Restaurant;
import application.entities.Table;

public class DataHolder {

    public static DAO<Restaurant> restaurant = null;
    public static DAO<Client> clients = null;
    public static DAO<Table> tables = null;
    public static Client myUser = null;
    public static String rest_id = "0";
    public static Restaurant tempRest = null;
    public static DAO<Request> requests = null;
    public static boolean isAutoApprove = false;
    public static String version="1";
}