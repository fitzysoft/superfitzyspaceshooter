package com.fitzysoft.spaceshooter;

import carlfx.gameengine.GameWorld;
import carlfx.gameengine.SoundManager;
import carlfx.gameengine.Sprite;
import com.sun.javafx.geom.Vec2d;
import com.sun.tools.internal.jxc.ap.Const;
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
    private static final double missileSpeed = 48;
    private GameContext gameContext;
    private double angle;
    private double speedX;
    private double speedY;


    public Missile(GameContext gameContext, Point2D position, double angle) {
        this.gameContext = gameContext;
        this.angle = angle;

        // Load one image.
        ImageView imageView = new ImageView();
        imageView.setImage(new Image(getClass().getClassLoader().getResource("missile.png").toExternalForm(), true));
        imageView.setCache(true);
        node = imageView;

        // Position it
        node.setTranslateX(position.getX());
        node.setTranslateY(position.getY());
        node.setRotate(angle);
        node.setVisible(true);

        // Work out the velocity details while we are here
        // Based on the angle we work out how much along the Y and X axis we need to move
        speedX = Math.cos((node.getRotate() - 90) * Constants.degToRConst) * missileSpeed;
        speedY = Math.sin((node.getRotate() - 90) * Constants.degToRConst) * missileSpeed;

    }

    private boolean outOfBounds() {
        Scene scene = gameContext.getSfsGameWorld().getGameSurface();
        return (speedX < 0 && node.getTranslateX() < 0) ||
                (speedX > 0 && node.getTranslateX() > scene.getWidth() - node.getBoundsInLocal().getWidth()) ||
                (speedY < 0 && node.getTranslateY() < 0) ||
                (speedY > 0 && node.getTranslateY() > scene.getHeight() - node.getBoundsInLocal().getHeight());
    }

    @Override
    public void update() {
        node.setTranslateX(node.getTranslateX() + speedX);
        node.setTranslateY(node.getTranslateY()+speedY);
        boolean hitem = false;
        for (Enemy enemy: gameContext.enemyWave.getEnemies()) {
            if (simpleCollisionCheck(enemy)) {
                enemy.explode();
                hitem = true;
                break;
            }
        }

        // Check if we go off screen
        if (hitem || outOfBounds()) {
            GameWorld gameWorld = gameContext.getSfsGameWorld();
            // remove from the sprite manager
            handleDeath(gameWorld);
            // remove from the scene
            //
            gameWorld.getSceneNodes().getChildren().removeAll(node);
        }

    }
}
