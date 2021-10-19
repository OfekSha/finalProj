package application;

import application.controller.dao.DAO;
import application.entities.Client;
import application.entities.Restaurant;
import application.entities.Table;

public class DataHolder {
    public static DAO<Restaurant> restaurant=null;
    public static DAO<Client> clients=null;
    public static DAO<Table> tables=null;
}
