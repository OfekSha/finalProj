import DAO.restDao;
import application.DataHolder;
import application.controller.tabs.PermissionsController;
import application.entities.Permission;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationTest;

import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Set;

import static org.testfx.api.FxAssert.verifyThat;
@Disabled
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppTest extends ApplicationTest {
private final int waitTime=2000;
private final String modelViewTab="Model";
private final String chatTab="Chat";
private final String notificationTab="Notifications";
private final String modelEditTab="Model Edit";
private final String permissionTab="Permissions";
private final String optionsTab="Options";
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = null;
        root = FXMLLoader.load(getClass().getResource("/application/fxml/entrance.fxml"));
        Scene scene = new Scene(root, 1000, 1000);

        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
        stage.setFullScreen(true);
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
        sleep(waitTime);

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
        sleep(waitTime);
        clickOn(lookup("#permissions_buttons").lookup("add").queryButton());
        clickOn(lookup("#permissions_buttons").lookup("add").queryButton());
        clickOn(lookup("#permissions_buttons").lookup("add").queryButton());
        clickOn(lookup("#permissions_buttons").lookup("add").queryButton());
        clickOn(lookup("#permissions_buttons").lookup("add").queryButton());
        TableView table = lookup("#permission_table").queryTableView();
        PermissionsController.TableClientData data= (PermissionsController.TableClientData) table.getItems().get(1);
        doubleClickOn(data.getName()).type(getKeyCodes("test")).type(KeyCode.ENTER);
        doubleClickOn(data.getPassword()).type(getKeyCodes("test2")).type(KeyCode.ENTER);
        doubleClickOn(data.getPermission()).clickOn(Permission.Hostess.name());
        data= (PermissionsController.TableClientData) table.getItems().get(2);
        doubleClickOn(data.getName()).type(getKeyCodes("test3")).type(KeyCode.ENTER);
        doubleClickOn(data.getPassword()).type(getKeyCodes("test4")).type(KeyCode.ENTER);
        doubleClickOn(data.getPermission()).clickOn(Permission.Waiter.name());
        data= (PermissionsController.TableClientData) table.getItems().get(3);
        doubleClickOn(data.getName()).type(getKeyCodes("test5")).type(KeyCode.ENTER);
        doubleClickOn(data.getPassword()).type(getKeyCodes("test6")).type(KeyCode.ENTER);
        doubleClickOn(data.getPermission()).clickOn(Permission.Manager.name());
        data= (PermissionsController.TableClientData) table.getItems().get(4);
        doubleClickOn(data.getName()).type(getKeyCodes("test7")).type(KeyCode.ENTER);
        doubleClickOn(data.getPassword()).type(getKeyCodes("test8")).type(KeyCode.ENTER);
        doubleClickOn(data.getPermission());
        Node temp = lookup(Permission.Owner.name()).lookup(".list-cell").query();
        clickOn(temp);
        data= (PermissionsController.TableClientData) table.getItems().get(5);
        doubleClickOn(data.getName()).type(getKeyCodes("test9")).type(KeyCode.ENTER);
        doubleClickOn(data.getPassword()).type(getKeyCodes("test10")).type(KeyCode.ENTER);
        doubleClickOn(data.getPermission()).clickOn(Permission.User.name());
        clickOn(lookup("#permissions_buttons").lookup("save").queryButton());
        sleep(waitTime);
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
        sleep(waitTime);
    }
    @Test
    @Order(3)
    void testEditModel() throws InterruptedException {
        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("ofek"));
        clickOn(".password").type(getKeyCodes("pass"));
        clickOn("Login");
        clickOn(modelEditTab);
        GridPane model=lookup("#gridPane_model_editor").queryAs(GridPane.class);
        clickOn(model.getChildren().get(0));
        clickOn(lookup("#vbox_edit_menu").lookup("add").queryButton()).clickOn("edit");
        Set<TextField> inputs = lookup(".dialog-pane").lookup(".text-field").queryAllAs(TextField.class);
        Iterator<TextField> it = inputs.iterator();
       while(it.hasNext()){
           clickOn(it.next()).type(KeyCode.DIGIT1);
       }
        clickOn(lookup(".dialog-pane").lookup(ButtonType.OK.getText()).queryButton());
        clickOn(lookup("#vbox_edit_menu").lookup("save").queryButton());
        sleep(waitTime);

    }
    @Order(4)
    void testEditModelAfterExit() throws InterruptedException {
        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("ofek"));
        clickOn(".password").type(getKeyCodes("pass"));
        clickOn("Login");
        clickOn(modelEditTab);
        sleep(waitTime);

    }
    @Test
    @Order(5)
    void testHostess() throws InterruptedException {
        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("test"));
        clickOn(".password").type(getKeyCodes("test2"));
        clickOn("Login");
        verifyThat(modelViewTab,Node::isVisible);
        verifyThat(chatTab,Node::isVisible);
        verifyThat(notificationTab,Node::isVisible);
        Assertions.assertTrue(lookup(modelEditTab).tryQuery().isEmpty());
        Assertions.assertTrue(lookup(optionsTab).tryQuery().isEmpty());
        Assertions.assertTrue(lookup(permissionTab).tryQuery().isEmpty());
        sleep(waitTime);

    }
    @Test
    @Order(5)
    void testWaiter() throws InterruptedException {
        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("test3"));
        clickOn(".password").type(getKeyCodes("test4"));
        clickOn("Login");
        verifyThat(modelViewTab,Node::isVisible);
        Assertions.assertTrue(lookup(modelEditTab).tryQuery().isEmpty());
        Assertions.assertTrue(lookup(optionsTab).tryQuery().isEmpty());
        Assertions.assertTrue(lookup(permissionTab).tryQuery().isEmpty());
        Assertions.assertTrue(lookup(chatTab).tryQuery().isEmpty());
        Assertions.assertTrue(lookup(notificationTab).tryQuery().isEmpty());
        sleep(waitTime);
    }
    @Test
    @Order(5)
    void testManager() throws InterruptedException {
        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("test5"));
        clickOn(".password").type(getKeyCodes("test6"));
        clickOn("Login");
        verifyThat(modelViewTab,Node::isVisible);
        verifyThat(chatTab,Node::isVisible);
        verifyThat(optionsTab,Node::isVisible);
        verifyThat(notificationTab,Node::isVisible);
        Assertions.assertTrue(lookup(modelEditTab).tryQuery().isEmpty());
        Assertions.assertTrue(lookup(permissionTab).tryQuery().isEmpty());
        sleep(waitTime);
    }
    @Test
    @Order(5)
    void testUser() throws InterruptedException {
        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("test9"));
        clickOn(".password").type(getKeyCodes("test10"));
        clickOn("Login");
        Assertions.assertTrue(lookup(modelEditTab).tryQuery().isEmpty());
        Assertions.assertTrue(lookup(permissionTab).tryQuery().isEmpty());
        Assertions.assertTrue(lookup(modelViewTab).tryQuery().isEmpty());
        Assertions.assertTrue(lookup(chatTab).tryQuery().isEmpty());
        Assertions.assertTrue(lookup(optionsTab).tryQuery().isEmpty());
        Assertions.assertTrue(lookup(notificationTab).tryQuery().isEmpty());
        sleep(waitTime);
    }
    @Test
    @Order(5)
    void testOwner() throws InterruptedException {

        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("test7"));
        clickOn(".password").type(getKeyCodes("test8"));
        clickOn("Login");
        sleep(waitTime);
        verifyThat(modelViewTab,Node::isVisible);
        verifyThat(chatTab,Node::isVisible);
        verifyThat(notificationTab,Node::isVisible);
        verifyThat(optionsTab,Node::isVisible);
        verifyThat(modelEditTab,Node::isVisible);
        verifyThat(permissionTab,Node::isVisible);


    }
    @Test
    @Order(2)
    void testLogin() throws InterruptedException {

        clickOn("LogIn");
        clickOn(".id").type(KeyCode.DIGIT1);
        clickOn(".name").type(getKeyCodes("ofek"));
        clickOn(".password").type(getKeyCodes("pass"));
        clickOn("Login");
        verifyThat(modelViewTab,Node::isVisible);
        verifyThat(chatTab,Node::isVisible);
        verifyThat(notificationTab,Node::isVisible);
        verifyThat(optionsTab,Node::isVisible);
        verifyThat(modelEditTab,Node::isVisible);
        verifyThat(permissionTab,Node::isVisible);


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
