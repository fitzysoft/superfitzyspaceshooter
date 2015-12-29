package com.fitzysoft.spaceshooter;

import javafx.scene.image.Image;
import carlfx.gameengine.Sprite;
import javafx.scene.image.ImageView;
import javafx.scene.media.AudioClip;

/**
 * Created by James FitzGerald on 11/1/15.
 */
public class PlayerShip extends Sprite {

    private static final double shipRotate = 2.0;
    private static final int maxShipSpeed = 16;
    private static final double shipAccel = 0.25;

    // close enough?
    private static final double degToRConst = 3.14159265358979323846264338327950288419716939937510 / 180;

    private double shipXSpeed = 0;
    private double shipYSpeed = 0;
    private double shipSpeed = 0;


    public PlayerShip() {
        // Load one image.
        ImageView imageView = new ImageView();
        imageView.setImage(new Image("file:///Users/jamesfitzgerald/Documents/personal-devel/superfitzy-javafx/src/com/fitzysoft/spaceshooter/ship1.png", true));
        //getClass().getClassLoader().getResource("laser_2.mp3")
        //imageView.setImage(new Image("file://./ship1.png", true));
        imageView.setCache(true);
        imageView.setFitWidth(64);
        imageView.setFitHeight(64);
        node = imageView;

        node.setLayoutX(100);
        node.setLayoutY(100);
        node.setVisible(true);


        //new Image(getClass().getClassLoader().getResource("ship.png").toExternalForm(), true);

        // todo: Create flipbook of different angles
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

    public void thrustShip(PlayerThrustDirection thrust) {
        thrustDirection = thrust;
        //thrustFirstPress = firstPress;
    }


    @Override
    public void update() {
        // todo: move ship
//        node.setVisible(true);

        // todo: Check screen coordinates
        // Will trap ship to the screen for now
        // and add in scrolling a little later
        //


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
        // And make the sprite move
        node.setTranslateX(node.getTranslateX() + shipXSpeed);
        node.setTranslateY(node.getTranslateY() + shipYSpeed);

    }
}
