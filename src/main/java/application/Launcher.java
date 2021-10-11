package application;

import tests.TestLauncher;

import java.util.Scanner;

public class Launcher {
    final public static int TEST_MODE=1;
    final public static int NOT_TEST_MODE=0;
    public static int mode;
    public static void main(String[] args) {
        mode=NOT_TEST_MODE;
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("is it test mode? (y/n)");
        String answer = myObj.nextLine();  // Read user input
        if (answer.contains("y"))mode= TEST_MODE;
        if (mode==NOT_TEST_MODE){ // not test mode
            DataHolder.dao_rest=null; // set the data fetching for restaurant.
            App.main(args);
        }
        else{ // test mode
            TestLauncher.main(args);
        }
    }
}
