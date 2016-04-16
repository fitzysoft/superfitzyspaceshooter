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
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.util.Random;
import java.util.logging.*;
// for the background - will be refactored
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


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

    private GameContext gameContext;

    // hacking around a gap in my knowledge
    private boolean resizedBackground = false;
    private ImageView background;

    public SFSpaceShooterGame(int fps, String title){
        super(fps, title);
    }

    public void initialize(Stage stage) {
        stage.setTitle(getWindowTitle());

        logger.setLevel(Level.WARNING);

        gameContext = new GameContext();
        gameContext.setSfsGameWorld(this);
        gameContext.setSoundManager(getSoundManager());

        // Create the scene
        setSceneNodes(new Group());
        setGameSurface(new Scene(getSceneNodes(), Constants.screenWidth, Constants.screenHeight));
        //getGameSurface().setFill(Color.WHITE);
        stage.setScene(getGameSurface());
        stage.setFullScreen(true);

        setupBackground();

        // create spaceship
        createPlayerShip();

        // create aliens
        //
        gameContext.enemyWave.createEnemies(gameContext.currentLevel);

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

    public void enemyShot() {
        // todo: bump the score

        // if no enemies left start the next wave
        if (gameContext.getEnemyWave().allDead()) {
            gameContext.getEnemyWave().setCreateNextWaveOnNextUpdate();
        }
    }

    public void playerHit() {
        // todo: lose life
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

    // For now it is a static background, but we will end making a scrolling background
    //
    private void setupBackground() {
        background = new ImageView();
        // image obtained from : https://d1o50x50snmhul.cloudfront.net/wp-content/uploads/2016/04/ann11053e2-1200x800.jpg
        background.setImage(new Image(getClass().getClassLoader().getResource("ann11053e2-1200x800.png").toExternalForm(), true));
        background.setCache(true);
        background.setFitWidth(getGameSurface().getWidth());
        background.setFitHeight(getGameSurface().getHeight());
        background.setSmooth(true);
        background.setVisible(true);
        getSceneNodes().getChildren().add(Constants.BACKGROUND_NODE_LEVEL, background);
        getGameSurface().widthProperty().addListener((observable, oldValue, newValue) -> {
            logger.info("Width changed from " + oldValue + "to" + newValue);
            background.setFitWidth((double) newValue);
        });
        getGameSurface().heightProperty().addListener((observable, oldValue, newValue) -> {
            logger.info("Height changed from " + oldValue + "to" + newValue);
            background.setFitHeight((double) newValue);
        });

//
//        BackgroundImage backgroundImage = new BackgroundImage(
//                new Image(getClass().getClassLoader().getResource("ann11053e2-1200x800.png").toExternalForm(), true),
//                        BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.DEFAULT,
//                        BackgroundSize.DEFAULT);
//        getSceneNodes().getChildren().add(backgroundImage);

//        //getGameSurface().getRoot().get  .setBackground(new Background(backgroundImage));


//        // todo: this feels kind of ugly - I shouldn't have to use CSS
//        String imageUrl = getClass().getClassLoader().getResource("ann11053e2-1200x800.png").toExternalForm();
//        getGameSurface().getRoot().setStyle("-fx-background-image: url('" + imageUrl + "'); " +
//                "-fx-background-position: center center; " +
//                "-fx-background-repeat: stretch;");
    }

    private void createPlayerShip() {
        PlayerShip playerShip = new PlayerShip(gameContext);
        getSpriteManager().addSprites(playerShip);
        getSceneNodes().getChildren().add(Constants.PLAYERSHIP_NODE_LEVEL, playerShip.node);
        gameContext.setPlayerShip(playerShip);
    }

    private void createNextEnemyWave() {
        int nextLevel = gameContext.getCurrentLevel() + 1;
        gameContext.enemyWave.createEnemies(nextLevel);
        gameContext.setCurrentLevel(nextLevel);
    }

    @Override
    protected void handleUpdate(Sprite sprite) {

        // advance object
        sprite.update();
    }

    // todo: remove below, refactor super class
    @Override
    protected boolean handleCollision(Sprite spriteA, Sprite spriteB) {

        // todo: implement checks for player colliding with enemies
        // todo: implement checks for missiles colliding with enemies
        logger.info("Collision detected");
        return super.handleCollision(spriteA, spriteB);
    }

    @Override
    protected void cleanupSprites() {
        gameContext.getEnemyWave().cleanUpEnemies();

        // if no enemies left start the next wave
        // todo: I could shift much of this methods enemy logic into enemy wave including the code in enemyShot
        if (gameContext.getEnemyWave().isCreateNextWaveOnNextUpdate()) {
            logger.info("all enemies dead, creating the next wave");
            createNextEnemyWave();
        }
        super.cleanupSprites();
    }

    // todo: Do I have to worry about thread safety? I mean can input events and handleUpdate be called at the same time?
    private void setupInput(Stage primaryStage) {
        EventHandler eventHandler = new EventHandler<KeyEvent>() {

            private boolean firstThrust = false;
            @Override
            public void handle(KeyEvent event) {
                // todo: Handle thrust

                PlayerShip playerShip = gameContext.getPlayerShip();

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
                    case SPACE:
                        if (event.getEventType() == KeyEvent.KEY_PRESSED) {
                            playerShip.fire();
                            event.consume();
                        }
                        break;
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
