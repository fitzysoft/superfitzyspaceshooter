package com.fitzysoft.spaceshooter;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;

/**
 * Created by James FitzGerald on 11/1/15.
 */
public class PlayerShip {
    private ImageView imageView;

    PlayerShip(ImageView imageView) {
        this.imageView = imageView;
        //this.imageView.addEventHandler(KeyEvent.KEY_PRESSED, eventHandler);
        //this.imageView.setOnKeyPressed(eventHandler);
    }
//    final EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
//        @Override
//        public void handle(KeyEvent event) {
//            // todo
//            System.out.println("Handling event " + event.getEventType());
//            event.consume();
//        }
//    };
}
