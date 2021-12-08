package gui.controller;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.StringProperty;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.IOException;

public class cellController extends VBox {
    private static cellController selectedCell;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static cellController getSelectedCell() {
        return selectedCell;
    }
    final private int x,y;
    public static void setSelectedCell(cellController selectedCell) {
        cellController.selectedCell = selectedCell;
    }
    PseudoClass red = PseudoClass.getPseudoClass("red");
    PseudoClass green = PseudoClass.getPseudoClass("green");
    @FXML
    private Label smoke;
    private BooleanProperty isSmoke;
    private BooleanProperty isAvailable;
    @FXML
    private Label seats;
    @FXML
    private Label seats_text;
    private double size = 12;
    private static final int MAX_WIDTH=100;

    @FXML
    private Label table;
    private boolean hide;

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
        smoke.setVisible(!hide && isSmoke.get());
        seats.setVisible(!hide);
        seats_text.setVisible(!hide);
        table.setVisible(!hide);
    }

    public String getTableNumber() {
        return tableNumberProperty().get().split("#")[1];
    }

    public void setTableNumber(String value) {

        tableNumberProperty().set("#"+value);
    }

    public StringProperty tableNumberProperty() {

        return table.textProperty();
    }

    public String getSeats() {
        //return seatsProperty().get().split("\n")[1];
        return seatsProperty().get();
    }

    public void setSeats(String value) {
       // seatsProperty().set("seats:\n"+value);
        seatsProperty().set(""+value);
        setFontSize();
    }
    public StringProperty seatsProperty() {
        return seats_text.textProperty();
    }
    public boolean isSmoke() {
        return isSmokeProperty().get();
    }

    public void setIsSmoke(boolean value) {
        isSmokeProperty().set(value);
        smoke.setVisible(value);
    }

    public BooleanProperty isSmokeProperty() {
        return isSmoke;
    }

    public boolean isAvailable() {
        return isAvailableProperty().get();
    }

    public void setIsAvailable(boolean value) {
        isAvailableProperty().set(value);
    }
    private void changeStyle(String oldStyle, String newStyle,Node... nodes){
        PseudoClass old = PseudoClass.getPseudoClass(oldStyle);
        PseudoClass now = PseudoClass.getPseudoClass(newStyle);
        for (Node node:nodes) {
            node.pseudoClassStateChanged(old,false);
            node.pseudoClassStateChanged(now,true);
        }
    }

    public BooleanProperty isAvailableProperty() {
        return isAvailable;
    }
    private void setFontSize(){

        seats_text.setFont(Font.font ("Arial", size));
        // java 7 =>
        //    text.snapshot(null, null);
        // java 8 =>
        seats_text.applyCss();

        double width = seats_text.getLayoutBounds().getWidth();

        if(width > MAX_WIDTH){
            size = size - 0.25;
            setFontSize();
        }
    }
    public cellController(int x, int y) {
        this.x=x;
        this.y=y;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("application/fxml/cell.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            isSmoke=new SimpleBooleanProperty(false);
            smoke.setText("");
            smoke.setVisible(false);
            isAvailable=new SimpleBooleanProperty(false);
            table.pseudoClassStateChanged(red, true);
            table.pseudoClassStateChanged(green, false);
            seats.pseudoClassStateChanged(red, true);
            seats.pseudoClassStateChanged(green, false);
            isAvailable.addListener((obj, old, now) -> {

                //changeStyle(now ? "red": "green",now ? "green": "red",table,seats)
                table.pseudoClassStateChanged(red, !now);
                table.pseudoClassStateChanged(green, now);
                seats.pseudoClassStateChanged(red, !now);
                seats.pseudoClassStateChanged(green, now);

                    }
            );
            setSeats("0");
            setTableNumber("0");
            //setFontSize();
            setOnMouseClicked(event -> {
                if (selectedCell!=null){
                    selectedCell.getStyleClass().remove("selected");
                }
                selectedCell=this;
                selectedCell.getStyleClass().add("selected");
            });
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }
}