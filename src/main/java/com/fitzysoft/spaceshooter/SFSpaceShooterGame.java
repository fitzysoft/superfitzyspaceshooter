package com.fitzysoft.spaceshooter;

import carlfx.gameengine.GameWorld;
import carlfx.gameengine.Sprite;
import com.fitzysoft.particles.ParticleEffectsManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.logging.*;
// for the background - will be refactored
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;

// todo: Address the deprecation warning "This application, or a library it uses, is using the deprecated Carbon Component Manager for hosting Audio Units. Support for this will be removed in a future release. Also, this makes the host incompatible with version 3 audio units. Please transition to the API's in AudioComponent.h."
// looks like it is at the C level
// todo: Add game-over logic, restart etc...
// todo: Add scoring
// todo: Consider refactoring Kai's cool beats into its own class
// todo: Introduce some very cool particle physic based explosion effects

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

    // todo: look for a more elegant approach
    private boolean widthChanged = false;
    private boolean heightChanged = false;
    private boolean levelSetup = false;
    private int frameCounter = 0;

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
        setGameSurface(new Scene(getSceneNodes(), Constants.initialScreenWidth, Constants.initialScreenHeight));
        stage.setScene(getGameSurface());
        stage.setFullScreen(true);

        setupBackground();
    }

    // Once the screen has resized to fullscreen we will set the level up
    public void setupLevel() {
        if (!levelSetup) {
            // create spaceship
            createPlayerShip();

            ParticleEffectsManager particleEffectsManager = new ParticleEffectsManager((int)getGameSurface().getWidth(),
                    (int)getGameSurface().getHeight());
            gameContext.setParticleEffectsManager(particleEffectsManager);
            getSceneNodes().getChildren().add(particleEffectsManager);

            // create aliens
            //
            gameContext.enemyWave.createEnemies(gameContext.currentLevel);


            // Setup our input handlers
            setupInput();

            // load Kai's cool beats
            //
            playBackgroundMusic();

            setupScoreMessage();

            // Let's just start the action right now
            resetLevel();
        }
        levelSetup = true;
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
        // if no enemies left start the next wave
        if (gameContext.getEnemyWave().allDead()) {
            gameContext.getEnemyWave().setCreateNextWaveOnNextUpdate();
        }
    }

    public void playerHit() {
        // todo: lose life
        //gameContext.setGameState(GameContext.GameState.PLAYER_KILLED);
        if (gameContext.getLivesProperty().intValue() <= 0) {
            // its game over man!
            gameOver();
        } else {
            resetLevel();
        }
    }

    private void resetLevel() {
        showGameMessage("Player Ready", Color.GREENYELLOW);
        gameContext.setGameState(GameContext.GameState.READY_TO_START);
        doIn(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                startLevel();
                return null;
            }
        }, 3000);
    }

    private void startLevel() {
        gameContext.setGameState(GameContext.GameState.LEVEL_STARTED);
        clearPlayerReadyMessage();
    }

    private void gameOver() {

        showGameMessage("Game Over! Score: " + gameContext.getScoreProperty().intValue(), Color.ORANGERED);
        gameContext.setGameState(GameContext.GameState.GAME_OVER);
        doIn(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                gameContext.getLivesProperty().set(Constants.LIVES);
                gameContext.getScoreProperty().set(0);
                gameContext.setCurrentLevel(1);
                resetLevel();
                return null;
            }
        }, 15000);
    }

    // todo: You know below is very ugly!!!
    // todo: consider throw away this state stuff in favour of property listening / reactive style
    //private GameContext.GameState newState;
    private boolean frameCounterActive;
    private Callable<Void> f;   // todo: will refactor into a structure and have a list of them
    private void doIn(Callable<Void> f, int milliseconds) {
        // todo: only support one thing at a time right now, change the use of frameCounter to something more intelligent
        //
        // todo: hardcoded fpms
        this.frameCounter = (int) (0.006 * (double) milliseconds);
        this.f = f;
        frameCounterActive = true;
        //this.newState = newState;
    }

    private void frameDecrement() {
        if (frameCounterActive) {
            if (frameCounter == 0) {
                frameCounterActive = false;
                try {
                    f.call();
                } catch (Exception ex) {
                    // nada
                }

            } else {
                frameCounter--;
            }
        }

    }

    private double timeRemaining() {
        return frameCounter / 0.006;
    }

    // todo: refactor to use property binding instead
    private Text playerReadyTextNode;

    public void showGameMessage(String message, Color color) {
        // todo: experiment with 'animations' - start one here to make it glow in and out
        if (playerReadyTextNode == null) {
            playerReadyTextNode = new Text(message);
            getSceneNodes().getChildren().add(playerReadyTextNode);
            playerReadyTextNode.setFont(new Font(Constants.FONT_SIZE));
            playerReadyTextNode.setTextAlignment(TextAlignment.CENTER);
        }

        playerReadyTextNode.setText(message);
        playerReadyTextNode.setFill(color);
        playerReadyTextNode.setX(getGameSurface().getWidth()/2 - playerReadyTextNode.getBoundsInLocal().getWidth()/2);
        playerReadyTextNode.setY(getGameSurface().getHeight() / 2 /*- playerReadyTextNode.getBoundsInLocal().getHeight()/2 */);
        playerReadyTextNode.setVisible(true);
    }

    private Text scoreTextNode;
    private Text livesLeftTextNode;
    private Text levelTextNode;

    private void setupScoreMessage() {
        scoreTextNode = new Text();
        getSceneNodes().getChildren().add(scoreTextNode);
        Font font = new Font(20);
        scoreTextNode.setX(5.0);
        scoreTextNode.setY(30.0);
        scoreTextNode.setFont(font);
        scoreTextNode.setFill(Color.YELLOWGREEN);
        scoreTextNode.setText("Score: " + gameContext.getScoreProperty().intValue());
        scoreTextNode.setVisible(true);

        gameContext.getScoreProperty().addListener((observable, oldValue, newValue) -> {
            scoreTextNode.setText("Score: " + gameContext.getScoreProperty().intValue());
        });

        livesLeftTextNode = new Text();
        getSceneNodes().getChildren().add(livesLeftTextNode);
        livesLeftTextNode.setX(5);
        livesLeftTextNode.setY(60);
        livesLeftTextNode.setFont(font);
        livesLeftTextNode.setFill(Color.YELLOWGREEN);
        livesLeftTextNode.setText("Lives: " + gameContext.getLivesProperty().intValue());
        gameContext.getLivesProperty().addListener((observable, oldValue, newValue) -> {
                    livesLeftTextNode.setText("Lives: " + gameContext.getLivesProperty().intValue());
                    if (newValue.intValue() <= 0) {
                        // Its game over man!
                        // todo: game over logic
                        //gameContext.setCurrentLevel(1);
                    }
                }
        );

        levelTextNode = new Text();
        getSceneNodes().getChildren().add(levelTextNode);
        levelTextNode.setX(5);
        levelTextNode.setY(90);
        levelTextNode.setFont(font);
        levelTextNode.setFill(Color.YELLOWGREEN);
        levelTextNode.setText("Level: " + gameContext.getCurrentLevel() + 1);
        gameContext.currentLevelProperty().addListener((observable, oldValue, newValue) -> {
            levelTextNode.setText("Level: " + gameContext.getCurrentLevel());
        });

    }

    private void clearPlayerReadyMessage() {
        //getSceneNodes().getChildren().remove(playerReadyTextNode);
        if (playerReadyTextNode != null) {
            playerReadyTextNode.setVisible(false);
        }

    }

    // very much a work in progress - will likely shift some of this to the SoundManager class
    private void playBackgroundMusic() {
        logger.info("playBackgroundMusic");

        // We want sfsong (which is index 0) to come up more often
        //
        int nextSongIndex = rng.nextInt(kaisCoolBeats.length+1);
        if (nextSongIndex > kaisCoolBeats.length) {
            nextSongIndex = 0;
        }

        String songToPlay = getClass().getClassLoader().getResource(
                kaisCoolBeats[nextSongIndex]).toString();
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
            widthChanged = true;
            if (widthChanged && heightChanged) {
                setupLevel();
            }
        });
        getGameSurface().heightProperty().addListener((observable, oldValue, newValue) -> {
            logger.info("Height changed from " + oldValue + "to" + newValue);
            background.setFitHeight((double) newValue);
            heightChanged = true;
            if (widthChanged && heightChanged) {
                setupLevel();
            }
        });

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
    protected void updateSprites() {

        frameDecrement();

        if (gameContext.getParticleEffectsManager() != null) {
            gameContext.getParticleEffectsManager().update();
        }

        super.updateSprites();
    }

    @Override
    protected void handleUpdate(Sprite sprite) {
        super.handleUpdate(sprite);
        sprite.update();
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
    private void setupInput() {
        EventHandler eventHandler = new EventHandler<KeyEvent>() {

            private boolean firstThrust = false;
            @Override
            public void handle(KeyEvent event) {

                boolean gameStarted = gameContext.getGameState() == GameContext.GameState.LEVEL_STARTED;
                // you can only use the keys when the level has started

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
                        if (gameStarted) {
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
        getGameSurface().setOnKeyPressed(eventHandler);
        getGameSurface().setOnKeyReleased(eventHandler);
    }

}
