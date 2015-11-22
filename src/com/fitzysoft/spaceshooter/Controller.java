package com.fitzysoft.spaceshooter;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;

// todo: Add the ship and enemies programmatically, it doesn't need to be defined in the xml, no benefit
//

public class Controller implements Initializable {

    @FXML //  fx:id="mainPane"
    private Pane mainPane; // Value injected by FXMLLoader

    private PlayerShip playerShip;

    //private Scene scene;

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert mainPane != null : "fx:id=\"main\" was not injected: check your FXML file 'sample.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        //ImageView shipImageView = (ImageView) mainPane.getChildren().get(0);
        playerShip = new PlayerShip();
        mainPane.setOnKeyPressed(eventHandler);

//        mainPane.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
    }

    EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
        @Override
        public void handle(KeyEvent event) {
            // todo
            System.out.println("Handling event " + event.getEventType());
            event.consume();
        }
    };


    //public void setScene(Scene scene) { this.scene = scene; }
}
