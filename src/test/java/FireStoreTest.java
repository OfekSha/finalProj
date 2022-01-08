import application.DataHolder;
import application.controller.BaseFrameController;
import application.controller.dao.RequestDaoFireStore;
import application.controller.dao.clientDaoFireStore;
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
        //root.changeFrame("application/fxml/entrance.fxml");
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
        DataHolder.requests = new RequestDaoFireStore();
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

}
