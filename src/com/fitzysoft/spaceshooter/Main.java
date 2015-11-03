package com.fitzysoft.spaceshooter;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

//        EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                // todo
//                System.out.println("Handling event " + event.getEventType());
//                event.consume();
//            }
//        };
        root.setFocusTraversable(true);
//        root.setOnKeyPressed(eventHandler);
        root.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent kEvent) {
                if (kEvent.getCode() == KeyCode.SPACE) {
                    System.out.println(" Space Bar pressed");
                }

            }
        });

        primaryStage.setTitle("Super Fitzy Space Shooter");
        primaryStage.setScene(new Scene(root));
        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
