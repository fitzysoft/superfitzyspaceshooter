package com.fitzysoft.spaceshooter;

import carlfx.gameengine.SoundManager;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import carlfx.gameengine.Sprite;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.logging.*;

/**
 * Created by James FitzGerald on 11/1/15.
 */
public class PlayerShip extends Sprite {

    private static Logger logger = Logger.getLogger("com.fitzysoft.sfs");

    private static final double shipRotate = 8.0;
    private static final double maxShipSpeed = 32;
    private static final double minShipSpeed = 2.5;
    private static final double shipAccel = 1.0;

    private double shipXSpeed = 0;
    private double shipYSpeed = 0;
    private double shipSpeed = minShipSpeed;

    // frame rate should be 30 fps
    private static int dyingStepTotal = 90;
    private int dyingStepCount = 0;

    private GameContext gameContext;

    enum PlayerState {ALIVE, DYING, DEAD};
    private PlayerState state = PlayerState.ALIVE; // w.i.p

    // todo: refactor this interface to take a context object for the various managers
    //
    public PlayerShip(GameContext gameContext) {

        this.gameContext = gameContext;

        // Load sound effects
        SoundManager soundManager = gameContext.getSoundManager();
        soundManager.loadSoundEffects("thrust", getClass().getClassLoader().getResource("thrust.wav"));
        soundManager.loadSoundEffects("slow", getClass().getClassLoader().getResource("slow.wav"));
        soundManager.loadSoundEffects("fire", getClass().getClassLoader().getResource("fire.wav"));
        soundManager.loadSoundEffects("player_death", getClass().getClassLoader().getResource("player_death.wav"));

        // Load one image.
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(getClass().getClassLoader().getResource("ship1.png").toExternalForm(), true));
        imageView.setCache(true);
        node = imageView;

        Scene scene = gameContext.getSfsGameWorld().getGameSurface();
        node.setTranslateX(scene.getWidth() / 2);
        node.setTranslateY(scene.getHeight() / 2);

