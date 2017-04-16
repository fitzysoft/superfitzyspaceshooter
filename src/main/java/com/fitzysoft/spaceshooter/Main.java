package com.fitzysoft.spaceshooter;

import carlfx.gameengine.GameWorld;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

public class Main extends Application {

    // todo: build out an abstract base
    SFSpaceShooterGame gameWorld = new SFSpaceShooterGame(30, "Super Fitzy Space Shooter");

    @Override
    public void start(Stage primaryStage) throws Exception{

        primaryStage.setTitle("Super Fitzy Space Shooter");
        primaryStage.setOnCloseRequest(e -> Platform.exit());
        gameWorld.initialize(primaryStage);
        gameWorld.setApplication(this);
        gameWorld.beginGameLoop();

        //primaryStage.setFullScreen(true);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);

    }
}
