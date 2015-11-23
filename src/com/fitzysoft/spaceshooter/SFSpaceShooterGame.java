package com.fitzysoft.spaceshooter;

import carlfx.gameengine.GameWorld;
import carlfx.gameengine.Sprite;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

import javafx.scene.media.AudioClip;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.*;


/**
 * Created by James FitzGerald on 11/2/15.
 */
public class SFSpaceShooterGame extends GameWorld {

    private static Logger logger = Logger.getLogger("com.fitzysoft.sfs");

    public SFSpaceShooterGame(int fps, String title){
        super(fps, title);
    }

    public PlayerShip playerShip = new PlayerShip();

    public void initialize(Stage stage) {
        stage.setTitle(getWindowTitle());

        // Create the scene
        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), 1024, 768));
        getGameSurface().setFill(Color.WHITE);
        stage.setScene(getGameSurface());

        // create spaceship
        createPlayerShip();

        // create aliens
        //
        // todo: create some aliens

        // Setup our input handlers
        setupInput(stage);

        // load Kai's cool beats
        //
        // getClass().getClassLoader().getResource("SFSsong.WAV")
        try {
            getSoundManager().loadSoundEffects("background_music", new URL("file:///Users/jamesfitzgerald/Documents/personal-devel/superfitzy-javafx/src/com/fitzysoft/spaceshooter/sfssong.wav"));

        } catch (MalformedURLException e) {
            // todo...
            logger.severe(e.getMessage());
        }

        // A little background music if you please
        //
        getSoundManager().playSound("background_music", AudioClip.INDEFINITE);

    }

    private void createPlayerShip() {
        Scene gameSurface = getGameSurface();

        playerShip.node.setTranslateX(gameSurface.getWidth()/2);
        playerShip.node.setTranslateY(gameSurface.getHeight() / 2);
        getSpriteManager().addSprites(playerShip);
        getSceneNodes().getChildren().add(0, playerShip.node);

    }

    @Override
    protected void handleUpdate(Sprite sprite) {
        // advance object
        sprite.update();
    }

    private void setupInput(Stage primaryStage) {
        EventHandler eventHandler = new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                // todo
                logger.info("key hit");
            }
        };

        // Initialize input
        primaryStage.getScene().setOnKeyPressed(eventHandler);
    }

}
