package com.fitzysoft.spaceshooter;

import carlfx.gameengine.GameWorld;
import carlfx.gameengine.Sprite;
import javafx.scene.CacheHint;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Group;

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
        getGameSurface().setFill(Color.BLACK);
        stage.setScene(getGameSurface());

        // create spaceship
        createPlayerShip();

        // create aliens
        //

        // load Kai's cool beats
        //
        // getClass().getClassLoader().getResource("SFSsong.WAV")
        try {
            getSoundManager().loadSoundEffects("background_music", new URL("file:///Users/jamesfitzgerald/Documents/personal-devel/superfitzy-javafx/src/com/fitzysoft/spaceshooter/sfssong.wav"));

        } catch (MalformedURLException e) {
            // todo...
            logger.severe(e.getMessage());
        }

        // Not sure I should do it here
        //
        getSoundManager().playSound("background_music");

    }

    private void createPlayerShip() {
        Scene gameSurface = getGameSurface();

//        playerShip.setTranslateX(gameSurface.getWidth()/2);
//        playerShip.setTranslateY(gameSurface.getHeight() / 2);
//        playerShip.setVisible(true);
//        playerShip.setCacheHint(CacheHint.SPEED);
        getSpriteManager().addSprites(playerShip);
        getSceneNodes().getChildren().add(0, playerShip.node);

    }

    @Override
    protected void handleUpdate(Sprite sprite) {
        // advance object
        sprite.update();
    }

}
