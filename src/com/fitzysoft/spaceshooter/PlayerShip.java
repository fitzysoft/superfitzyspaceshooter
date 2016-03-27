package com.fitzysoft.spaceshooter;

import carlfx.gameengine.GameWorld;
import carlfx.gameengine.SoundManager;
import carlfx.gameengine.SpriteManager;
import com.sun.javafx.geom.Vec2d;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import carlfx.gameengine.Sprite;
import javafx.scene.image.ImageView;
import java.util.logging.*;

/**
 * Created by James FitzGerald on 11/1/15.
 */
public class PlayerShip extends Sprite {

    private static Logger logger = Logger.getLogger("com.fitzysoft.sfs");

    private static final double shipRotate = 8.0;
    private static final int maxShipSpeed = 32;
    private static final double shipAccel = 1.0;

    // close enough?
    private static final double degToRConst = 3.14159265358979323846264338327950288419716939937510 / 180;

    private double shipXSpeed = 0;
    private double shipYSpeed = 0;
    private double shipSpeed = 0;

    private SoundManager soundManager;
    private SpriteManager spriteManager;
    private Scene scene;
    GameWorld gameWorld;

    // todo: refactor this interface to just include GameWorld or something higher level
    public PlayerShip(SoundManager soundManager, Scene scene, SpriteManager spriteManager, GameWorld gameWorld) {

        this.scene = scene;
        this.soundManager = soundManager;
        this.spriteManager = spriteManager;
        this.gameWorld = gameWorld;

        // Load sound effects
        soundManager.loadSoundEffects("thrust", getClass().getClassLoader().getResource("thrust.wav"));
        soundManager.loadSoundEffects("slow", getClass().getClassLoader().getResource("slow.wav"));
        soundManager.loadSoundEffects("fire", getClass().getClassLoader().getResource("fire.wav"));

        // Load one image.
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(getClass().getClassLoader().getResource("ship1.png").toExternalForm(), true));
        imageView.setCache(true);
        node = imageView;

        node.setTranslateX(scene.getWidth() / 2);
        node.setTranslateY(scene.getHeight()/2);
        shipYSpeed = shipXSpeed = 1.5;
        node.setVisible(true);

        // todo: Consider creating a flipbook of different angles
        //

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
    private PlayerThrustDirection thrustDirection = PlayerThrustDirection.PLAYER_THRUST_OFF;
    private boolean thrustFirstPress = false;

    public void fire() {
        soundManager.playSound("fire");

        // For now I will start the missile from the center of the screen
        Point2D scenePoint = new Point2D(node.getTranslateX() + node.getBoundsInLocal().getMaxX()/2,
                node.getTranslateY() + node.getBoundsInLocal().getMaxY()/2);
        logger.info("Scene Point " + scenePoint.toString());
        Missile missile = new Missile(soundManager, scene, scenePoint, node.getRotate());
        // todo: trying to control z-order - has to be via the order the nodes are in the scene graph
        // there is currently no background image, this will have to be tweaked when we do
        gameWorld.getSceneNodes().getChildren().add(0, missile.node);
        spriteManager.addSprites(missile);
    }

    public void thrustShip(PlayerThrustDirection thrust) {
        thrustDirection = thrust;
        soundManager.playSound(thrust == PlayerThrustDirection.PLAYER_THRUST_FWD ? "thrust" : "slow");
    }


    @Override
    public void update() {

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
                double deltaX = Math.cos((node.getRotate() - 90) * degToRConst) * shipAccel;
                double deltaY = Math.sin((node.getRotate() - 90) * degToRConst) * shipAccel;

                // We have a maximum speed, so lets work out how fast we would be going. Shout out to Pythagorus!
                speed = Math.sqrt((shipXSpeed + deltaX)*(shipXSpeed + deltaX) + (shipYSpeed + deltaY)*(shipYSpeed + deltaY));
                // If it is not too fast then we can thrust in the desired direction
                if (speed < maxShipSpeed) {
                    shipXSpeed += deltaX;
                    shipYSpeed += deltaY;
                    shipSpeed = speed;

                } else {
                    // we are maxed out.  We should drift towards the new direction.
                    //
                    shipXSpeed = (shipXSpeed + deltaX) * shipSpeed/speed;
                    shipYSpeed = (shipYSpeed + deltaY) * shipSpeed/speed;
                }
                break;
            case PLAYER_THRUST_BACK:
                // lets slow down
                speed = shipSpeed - shipAccel;
                if (speed < 0) {
                    shipXSpeed = shipYSpeed = shipSpeed = 0;
                } else {
                    // and drift tw
                    shipXSpeed = shipXSpeed * (speed/shipSpeed);
                    shipYSpeed = shipYSpeed * (speed/shipSpeed);
                    shipSpeed = speed;
                }
        }

        // Check if we hit the edges of the screen
        if (!(shipXSpeed < 0 && node.getTranslateX() < 0) &&
                !(shipXSpeed > 0 && node.getTranslateX() > scene.getWidth() - node.getBoundsInLocal().getWidth())) {
            node.setTranslateX(node.getTranslateX() + shipXSpeed);
        } else {
            shipXSpeed = 0;
        }

        if (!(shipYSpeed < 0 && node.getTranslateY() < 0) &&
                !(shipYSpeed > 0 && node.getTranslateY() > scene.getHeight() - node.getBoundsInLocal().getHeight())) {
            node.setTranslateY(node.getTranslateY() + shipYSpeed);
        } else {
            shipYSpeed = 0;
        }
    }
}
