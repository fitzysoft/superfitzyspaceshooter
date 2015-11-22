package com.fitzysoft.spaceshooter;

import carlfx.gameengine.GameWorld;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    // todo: build out an abstract base
    GameWorld gameWorld = new SFSpaceShooterGame(30, "Super Fitzy Space Shooter");

    @Override
    public void start(Stage primaryStage) throws Exception{
       //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));

//        EventHandler<KeyEvent> eventHandler = new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent event) {
//                // todo
//                System.out.println("Handling event " + event.getEventType());
//                event.consume();
//            }
//        };
//        root.setOnKeyPressed(eventHandler);


        primaryStage.setTitle("Super Fitzy Space Shooter");
        //primaryStage.setScene(new Scene(root));

        gameWorld.initialize(primaryStage);
        gameWorld.beginGameLoop();

        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
