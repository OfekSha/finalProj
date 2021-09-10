package gui.controller;

import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.*;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;

public class MapController extends AnchorPane implements Initializable {
    @FXML
    public SwingNode sn;

    private JFXPanel fxPanel=new JFXPanel();
    private JXMapViewer mapViewer=new JXMapViewer();
    public MapController() {
        super();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(
                "Map.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            // Create a TileFactoryInfo for OpenStreetMap
            TileFactoryInfo info = new OSMTileFactoryInfo();
            DefaultTileFactory tileFactory = new DefaultTileFactory(info);
            mapViewer.setTileFactory(tileFactory);
            // Use 8 threads in parallel to load the tiles
            tileFactory.setThreadPoolSize(8);
            // Set the focus
            GeoPosition frankfurt = new GeoPosition(32.812012,  34.977829);
            mapViewer.setZoom(4);
            mapViewer.setAddressLocation(frankfurt);
            fxPanel = new JFXPanel();
            mapViewer.add(fxPanel);
            // Display the viewer in a JFrame
            sn.setContent(mapViewer);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

    }

    @Override
    public void setPrefSize(double prefWidth, double prefHeight) {
        super.setPrefSize(prefWidth, prefHeight);
        sn.resize(prefWidth,prefHeight);
    }

    @Override
    protected void setHeight(double value) {
        super.setHeight(value);
        sn.resize(super.getWidth(),value);
    }

    @Override
    protected void setWidth(double value) {
        super.setWidth(value);
        sn.resize(value,super.getHeight());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        sn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                GeoPosition mouse;
                mouse= mapViewer.convertPointToGeoPosition(new Point2D.Double(event.getX(),event.getY()));
                mapViewer.setAddressLocation(mouse);
                WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<Waypoint>();
                Waypoint point = new DefaultWaypoint(mouse);
                Set<Waypoint> waypoints=new HashSet<Waypoint>(Arrays.asList(point));

                waypointPainter.setWaypoints(waypoints);
                mapViewer.setOverlayPainter(waypointPainter);

            }
        });
    }
}
