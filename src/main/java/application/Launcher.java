package application;

import application.controller.dao.DAO;
import application.entities.Restaurant;
import tests.DAO.restDao;
import tests.TestLauncher;

import java.util.Scanner;

public class Launcher {
    final public static int TEST_MODE=1;
    final public static int NOT_TEST_MODE=0;
    public static int mode;
    public static DAO<Restaurant> dao_rest;
    public static void main(String[] args) {
        mode=NOT_TEST_MODE;
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("is it test mode? (y/n)");
        String answer = myObj.nextLine();  // Read user input
        if (answer.contains("y"))mode= TEST_MODE;
        if (mode==NOT_TEST_MODE){ // not test mode
            App.main(args);
            dao_rest=null;
        }
        else{ // test mode
            dao_rest=new restDao();
            TestLauncher.main(args);
        }
    }
}
