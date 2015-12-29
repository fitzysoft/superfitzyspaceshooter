package com.fitzysoft.spaceshooter;

import javafx.scene.image.Image;
import carlfx.gameengine.Sprite;
import javafx.scene.image.ImageView;

import java.net.URL;

/**
 * Created by James FitzGerald on 11/1/15.
 */
public class PlayerShip extends Sprite {

    private static final double shipRotate = 2.0;

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

    public PlayerSteerDirection getPlayerSteerDirection() {
        return playerSteerDirection;
    }

    public void setPlayerSteerDirection(PlayerSteerDirection playerSteerDirection) {
        this.playerSteerDirection = playerSteerDirection;
    }

    private PlayerSteerDirection playerSteerDirection = PlayerSteerDirection.PLAYER_STEER_STRAIGHT;


    @Override
    public void update() {
        // todo: move ship
//        node.setVisible(true);
        //node.setTranslateX(node.getTranslateX() + 0.5);
        //node.setTranslateY(node.getTranslateY() + 0.5);

        switch (playerSteerDirection) {
            case PLAYER_STEER_LEFT:
                node.setRotate(-1*shipRotate + node.getRotate());
                break;
            case PLAYER_STEER_RIGHT:
                node.setRotate(shipRotate + node.getRotate());
                break;
        }

    }
}
