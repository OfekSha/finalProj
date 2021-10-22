import DAO.restDao;
import application.DataHolder;
import application.entities.REST_TAGS;
import application.entities.Restaurant;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.jxmapviewer.viewer.GeoPosition;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.HashSet;
import java.util.Set;

import static org.testfx.api.FxAssert.verifyThat;

public class AppTest extends ApplicationTest {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("/application/fxml/entrance.fxml"));
        Scene scene = new Scene(root, 1000, 1000);
        stage.setScene(scene);
        stage.show();
    }

    @BeforeEach
    public void setUpClass() throws Exception {
        String name = "asd";
        String ownerName = "owner name";
        String address = "address";
        String phoneNumber = "050412354";
        String password = "sad";
        GeoPosition position = new GeoPosition(45.4, 50.4);
        Set<REST_TAGS> tags = new HashSet();
        int id = 1;
        DataHolder.restaurant = new restDao();
        DataHolder.restaurant.save(new Restaurant(name, ownerName, address, phoneNumber, password, tags, position, id));
    }

    @Test
    void testLoginBtn() throws InterruptedException {
        clickOn("LogIn");
        verifyThat("Login", Node::isVisible);

    }
    @Test
    void testPermissions() throws InterruptedException {
        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("asd"));
        clickOn(".password").type(getKeyCodes("sad"));
        clickOn("Login");
        clickOn("Permissions");
        sleep(20000);
        clickOn("name").type(getKeyCodes("sad"));
    }
    private KeyCode[] getKeyCodes(String name){
        KeyCode[] keycode= new KeyCode[name.length()];
        for (int i=0;i<name.length();i++){
            keycode[i]=KeyCode.getKeyCode(String.valueOf(name.charAt(i)).toUpperCase());
        }
        return keycode;
    }
    @Test
    void testLogin() throws InterruptedException {

        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("asd"));
        clickOn(".password").type(getKeyCodes("sad"));
        clickOn("Login");
        verifyThat("Options",Node::isVisible);

    }
    @Test
    void testRegisterBtn() throws InterruptedException {
        clickOn("Register");
        verifyThat("Register", Node::isVisible);
    }
    @AfterEach
    public void tearDown()throws Exception{
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
}
