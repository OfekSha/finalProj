import DAO.restDao;
import application.DataHolder;
import application.controller.BaseFrameController;
import application.controller.tabs.PermissionsController;
import application.entities.Permission;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
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
import org.testfx.util.WaitForAsyncUtils;

import java.util.concurrent.TimeoutException;

public class ManualTest extends ApplicationTest {
    Thread temp;
    Runnable t;
    FxRobot robot;
    @Override
    public void start(Stage stage) throws Exception {
         robot=new FxRobot();
        BaseFrameController root = new BaseFrameController();
        Scene scene = new Scene(root);
        stage.addEventFilter(KeyEvent.KEY_PRESSED,
                event -> {
            if (event.getCode().equals(KeyCode.ESCAPE))temp.interrupt();// if click esc in test mode will close the application
                });

        stage.setScene(scene);

        stage.show();
        root.changeFrame("application/fxml/entrance.fxml");
        stage.setMaximized(true);
        stage.setFullScreen(true);




    }
    private KeyCode[] getKeyCodes(String name){
        KeyCode[] keycode= new KeyCode[name.length()];
        for (int i=0;i<name.length();i++){
            keycode[i]=KeyCode.getKeyCode(String.valueOf(name.charAt(i)).toUpperCase());
        }
        return keycode;
    }
    void testPermissions() throws InterruptedException, TimeoutException {

        clickOn("LogIn");
        WaitForAsyncUtils.async(()->{

            WaitForAsyncUtils.waitForFxEvents();
            clickOn(".id").type(KeyCode.DIGIT1);
            clickOn(".name").type(getKeyCodes("ofek"));
            clickOn(".password").type(getKeyCodes("pass"));
            clickOn("Login");
            WaitForAsyncUtils.waitForFxEvents();
            clickOn("Permissions");
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
        });

    }
    @BeforeEach
    public void setUpClass() throws Exception {
        DataHolder.restaurant = new restDao();
    }
    @Test
    void test1()  {
        //sleep(999999999);
        try {

            temp=Thread.currentThread();
            temp.sleep(999999999);
        } catch (InterruptedException e) {
            return;
        }
    }
    @Test
    void test2()  {
        try {
            temp=Thread.currentThread();
            temp.sleep(999999999);
        } catch (InterruptedException e) {
            return;
        }
    }
    @Test
    void test3()  {
        try {
            temp=Thread.currentThread();
            temp.sleep(999999999);
        } catch (InterruptedException e) {
            return;
        }
    }
    @AfterEach
    public void tearDown()throws Exception{
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }
}
