package com.fitzysoft.spaceshooter;

import carlfx.gameengine.GameWorld;
import carlfx.gameengine.Sprite;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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

    public PlayerShip playerShip;

    public void initialize(Stage stage) {
        stage.setTitle(getWindowTitle());

        // Create the scene
        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), 1024, 768));
        getGameSurface().setFill(Color.WHITE);
        stage.setScene(getGameSurface());
        stage.setFullScreen(true);

        // create spaceship
        createPlayerShip();

        // create aliens
        //
        // todo: create some aliens

        // Setup our input handlers
        setupInput(stage);

        // load Kai's cool beats
        //
        getSoundManager().loadSoundEffects("background_music", getClass().getClassLoader().getResource("sfssong.wav"));

        // A little background music if you please
        //
        getSoundManager().playSound("background_music", AudioClip.INDEFINITE);
    }

    Application application;
    public void setApplication(Application application) {
        this.application = application;
    }

    public void quit() {
        Platform.exit();
        System.exit(0);
    }

    private void createPlayerShip() {
        playerShip = new PlayerShip(getSoundManager(), getGameSurface());
        getSpriteManager().addSprites(playerShip);
        getSceneNodes().getChildren().add(0, playerShip.node);

    }

    @Override
    protected void handleUpdate(Sprite sprite) {
        // advance object
        sprite.update();
    }

    @Override
    protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {

        // todo: implement checks for player colliding with enemies
        // todo: implement checks for missiles colliding with enemies



        return super.handleCollision(spriteA, spriteB);
    }

    // todo: Do I have to worry about thread safety? I mean can input events and handleUpdate be called at the same time?
    private void setupInput(Stage primaryStage) {
        EventHandler eventHandler = new EventHandler<KeyEvent>() {

            private boolean firstThrust = false;
            @Override
            public void handle(KeyEvent event) {
                // todo: Handle thrust


                switch (event.getCode()) {
                    case ESCAPE:
                        quit();
                        break;
                    //case KP_LEFT:
                    case LEFT:
                        playerShip.setSteerDirection(event.getEventType() == KeyEvent.KEY_PRESSED ?
                                PlayerShip.PlayerSteerDirection.PLAYER_STEER_LEFT :
                                PlayerShip.PlayerSteerDirection.PLAYER_STEER_STRAIGHT);
                        event.consume();
                        break;
                    //case KP_RIGHT:
                    case RIGHT:
                        playerShip.setSteerDirection(event.getEventType() == KeyEvent.KEY_PRESSED ?
                                PlayerShip.PlayerSteerDirection.PLAYER_STEER_RIGHT :
                                PlayerShip.PlayerSteerDirection.PLAYER_STEER_STRAIGHT);
                        event.consume();
                        break;
                    case UP:
                    case DOWN:
                        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                            if (!firstThrust) {
                                playerShip.thrustShip(event.getCode() == KeyCode.UP ?
                                        PlayerShip.PlayerThrustDirection.PLAYER_THRUST_FWD :
                                        PlayerShip.PlayerThrustDirection.PLAYER_THRUST_BACK);
                                firstThrust = true;
                            }
                            event.consume();
                        } else {
                            firstThrust = false;
                        }
                        break;
                    case CONTROL:
                        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                            playerShip.fire();
                            event.consume();
                        }
                    default:
                        //
                }

            }

        };

        // Initialize input
        primaryStage.getScene().setOnKeyPressed(eventHandler);
        primaryStage.getScene().setOnKeyReleased(eventHandler);
    }

}
