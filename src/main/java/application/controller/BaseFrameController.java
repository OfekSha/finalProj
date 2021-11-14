package application.controller;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class BaseFrameController extends GridPane {
    public static BaseFrameController instance;

    @FXML
    private HBox header;
    @FXML
    private Button btn_back;
    @FXML
    private StackPane include;
    private Node lastNode;

    public Node getLastNode() {
        return lastNode;
    }

    public void changeFrame(String fxmlSource) throws IOException {
        if (!include.getChildren().isEmpty())
        lastNode=include.getChildren().get(0);
        include.getChildren().clear();
        Node node=FXMLLoader.load(getClass().getClassLoader().getResource(fxmlSource));
        resizeNode(node);
        include.getChildren().add(node);
    }
    public void changeFrame(Node node) throws IOException {
        if (!include.getChildren().isEmpty())
        lastNode=include.getChildren().get(0);
        include.getChildren().clear();
        resizeNode(node);
        include.getChildren().add(node);
    }
private void resizeNode(Node node){
    if (node instanceof TabPane){
        TabPane tabPane= (TabPane) node;
        for ( Tab tab:tabPane.getTabs()){
            resizeNode(tab.getContent());
        }

    }
    else if (node instanceof Region){
        Region region=(Region)node ;
        for ( Node child:region.getChildrenUnmodifiable()){
            resizeNode(child);
        }
    }

    if (node instanceof Label){
            Label text = (Label) node;
            DoubleProperty fontSize = new SimpleDoubleProperty(10);
            fontSize.bind(text.widthProperty().add(text.heightProperty()).divide(10));
            fontSize.bind(heightProperty().add(widthProperty()).multiply(0.01));
            node.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
    }
    if (node instanceof TextInputControl){
        TextInputControl text=(TextInputControl) node;
        DoubleProperty fontSize = new SimpleDoubleProperty(10);
        fontSize.bind(text.widthProperty().add(text.heightProperty()).divide(25));
        fontSize.bind(heightProperty().add(widthProperty()).multiply(0.01));
        node.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), ";"));
    }
    if (node instanceof Button){
        Button text = (Button) node;
        DoubleProperty fontSize = new SimpleDoubleProperty(10);
        fontSize.bind(heightProperty().add(widthProperty()).multiply(0.01));
        node.styleProperty().bind(Bindings.concat("-fx-font-size: ", fontSize.asString(), "px;"));
    }

}
    public BaseFrameController(){
        instance=this;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("application/fxml/baseFrame.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
    @FXML
    public void backClicked(ActionEvent e) throws IOException {
        changeFrame(lastNode);
    }

}