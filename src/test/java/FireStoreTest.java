import application.DataHolder;
import application.controller.BaseFrameController;
import application.controller.FireStoreConnection;
import application.controller.FireStoreListener;
import application.controller.dao.DAO;
import application.entities.Restaurant;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class FireStoreTest  extends ApplicationTest {
    Thread temp;
    FxRobot robot;

    @Override
    public void start(Stage stage) throws Exception {
        robot = new FxRobot();
        BaseFrameController root = new BaseFrameController();
        Scene scene = new Scene(root);
        stage.addEventFilter(KeyEvent.KEY_PRESSED,
                event -> {
                    if (event.getCode().equals(KeyCode.ESCAPE))
                        temp.interrupt();// if click esc in test mode will close the application
                });

        stage.setScene(scene);

        stage.show();
        root.changeFrame("application/fxml/entrance.fxml");
        //stage.setMaximized(true);
        //stage.setFullScreen(true);


    }

    private KeyCode[] getKeyCodes(String name) {
        KeyCode[] keycode = new KeyCode[name.length()];
        for (int i = 0; i < name.length(); i++) {
            keycode[i] = KeyCode.getKeyCode(String.valueOf(name.charAt(i)).toUpperCase());
        }
        return keycode;
    }

    @BeforeEach
    public void setUpClass() throws Exception {
        DataHolder.restaurant = new clientDaoFireStore();
    }

    @Test
    void test1() {
        //sleep(999999999);
        try {

            temp = Thread.currentThread();
            temp.sleep(999999999);
        } catch (InterruptedException e) {
            return;
        }
    }

    @Test
    void test2() {
        try {
            temp = Thread.currentThread();
            temp.sleep(999999999);
        } catch (InterruptedException e) {
            return;
        }
    }

    @Test
    void test3() {
        try {
            temp = Thread.currentThread();
            temp.sleep(999999999);
        } catch (InterruptedException e) {
            return;
        }
    }

    @AfterEach
    public void tearDown() throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    private class clientDaoFireStore implements DAO<Restaurant> {
        private FireStoreConnection db= FireStoreConnection.getDB();
        private FireStoreListener listener;
        public void setListener(FireStoreListener listener) {
            this.listener = listener;
        }
        @Override
        public Optional<Restaurant> get(String id) {

            try {
                Restaurant restaurant=new Restaurant();
                restaurant = db.getDataById(id,restaurant);
                if (restaurant!=null) {
                    DataHolder.rest_id=id;
                    restaurant.setId(id);
                }
                return  Optional.ofNullable(restaurant);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        }

        @Override
        public List<Restaurant> getAll() {
            return null;
        }

        @Override
        public void save(Restaurant restaurant) {
            try {
                DataHolder.rest_id=db.addDataRes("Restaurants", restaurant);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void connectLiveData(FireStoreListener listener) {
            this.listener=listener;
        }

        @Override
        public void update(Restaurant restaurant, String[] params) {
            try {
                db.updateData("Restaurants",DataHolder.rest_id, restaurant);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void delete(Restaurant restaurant) {

        }
    }

}
