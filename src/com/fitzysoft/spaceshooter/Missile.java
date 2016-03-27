package com.fitzysoft.spaceshooter;

import carlfx.gameengine.GameWorld;
import carlfx.gameengine.SoundManager;
import carlfx.gameengine.Sprite;
import com.sun.javafx.geom.Vec2d;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Created by James FitzGerald on 12/30/15.
 */
public class Missile extends Sprite {

    // todo: I have repeated myself
    // close enough?
    private static final double degToRConst = 3.14159265358979323846264338327950288419716939937510 / 180;
    private static final double missileSpeed = 32;
    private SoundManager soundManager;
    private Scene scene;
    private GameWorld gameWorld;
    private double angle;
    private double speedX;
    private double speedY;


    public Missile(SoundManager soundManager, Scene scene, GameWorld gameWorld, Point2D position, double angle) {
        this.scene = scene;
        this.gameWorld = gameWorld;
        this.soundManager = soundManager;
        this.angle = angle;

        // Load one image.
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(getClass().getClassLoader().getResource("missile.png").toExternalForm(), true));
        imageView.setCache(true);
        node = imageView;
        // Position it
        node.setTranslateX(position.getX());
        node.setTranslateY(position.getY());
        //node.setTranslateX(512.0);
        //node.setTranslateY(512.0);
        node.setRotate(angle);
        node.setVisible(true);

        // Work out the velocity details while we are here
        // Based on the angle we work out how much along the Y and X axis we need to move
        speedX = Math.cos((node.getRotate() - 90) * degToRConst) * missileSpeed;
        speedY = Math.sin((node.getRotate() - 90) * degToRConst) * missileSpeed;

    }

    @Override
    public void update() {

        if ((speedX < 0 && node.getTranslateX() < 0) ||
            (speedX > 0 && node.getTranslateX() > scene.getWidth() - node.getBoundsInLocal().getWidth()) ||
            (speedY < 0 && node.getTranslateY() < 0) ||
            (speedY > 0 && node.getTranslateY() > scene.getHeight() - node.getBoundsInLocal().getHeight())) {

            // remove from the sprite manager
            handleDeath(gameWorld);

            // remove from the scene
            //
            gameWorld.getSceneNodes().getChildren().removeAll(node);
        }

        node.setTranslateX(node.getTranslateX()+speedX);
        node.setTranslateY(node.getTranslateY()+speedY);


        /*
        // todo : check if we go out of bounds and destroy
        // todo : collision stuff (maybe in gameworld)
        double deltaX = Math.cos((node.getRotate() - 90) * degToRConst); // * 0.5 + 5;
        double deltaY = Math.sin((node.getRotate() - 90) * degToRConst); // * 0.5 + 5l;

        node.setTranslateX(node.getTranslateX()+speedX);
        node.setTranslateY(node.getTranslateY()+speedY);
        */
    }
}
