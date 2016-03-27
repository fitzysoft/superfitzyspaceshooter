package com.fitzysoft.spaceshooter;

import carlfx.gameengine.GameWorld;
//import carlfx.gameengine.SoundManager;
import carlfx.gameengine.Sprite;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

//import javafx.scene.media.AudioClip;
//import java.net.MalformedURLException;
//import java.net.URL;
import java.util.Random;
import java.util.logging.*;


/**
 * Created by James FitzGerald on 11/2/15.
 */
public class SFSpaceShooterGame extends GameWorld {

    private static Logger logger = Logger.getLogger("com.fitzysoft.sfs");
    private Random rng = new Random();
    private String[] kaisCoolBeats = {"sfssong.wav", "MR_FACE_GUY_Citrus_Game.wav", "MR_FACE_GUY_DD1_X.wav",
            "MR_FACE_GUY_Dogestep_1.wav", "MR_FACE_GUY_Geyser.wav", "MR_FACE_GUY_Level_HEX.wav",
            "MR_FACE_GUY_Life_n_That_Over_Again.wav", "MR_FACE_GUY_Motion_MEX_2.wav", "MR_FACE_GUY_Motion_MEX.wav",
            "MR_FACE_GUY_Royality.wav", "MR_FACE_GUY_SMASHDOWN_HIT_CRAZE.wav", "MR_FACE_GUY_Stardown_Dance_1.wav",
            "MR_FACE_GUY_Static_Floater_MEX.wav", "MR_FACE_GUY_UNiSON!_dARtEd.wav"};
    private MediaPlayer backgroundMediaPlayer;

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
        playBackgroundMusic();
    }

    Application application;
    public void setApplication(Application application) {
        this.application = application;
    }

    public void quit() {
        Platform.exit();
        System.exit(0);
    }

    // very much a work in progress - will likely shift some of this to the SoundManager class
    private void playBackgroundMusic() {
        logger.info("playBackgroundMusic");
        String songToPlay = getClass().getClassLoader().getResource(
                kaisCoolBeats[rng.nextInt(kaisCoolBeats.length)]).toString();
        Media media = new Media(songToPlay);
        backgroundMediaPlayer = new MediaPlayer(media);
        logger.info("Going to play " + songToPlay);
        // todo: make sure this is not dangerously recursive and will not blow out the stack
        // I don't think it will though, I think this method will end
        backgroundMediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                logger.info("Queuing up the next song");
                playBackgroundMusic();
            }
        });

        logger.info("calling play");
        backgroundMediaPlayer.play();
        logger.info("called play");
    }

    private void createPlayerShip() {
        playerShip = new PlayerShip(getSoundManager(), getGameSurface(), getSpriteManager(), this);
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
