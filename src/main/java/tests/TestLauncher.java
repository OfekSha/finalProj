package tests;

import application.App;
import application.DataHolder;
import tests.DAO.restDao;

public class TestLauncher {

    public static void main(String[] args) {
        DataHolder.dao_rest=new restDao();
        App.main(args);
    }
}
