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

    private SoundManager soundManager;
    private Scene scene;
    private double angle;

    public Missile(SoundManager soundManager, Scene scene, Point2D position, double angle) {
        this.scene = scene;
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
    }

    @Override
    public void update() {

        /*
        // todo : check if we go out of bounds and destroy
        // todo : collision stuff (maybe in gameworld)
        double deltaX = Math.cos((node.getRotate() - 90) * degToRConst); // * 0.5 + 5;
        double deltaY = Math.sin((node.getRotate() - 90) * degToRConst); // * 0.5 + 5l;

        node.setTranslateX(node.getTranslateX()+deltaX);
        node.setTranslateY(node.getTranslateY()+deltaY);
        */
    }
}