        node.setVisible(true);
    }

    public enum PlayerSteerDirection { PLAYER_STEER_LEFT, PLAYER_STEER_STRAIGHT, PLAYER_STEER_RIGHT};

    public PlayerSteerDirection getSteerDirection() {
        return steerDirection;
    }

    public void setSteerDirection(PlayerSteerDirection steerDirection) {
        this.steerDirection = steerDirection;
    }

    private PlayerSteerDirection steerDirection = PlayerSteerDirection.PLAYER_STEER_STRAIGHT;


    public enum PlayerThrustDirection {PLAYER_THRUST_OFF, PLAYER_THRUST_FWD, PLAYER_THRUST_BACK};
    private PlayerThrustDirection thrustDirection = PlayerThrustDirection.PLAYER_THRUST_FWD;

    public void fire() {
        gameContext.getSoundManager().playSound("fire");

        // For now I will start the missile from the center of the screen
        Point2D scenePoint = new Point2D(node.getTranslateX() + node.getBoundsInLocal().getMaxX()/2,
                node.getTranslateY() + node.getBoundsInLocal().getMaxY()/2);
        logger.info("Scene Point " + scenePoint.toString());
        Missile missile = new Missile(gameContext, scenePoint, node.getRotate());
        gameContext.getSfsGameWorld().getSceneNodes().getChildren().add(Constants.MISSILE_NODE_LEVEL, missile.node);
        gameContext.getSfsGameWorld().getSpriteManager().addSprites(missile);
    }

    public void thrustShip(PlayerThrustDirection thrust) {
        thrustDirection = thrust;
        gameContext.getSoundManager().playSound(
                thrust == PlayerThrustDirection.PLAYER_THRUST_FWD ? "thrust" : "slow");
    }

    public void enemyHitMe() {
        if (state == PlayerState.ALIVE) {
            gameContext.getSoundManager().playSound("player_death");
            state = PlayerState.DYING;
            killEnemies();
            gameContext.loseLife();
        }

    }

    // Move ship according to direction, thrust, or brake
    // check for collisions
    private void aliveUpdate() {
        // Steer
        switch (steerDirection) {
            case PLAYER_STEER_LEFT:
                node.setRotate(-1*shipRotate + node.getRotate());
                break;
            case PLAYER_STEER_RIGHT:
                node.setRotate(shipRotate + node.getRotate());
                break;
        }

        // Thrust
        double speed;
        switch (thrustDirection) {
            case PLAYER_THRUST_FWD:
                // Based on the angle we work out how much along the Y and X axis we need to move
                double deltaX = Math.cos((node.getRotate() - 90) * Constants.degToRConst) * shipAccel;
                double deltaY = Math.sin((node.getRotate() - 90) * Constants.degToRConst) * shipAccel;

                // We have a maximum speed, so lets work out how fast we would be going. Shout out to Pythagorus!
                speed = Math.sqrt((shipXSpeed + deltaX) * (shipXSpeed + deltaX) + (shipYSpeed + deltaY) * (shipYSpeed + deltaY));
                // If it is not too fast then we can thrust in the desired direction
                if (speed < maxShipSpeed) {
                    shipXSpeed += deltaX;
                    shipYSpeed += deltaY;
                    shipSpeed = speed;

                } else {
                    // we are maxed out.  We should drift towards the new direction.
                    //
                    shipXSpeed = (shipXSpeed + deltaX) * shipSpeed / speed;
                    shipYSpeed = (shipYSpeed + deltaY) * shipSpeed / speed;
                }
                break;
            case PLAYER_THRUST_BACK:
                // lets slow down
                speed = shipSpeed - shipAccel;
                if (speed < minShipSpeed) {
                    speed = minShipSpeed;
                }

                shipXSpeed = shipXSpeed * (speed / shipSpeed);
                shipYSpeed = shipYSpeed * (speed / shipSpeed);
                shipSpeed = speed;
        }

        // Check if we hit the edges of the screen
        if (!(shipXSpeed < 0 && node.getTranslateX() < 0) &&
                !(shipXSpeed > 0 && node.getTranslateX() >
                        gameContext.getSfsGameWorld().getGameSurface().getWidth() - node.getBoundsInLocal().getWidth())) {
            node.setTranslateX(node.getTranslateX() + shipXSpeed);
        } else {
            shipXSpeed = 0;
        }

        if (!(shipYSpeed < 0 && node.getTranslateY() < 0) &&
                !(shipYSpeed > 0 && node.getTranslateY() > gameContext.getSfsGameWorld().getGameSurface().getHeight() - node.getBoundsInLocal().getHeight())) {
            node.setTranslateY(node.getTranslateY() + shipYSpeed);
        } else {
            shipYSpeed = 0;
        }
    }

    // Some cool effect
    private void dyingUpdate() {
        dyingStepCount++;
        if (dyingStepCount >= dyingStepTotal) {
            state = PlayerState.DEAD;
        }
        // todo: make it some cool effect, for now I might just play with the transparency
        // todo: I am experimenting here
        //SuperFitzySpriteEffects.blurryFadeOut(node, dyingStepCount, dyingStepCount);
    }

    private void killEnemies() {
        ArrayList<Enemy> enemies = gameContext.getEnemyWave().getEnemies();
        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).explode(false);
        }
    }

    private void playerDead() {
        // make the ship alive and reset effects, position and speed
        // and then finally kill all the enemies
        state = PlayerState.ALIVE;
        node.setOpacity(1.0);
        node.setEffect(null);
        Scene scene = gameContext.getSfsGameWorld().getGameSurface();
        node.setTranslateX(scene.getWidth() / 2);
        node.setTranslateY(scene.getHeight() / 2);
        node.setRotate(0.0);

        shipSpeed = minShipSpeed;

        // Tell the game world we are now fully dead
        gameContext.getSfsGameWorld().playerHit();
    }

    @Override
    public void update() {

        if (gameContext.getGameState() == GameContext.GameState.READY_TO_START) {
            // do nothing while in this state
            return;
        }

        switch (state) {
            case ALIVE:
                aliveUpdate();
                break;

            case DYING:
                dyingUpdate();
                break;

            case DEAD:
            default:
                playerDead();
                break;
        }
    }

}
