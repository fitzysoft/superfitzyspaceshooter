package com.fitzysoft.spaceshooter;

import carlfx.gameengine.GameWorld;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    // todo: build out an abstract base
    GameWorld gameWorld = new SFSpaceShooterGame(30, "Super Fitzy Space Shooter");

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Super Fitzy Space Shooter");

        gameWorld.initialize(primaryStage);
        gameWorld.beginGameLoop();

        //primaryStage.setFullScreen(true);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
