import DAO.restDao;
import application.DataHolder;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;

import static org.testfx.api.FxAssert.verifyThat;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        DataHolder.restaurant = new restDao();
    }
    @Test
    @Order(1)
    void testLoginBtn() throws InterruptedException {
        clickOn("LogIn");
        verifyThat("Login", Node::isVisible);

    }
    @Test
    @Order(1)
    void testRegisterBtn() throws InterruptedException {
        clickOn("Register");
        verifyThat("Register", Node::isVisible);
    }
    @Test
    @Order(1)
    void testRegister() throws InterruptedException, InvalidPathException {
        clickOn("Register");
        clickOn("#tf_res_name").type(getKeyCodes("rest"));
        clickOn("#tf_password").type(getKeyCodes("pass"));
        clickOn("#tf_owner_name").type(getKeyCodes("ofek"));
        clickOn("#tf_address").type(getKeyCodes("haifa"));
        clickOn("#tf_phone").type(getKeyCodes("0504444123"));
        clickOn("Register");
        Paths.get("client_test_data1.json");

    }
    @Test
    @Order(3)
    void testPermissions() throws InterruptedException {
        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("ofek"));
        clickOn(".password").type(getKeyCodes("pass"));
        clickOn("Login");
        clickOn("Permissions");
        sleep(15000);
        //clickOn("name").type(getKeyCodes("sad"));
    }
    @Test
    @Order(4)
    void testPermissionsAfterExit() throws InterruptedException {
        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("ofek"));
        clickOn(".password").type(getKeyCodes("pass"));
        clickOn("Login");
        clickOn("Permissions");

        sleep(15000);
        //clickOn("name").type(getKeyCodes("sad"));
    }

    @Test
    @Order(2)
    void testLogin() throws InterruptedException {

        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("ofek"));
        clickOn(".password").type(getKeyCodes("pass"));
        clickOn("Login");
        sleep(15000);
        verifyThat("Options",Node::isVisible);


    }
    private KeyCode[] getKeyCodes(String name){
        KeyCode[] keycode= new KeyCode[name.length()];
        for (int i=0;i<name.length();i++){
            keycode[i]=KeyCode.getKeyCode(String.valueOf(name.charAt(i)).toUpperCase());
        }
        return keycode;
    }
    @AfterEach
    public void tearDown()throws Exception{
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
}
