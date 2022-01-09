package application;

import application.controller.BaseFrameController;
import application.controller.dao.RequestDaoFireStore;
import application.controller.dao.clientDaoFireStore;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        initDataBase();
        BaseFrameController root = new BaseFrameController();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);

    }
    private void initDataBase(){
        DataHolder.restaurant = new clientDaoFireStore();
        DataHolder.requests = new RequestDaoFireStore();
    }
}
